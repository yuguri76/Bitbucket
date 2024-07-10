package com.sparta.bitbucket.column.service;

import org.springframework.stereotype.Service;

import com.sparta.bitbucket.column.repository.columnRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class columnService {

	private final columnRepository columnRepository;
}
