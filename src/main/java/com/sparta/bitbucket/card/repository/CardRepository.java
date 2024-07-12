package com.sparta.bitbucket.card.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.bitbucket.card.entity.Card;

public interface CardRepository extends JpaRepository<Card, Long> {

	Optional<Card> findByIdAndCreateUser_Id(Long cardId, Long userId);

}
