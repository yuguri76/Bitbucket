package com.sparta.bitbucket.column.entity;



import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Columns {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String title;

	// @ManyToOne( fetch = FetchType.LAZY, cascade = CascadeType.REMOVE )
	// private Board board;

	private int orders;

	@Builder
	public Columns(String title,Board board, int orders){
		this.title = title;
		this.board = board;
		this.orders = orders;
	}
}
