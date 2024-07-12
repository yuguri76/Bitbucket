package com.sparta.bitbucket.column.entity;



import com.sparta.bitbucket.board.entity.Board;
import com.sparta.bitbucket.common.entity.Timestamped;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Builder(toBuilder = true)
@AllArgsConstructor
public class Columns extends Timestamped {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String title;

	@ManyToOne( fetch = FetchType.LAZY )
	@JoinColumn(name = "board_id", nullable = false)
	private Board board;

	private int orders;

	@Builder
	public Columns(String title,Board board, int orders){
		this.title = title;
		this.board = board;
		this.orders = orders;
	}

}
