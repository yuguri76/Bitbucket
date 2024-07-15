package com.sparta.bitbucket.board;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sparta.bitbucket.board.entity.BoardMember;

@Repository
public interface BoardMemberRepository extends JpaRepository<BoardMember, Long> {
	Optional<BoardMember> findAllByBoard_IdAndUser_Id(Long boardId, Long userId);

	boolean existsByBoard_IdAndUser_Id(Long boardId, Long userId);
}
