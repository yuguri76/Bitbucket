package com.sparta.bitbucket.card.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.sparta.bitbucket.auth.entity.User;
import com.sparta.bitbucket.board.entity.Board;
import com.sparta.bitbucket.card.dto.CardEditRequestDto;
import com.sparta.bitbucket.column.entity.Columns;
import com.sparta.bitbucket.comment.entity.Comment;
import com.sparta.bitbucket.common.entity.Timestamped;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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

	private LocalDate dueDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "create_user_id", nullable = false)
	private User createUser;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "column_id", nullable = false)
	private Columns columns;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "board_id", nullable = false)
	private Board board;

	@OneToMany(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true)
	private final List<Comment> comments = new ArrayList<>();

	@Builder
	public Card(User createUser, Columns columns, Board board, String title, String status, String assignee,
		String content, LocalDate dueDate, Long orders) {
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

	public void updateColumn(Columns columns) {
		this.columns = columns;
		this.status = columns.getTitle();
	}

	public void updateOrders(Long orders) {
		this.orders = orders;
	}

	public void addComment(Comment comment) {
		this.comments.add(comment);
	}
}

