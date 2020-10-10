package com.vivek.discussion.repository;

import com.vivek.discussion.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
}
