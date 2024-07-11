package com.sparta.bitbucket.column.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.bitbucket.column.entity.Columns;

public interface ColumnRepository extends JpaRepository<Columns, Long> {

	Boolean existsByBoardIdAndTitle(Long boardId, String title);
}
