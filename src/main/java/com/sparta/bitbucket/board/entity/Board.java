package com.sparta.bitbucket.board.entity;

import java.util.ArrayList;
import java.util.List;

import com.sparta.bitbucket.auth.entity.User;
import com.sparta.bitbucket.common.entity.Timestamped;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

@Getter
@Entity
@Table(name = "boards")
@NoArgsConstructor
public class Board extends Timestamped {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String title;

	@Column(nullable = false, length = 500)
	private String content;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@OneToMany(mappedBy = "board", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
	private List<BoardMember> boardMemberList = new ArrayList<>();

	@Builder
	public Board(String title, String content, User user) {
		this.title = title;
		this.content = content;
		this.user = user;
	}

	public void addBoardMember(BoardMember boardMember) {
		boardMemberList.add(boardMember);
	}

}
