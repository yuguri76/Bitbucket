package com.sparta.bitbucket.board.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.bitbucket.auth.entity.Role;
import com.sparta.bitbucket.auth.entity.User;
import com.sparta.bitbucket.auth.service.UserService;
import com.sparta.bitbucket.board.dto.BoardCreateRequestDto;
import com.sparta.bitbucket.board.dto.BoardEditRequestDto;
import com.sparta.bitbucket.board.dto.BoardMemberResponseDto;
import com.sparta.bitbucket.board.dto.BoardResponseDto;
import com.sparta.bitbucket.board.dto.BoardWithMemberListResponseDto;
import com.sparta.bitbucket.board.entity.Board;
import com.sparta.bitbucket.board.entity.BoardMember;
import com.sparta.bitbucket.board.repository.BoardMemberRepository;
import com.sparta.bitbucket.board.repository.BoardRepository;
import com.sparta.bitbucket.common.entity.ErrorMessage;
import com.sparta.bitbucket.common.entity.StatusMessage;
import com.sparta.bitbucket.common.exception.auth.UnauthorizedException;
import com.sparta.bitbucket.common.exception.board.BoardMemberDuplicateException;
import com.sparta.bitbucket.common.exception.board.BoardTitleDuplicateException;
import com.sparta.bitbucket.common.exception.card.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "board service")
@Service
@RequiredArgsConstructor
public class BoardService {

	private final static int PAGE_SIZE = 5;

	private final BoardRepository boardRepository;
	private final BoardMemberRepository boardMemberRepository;
	private final UserService userService;

	public List<BoardResponseDto> getAllBoards(int page, String sortBy) {

		Sort sort = Sort.by(Sort.Direction.DESC, sortBy);
		Pageable pageable = PageRequest.of(page, PAGE_SIZE, sort);

		Page<BoardResponseDto> boardResponseDtoPage = boardRepository.findAll(pageable)
			.map((board) -> BoardResponseDto
				.builder()
				.board(board)
				.build());
		List<BoardResponseDto> boardResponseDtoList = boardResponseDtoPage.getContent();

		if (boardResponseDtoList.isEmpty()) {
			throw new EntityNotFoundException(ErrorMessage.NOT_FOUND_BOARD.getMessage());
		}

		return boardResponseDtoList;
	}

	public BoardWithMemberListResponseDto getBoard(Long boardId, User user) {

		Board board = findBoardById(boardId);

		if (!isUserBoardMember(board.getId(), user.getId())) {
			throw new UnauthorizedException(ErrorMessage.UNAUTHORIZED_BOARD_MEMBER);
		}

		List<BoardMemberResponseDto> boardMemberResponseDtoList = board.getBoardMemberList()
			.stream()
			.map((boardMember) -> BoardMemberResponseDto
				.builder()
				.boardMember(boardMember)
				.build())
			.toList();

		return BoardWithMemberListResponseDto
			.builder()
			.board(board)
			.memberList(boardMemberResponseDtoList)
			.build();
	}

	@Transactional
	public BoardResponseDto createBoard(BoardCreateRequestDto requestDto, String email) {

		User user = userService.findUserByEmail(email);

		if (!isUserManager(user)) {
			throw new UnauthorizedException(ErrorMessage.UNAUTHORIZED_MANAGER);
		}

		if (boardRepository.findByTitle(requestDto.getTitle()).isPresent()) {
			throw new BoardTitleDuplicateException(ErrorMessage.BOARD_TITLE_DUPLICATE);
		}

		Board board = Board
			.builder()
			.title(requestDto.getTitle())
			.content(requestDto.getContent())
			.user(user)
			.build();

		BoardMember boardMember = BoardMember
			.builder()
			.board(board)
			.user(user)
			.build();

		board.addBoardMember(boardMember);

		Board saveBoard = boardRepository.save(board);
		boardMemberRepository.save(boardMember);

		user.addBoard(saveBoard);

		return BoardResponseDto
			.builder()
			.board(saveBoard)
			.build();
	}

	@Transactional
	public BoardMemberResponseDto inviteBoard(Long boardId, String invitedUserEmail, User user) {

		Board board = findBoardById(boardId);

		if (!isUserManager(user)) {
			throw new UnauthorizedException(ErrorMessage.UNAUTHORIZED_MANAGER);
		}

		if (!isUserBoardMember(board.getId(), user.getId())) {
			throw new UnauthorizedException(ErrorMessage.UNAUTHORIZED_BOARD_MEMBER);
		}

		User invitedUser = userService.findUserByEmail(invitedUserEmail);

		if (isUserBoardMember(board.getId(), invitedUser.getId())) {
			throw new BoardMemberDuplicateException(ErrorMessage.BOARD_MEMBER_DUPLICATE);
		}

		BoardMember boardMember = BoardMember
			.builder()
			.board(board)
			.user(invitedUser)
			.build();

		BoardMember saveBoardMember = boardMemberRepository.save(boardMember);

		invitedUser.addBoard(board);

		return BoardMemberResponseDto
			.builder()
			.boardMember(saveBoardMember)
			.build();
	}

	@Transactional
	public BoardResponseDto editBoard(Long boardId, BoardEditRequestDto requestDto, User user) {

		if (isNullAndEmpty(requestDto.getTitle()) && isNullAndEmpty(requestDto.getContent())) {
			throw new ResourceNotFoundException(ErrorMessage.RESOURCE_NOT_FOUND);
		}

		Board board = findBoardById(boardId);

		if (!isUserManager(user)) {
			throw new UnauthorizedException(ErrorMessage.UNAUTHORIZED_MANAGER);
		}

		if (!isNullAndEmpty(requestDto.getTitle())) {
			if (boardRepository.findByTitle(requestDto.getTitle()).isPresent()) {
				throw new BoardTitleDuplicateException(ErrorMessage.BOARD_TITLE_DUPLICATE);
			}
			board.updateTitle(requestDto.getTitle());
		}

		if (!isNullAndEmpty(requestDto.getContent())) {
			board.updateContent(requestDto.getContent());
		}

		return BoardResponseDto
			.builder()
			.board(board)
			.build();
	}

	public void deleteBoard(Long boardId, User user) {

		Board board = findBoardById(boardId);

		if (!isUserManager(user)) {
			throw new UnauthorizedException(ErrorMessage.UNAUTHORIZED_MANAGER);
		}

		boardRepository.delete(board);
		log.info(StatusMessage.DELETE_BOARD_SUCCESS.getMessage());
	}

	public Board findBoardById(Long boardId) {
		return boardRepository.findById(boardId).orElseThrow(
			() -> new EntityNotFoundException(ErrorMessage.NOT_FOUND_BOARD.getMessage())
		);
	}

	public boolean isUserManager(User user) {
		return user.getRole() == Role.MANAGER;
	}

	public boolean isUserBoardMember(Long boardId, Long userId) {
		return boardMemberRepository.existsByBoard_IdAndUser_Id(boardId, userId);
	}

	/**
	 * string 타입 데이터가 비어 있느지 확인하는 메서드
	 * @param string 문자열 타입의 입력 데이터
	 * @return 입력값이 비었으면 true, null이 아닌 값이 있다면 false
	 */
	private boolean isNullAndEmpty(String string) {
		return string == null || string.isEmpty();
	}
}
