package com.sparta.bitbucket.card.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.bitbucket.card.service.CardService;


import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CardController {
	private final CardService cardService;
}

