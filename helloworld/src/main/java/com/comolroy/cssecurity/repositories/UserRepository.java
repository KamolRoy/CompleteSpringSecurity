package com.comolroy.cssecurity.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.comolroy.cssecurity.entities.User;

// First parameter is the class and second is type of primary key
public interface UserRepository extends JpaRepository<User, Long>{

	User findByEmail(String email);

	
}
