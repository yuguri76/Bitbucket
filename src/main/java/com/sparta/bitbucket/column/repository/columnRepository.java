package com.sparta.bitbucket.column.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.bitbucket.column.entity.columns;

public interface columnRepository extends JpaRepository<columns, Long> {
}
