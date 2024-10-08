package com.sparta.bitbucket.column.entity;

import java.util.ArrayList;
import java.util.List;

import com.sparta.bitbucket.board.entity.Board;
import com.sparta.bitbucket.card.entity.Card;
import com.sparta.bitbucket.common.entity.Timestamped;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Columns extends Timestamped {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String title;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "board_id", nullable = false)
	private Board board;

	private int orders;

	@OneToMany(mappedBy = "columns", cascade = CascadeType.ALL, orphanRemoval = true)
	private final List<Card> cards = new ArrayList<>();

	@Builder(toBuilder = true)
	public Columns(String title, Board board, int orders) {
		this.title = title;
		this.board = board;
		this.orders = orders;
	}

	// setOrders 메소드 추가
	public void setOrders(int orders) {
		this.orders = orders;
	}

	public void addCard(Card card) {
		cards.add(card);
	}
}
