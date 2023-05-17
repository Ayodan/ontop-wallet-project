package com.ontop.wallet.service.impl;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ontop.wallet.dao.UserRepository;
import com.ontop.wallet.exception.NotFoundException;
import com.ontop.wallet.model.Users;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {UserServiceImpl.class})
@ExtendWith(SpringExtension.class)
class UserServiceImplTest {
    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserServiceImpl userServiceImpl;


    @Test
    void testGetUserById() {
        Users users = new Users();
        users.setFirstName("Jane");
        users.setId(1L);
        users.setSurname("Doe");
        Optional<Users> ofResult = Optional.of(users);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        assertSame(users, userServiceImpl.getUserById(1L));
        verify(userRepository).findById(Mockito.<Long>any());
    }


    @Test
    void testGetUserByThrowsNotFoundException() {
        when(userRepository.findById(Mockito.<Long>any()))
                .thenThrow(new NotFoundException("INVALID_USER", "An error occurred"));
        assertThrows(NotFoundException.class, () -> userServiceImpl.getUserById(1L));
        verify(userRepository).findById(Mockito.<Long>any());
    }
}

