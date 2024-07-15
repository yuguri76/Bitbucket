package com.sparta.bitbucket.column;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.bitbucket.column.entity.Columns;

public interface ColumnRepository extends JpaRepository<Columns, Long> {

	List<Columns> findAllByBoardIdOrderByOrders(Long boardId);

	Optional<Columns> findByIdAndBoardId(Long id, Long boardId);

	Boolean existsByBoardIdAndTitle(Long boardId, String title);
}
