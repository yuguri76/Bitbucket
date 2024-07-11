package com.sparta.bitbucket.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sparta.bitbucket.board.entity.BoardMember;

@Repository
public interface BoardMemberRepository extends JpaRepository<BoardMember, Long> {
}
