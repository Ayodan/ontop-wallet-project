package com.ontop.wallet.service.impl;


import com.ontop.wallet.dao.UserRepository;
import com.ontop.wallet.exception.NotFoundException;
import com.ontop.wallet.model.Users;
import com.ontop.wallet.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Users getUserById(Long userId) {
        Optional<Users> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()) {
            throw new NotFoundException("INVALID_USER", "user not found");
        }
        Users users = optionalUser.get();
        return users;
    }
}
