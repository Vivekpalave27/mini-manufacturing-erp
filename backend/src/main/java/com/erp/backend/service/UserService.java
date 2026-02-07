package com.erp.backend.service;
import com.erp.backend.exception.ResourceNotFoundException;

import com.erp.backend.dto.UserRequestDTO;
import com.erp.backend.dto.UserResponseDTO;
import com.erp.backend.entity.Role;
import com.erp.backend.entity.User;
import com.erp.backend.repository.RoleRepository;
import com.erp.backend.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public UserResponseDTO createUser(UserRequestDTO dto) {

        Role role = roleRepository.findById(dto.getRoleId())
        		.orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + dto.getRoleId()));


        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(role);
        user.setStatus(dto.getStatus());

        User savedUser = userRepository.save(user);
        return mapToResponse(savedUser);
    }

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private UserResponseDTO mapToResponse(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole().getRoleName());
        dto.setStatus(user.getStatus());
        return dto;
    }
}
