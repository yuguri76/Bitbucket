package com.sparta.bitbucket.comment.entity;

import com.sparta.bitbucket.auth.entity.User;
import com.sparta.bitbucket.card.entity.Card;
import com.sparta.bitbucket.common.entity.Timestamped;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 댓글 엔티티
 */
@Entity
@Getter
@Table(name = "comments")
@NoArgsConstructor
public class Comment extends Timestamped {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "card_id", nullable = false)
	private Card card;

	@Column(nullable = false)
	private String content;

	@Builder
	public Comment(User user, Card card, String content) {
		this.user = user;
		this.card = card;
		this.content = content;
	}
}