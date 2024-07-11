package com.sparta.bitbucket.column.entity;



import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

}
