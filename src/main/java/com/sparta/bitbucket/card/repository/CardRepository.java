package com.sparta.bitbucket.card.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.bitbucket.card.entity.Card;

public interface CardRepository extends JpaRepository<Card, Long> {

}
