package me.noticeapplication.persistence.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import me.noticeapplication.persistence.user.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
	Optional<UserEntity> findByUsername(String username);
}
