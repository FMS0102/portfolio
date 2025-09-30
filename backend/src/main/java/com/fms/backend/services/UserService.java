package com.fms.backend.services;

import com.fms.backend.dto.UserDTO;
import com.fms.backend.mappers.UserMapper;
import com.fms.backend.repositories.auth.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final Logger logger = LoggerFactory.getLogger(UserService.class.getName());

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public List<UserDTO> findAll() {
        logger.info("UserService: FindAll users.");
        return userMapper.toDTOList(userRepository.findAll());
    }
}
