package com.sparta.bitbucket.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sparta.bitbucket.board.entity.Board;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
}
