package com.bgrfacile.bgrsignapi.service;

import com.bgrfacile.bgrsignapi.dto.UserProfileDTO;
import com.bgrfacile.bgrsignapi.exception.ResourceNotFoundException;
import com.bgrfacile.bgrsignapi.model.*;
import com.bgrfacile.bgrsignapi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserProfileDTO getUserProfile(User user) {
        UserProfileDTO dto = new UserProfileDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());

        if (user.getAdmin() != null) {
            dto.setFirstName(user.getAdmin().getFirstName());
            dto.setLastName(user.getAdmin().getLastName());
            dto.setPhoneNumber(user.getAdmin().getPhoneNumber());
        } else if (user.getTeacher() != null) {
            dto.setFirstName(user.getTeacher().getFirstName());
            dto.setLastName(user.getTeacher().getLastName());
            dto.setHireDate(user.getTeacher().getHireDate());
            dto.setSpecialization(user.getTeacher().getSpecialization());
        } else if (user.getStudent() != null) {
            dto.setFirstName(user.getStudent().getFirstName());
            dto.setLastName(user.getStudent().getLastName());
            dto.setDateOfBirth(user.getStudent().getDateOfBirth());
        } else if (user.getParent() != null) {
            dto.setFirstName(user.getParent().getFirstName());
            dto.setLastName(user.getParent().getLastName());
            dto.setPhoneNumber(user.getParent().getPhoneNumber());
        }

        dto.setRoles(user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()));
        dto.setPermissions(user.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(Permission::getName)
                .collect(Collectors.toSet()));

        return dto;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setEmail(userDetails.getEmail());
        user.setPassword(userDetails.getPassword());
        user.setEnabled(userDetails.isEnabled());
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
