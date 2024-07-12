package com.sparta.bitbucket.card.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.bitbucket.auth.entity.User;
import com.sparta.bitbucket.board.entity.Board;
import com.sparta.bitbucket.card.dto.CardEditRequestDto;
import com.sparta.bitbucket.card.dto.CardMoveRequestDto;
import com.sparta.bitbucket.card.dto.CardRequestDto;
import com.sparta.bitbucket.column.entity.Columns;
import com.sparta.bitbucket.common.entity.Timestamped;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "cards")
@NoArgsConstructor
public class Card extends Timestamped {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 30)
	private String assignee;

	@Column(nullable = false)
	private String title;

	private String content;
	private Long orders;
	private String status;

	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDateTime dueDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="create_user_id", nullable = false)
	private User createUser;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "column_id", nullable = false)
	private Columns columns;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "board_id", nullable = false)
	private Board board;

	@Builder
	public Card(User createUser, Columns columns, Board board, String title, String status, String assignee, String content, LocalDateTime dueDate, Long orders) {
		this.createUser = createUser;
		this.columns = columns;
		this.board = board;
		this.title = title;
		this.status = status;
		this.assignee = assignee;
		this.content = content;
		this.dueDate = dueDate;
		this.orders = orders;
	}

	public void updateCard(CardEditRequestDto requestDto) {
		this.title = requestDto.getTitle();
		this.assignee = requestDto.getAssignee();
		this.content = requestDto.getContent();
		this.dueDate = requestDto.getDueDate();
	}

	public void updateOrders(CardMoveRequestDto requestDto) {
		this.orders = requestDto.getOrders();
	}
}

