package com.sparta.bitbucket.board;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sparta.bitbucket.auth.entity.User;
import com.sparta.bitbucket.board.entity.BoardMember;

@Repository
public interface BoardMemberRepository extends JpaRepository<BoardMember, Long> {

	List<BoardMember> findByUser(User user);

	boolean existsByBoard_IdAndUser_Id(Long boardId, Long userId);
}
