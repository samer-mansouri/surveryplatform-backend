package com.core.surveyplatform.service;

import com.core.surveyplatform.entity.User;
import com.core.surveyplatform.entity.enums.UserRole;
import com.core.surveyplatform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findById(UUID id) {
        return userRepository.findById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteById(UUID id) {
        userRepository.deleteById(id);
    }

    public Optional<User> updateUserRole(UUID id, UserRole role) {
        return userRepository.findById(id).map(user -> {
            user.setRole(role);
            return userRepository.save(user);
        });
    }
}
