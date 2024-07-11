package com.sparta.bitbucket.column.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.bitbucket.column.entity.Columns;

public interface ColumnRepository extends JpaRepository<Columns, Long> {

	List<Columns> findAllByBoardIdOrderByOrders(Long boardId);
}
