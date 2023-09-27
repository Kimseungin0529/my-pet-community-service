package com.project.pet.user.repositoy;

import com.project.pet.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    public User findByLoginId(String loginId);
}
