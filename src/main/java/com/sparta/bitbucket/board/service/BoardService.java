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
import com.sparta.bitbucket.auth.repository.UserRepository;
import com.sparta.bitbucket.board.dto.BoardCreateRequestDto;
import com.sparta.bitbucket.board.dto.BoardEditRequestDto;
import com.sparta.bitbucket.board.dto.BoardMemberResponseDto;
import com.sparta.bitbucket.board.dto.BoardResponseDto;
import com.sparta.bitbucket.board.dto.BoardWithMemberListResponseDto;
import com.sparta.bitbucket.board.entity.Board;
import com.sparta.bitbucket.board.entity.BoardMember;
import com.sparta.bitbucket.board.repository.BoardMemberRepository;
import com.sparta.bitbucket.board.repository.BoardRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {

	private final static int PAGE_SIZE = 5;

	private final BoardRepository boardRepository;
	private final BoardMemberRepository boardMemberRepository;
	private final UserRepository userRepository;

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
			throw new IllegalArgumentException("조회된 보드가 없습니다.");
		}

		return boardResponseDtoList;
	}

	public BoardWithMemberListResponseDto getBoard(Long boardId, User user) {

		Board board = findBoardById(boardId);

		if (!isUserBoardMember(board.getId(), user.getId())) {
			throw new IllegalArgumentException("로그인한 사용자는 해당 보드의 멤버가 아닙니다.");
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

	public BoardResponseDto createBoard(BoardCreateRequestDto requestDto, User user) {

		if (!isUserManager(user)) {
			throw new IllegalArgumentException("로그인한 사용자는 매니저가 아닙니다.");
		}

		if (boardRepository.findByTitle(requestDto.getTitle()).isPresent()) {
			throw new IllegalArgumentException("이미 생성된 보드입니다.");
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

		return BoardResponseDto
			.builder()
			.board(saveBoard)
			.build();
	}

	public BoardMemberResponseDto inviteBoard(Long boardId, String invitedUserEmail, User user) {

		Board board = findBoardById(boardId);

		if (!isUserManager(user)) {
			throw new IllegalArgumentException("로그인한 사용자는 매니저가 아닙니다.");
		}

		if (!isUserBoardMember(board.getId(), user.getId())) {
			throw new IllegalArgumentException("로그인한 사용자는 해당 보드의 멤버가 아닙니다.");
		}

		User invitedUser = userRepository.findByEmail(invitedUserEmail).orElseThrow(
			() -> new IllegalArgumentException("초대한 사용자가 존재하지 않습니다.")
		);

		if (isUserBoardMember(board.getId(), invitedUser.getId())) {
			throw new IllegalArgumentException("초대된 사용자는 이미 해당 보드의 멤버입니다.");
		}

		BoardMember boardMember = BoardMember
			.builder()
			.board(board)
			.user(invitedUser)
			.build();

		BoardMember saveBoardMember = boardMemberRepository.save(boardMember);

		return BoardMemberResponseDto
			.builder()
			.boardMember(saveBoardMember)
			.build();
	}

	@Transactional
	public BoardResponseDto editBoard(Long boardId, BoardEditRequestDto requestDto, User user) {

		if (isNullAndEmpty(requestDto.getTitle()) && isNullAndEmpty(requestDto.getContent())) {
			throw new IllegalArgumentException("수정으로 요청된 값이 없습니다.");
		}

		Board board = findBoardById(boardId);

		if (!isUserManager(user)) {
			throw new IllegalArgumentException("로그인한 사용자는 매니저가 아닙니다.");
		}

		if (!isNullAndEmpty(requestDto.getTitle())) {
			if (boardRepository.findByTitle(requestDto.getTitle()).isPresent()) {
				throw new IllegalArgumentException("수정하려는 보드 제목과 중복되는 제목이 있습니다.");
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
			throw new IllegalArgumentException("로그인한 사용자는 매니저가 아닙니다.");
		}

		boardRepository.delete(board);
	}

	private Board findBoardById(Long boardId) {
		return boardRepository.findById(boardId).orElseThrow(
			() -> new IllegalArgumentException("해당 id로 조회된 보드가 없습니다.")
		);
	}

	private boolean isUserManager(User user) {
		return user.getRole() == Role.MANAGER;
	}

	private boolean isUserBoardMember(Long boardId, Long userId) {
		return boardMemberRepository.findAllByBoard_IdAndUser_Id(boardId, userId).isPresent();
	}

	/**
	 * string 타입 데이터가 비어 있느지 확인하는 메서드
	 * @param string
	 * @return 입력값이 비었으면 true, null이 아닌 값이 있다면 false
	 */
	private boolean isNullAndEmpty(String string) {
		return string == null || string.isEmpty();
	}
}
