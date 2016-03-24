package com.comolroy.helloworld.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.comolroy.helloworld.entities.User;

// First parameter is the class and second is type of primary key
public interface UserRepository extends JpaRepository<User, Long>{

	User findByEmail(String email);

	
}
