package com.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.auth.entities.UserEntity;


public interface IUserRepository extends JpaRepository<UserEntity, Integer> {
	UserEntity findByEmail(String email);
}
