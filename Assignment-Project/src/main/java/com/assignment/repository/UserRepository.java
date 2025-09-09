package com.assignment.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.assignment.model.User;

public interface UserRepository extends JpaRepository<User, Long> 
{
	Optional<User> findByEmailAddress(String emailAddress);
}
