package com.sparta.bitbucket.comment;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.bitbucket.comment.entity.Comment;

/**
 * 댓글 레포지토리
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {
	List<Comment> findByCardIdOrderByCreatedAtDesc(Long cardId);

	Optional<Comment> findByIdAndCardId(Long id, Long cardId);
}