package com.mood.diary.service.user.repository;

import com.mood.diary.service.user.model.AuthUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthUserRepository extends MongoRepository<AuthUser, String> {
    Optional<AuthUser> findAuthUserByUsername(String username);
    Optional<AuthUser> findAuthUserByEmail(String email);
}
