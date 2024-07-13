package com.sparta.bitbucket.comment.repository;

import com.sparta.bitbucket.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * 댓글 레포지토리
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {
	List<Comment> findByCardIdOrderByCreatedAtDesc(Long cardId);
	Optional<Comment> findByIdAndCardId(Long id, Long cardId);
}