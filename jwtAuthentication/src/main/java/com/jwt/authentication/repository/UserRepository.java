package com.jwt.authentication.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jwt.authentication.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	
	User findByUserName(String userName);
	
}
