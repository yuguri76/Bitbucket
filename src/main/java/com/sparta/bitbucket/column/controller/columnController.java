package com.sparta.bitbucket.column.controller;

import org.springframework.web.bind.annotation.RestController;

import com.sparta.bitbucket.column.service.columnService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class columnController {

	private final com.sparta.bitbucket.column.service.columnService columnService;
}
