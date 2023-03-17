package com.hilltop.user.service;

import com.hilltop.user.domain.entity.User;
import com.hilltop.user.domain.request.LoginRequestDto;
import com.hilltop.user.domain.request.UserRequestDto;
import com.hilltop.user.exception.HillTopUserApplicationException;
import com.hilltop.user.exception.InvalidLoginException;
import com.hilltop.user.exception.UserExistException;
import com.hilltop.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * User service
 */
@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * This method is used to add user.
     *
     * @param userRequestDto userRequestDto
     */
    public void addUser(UserRequestDto userRequestDto) {
        try {
            checkMobileNoExist(userRequestDto.getMobileNo());
            userRepository.save(new User(userRequestDto));
            log.debug("Successfully added user data.");
        } catch (DataAccessException e) {
            throw new HillTopUserApplicationException("Failed to save user info in database.", e);
        }
    }

    /**
     * This method is used to validate if a user already exist with same mobileNo.
     *
     * @param mobileNo mobileNo
     */
    public void checkMobileNoExist(String mobileNo) {
        try {
            Optional<User> optionalUser = userRepository.findByMobileNo(mobileNo);
            if (optionalUser.isPresent())
                throw new UserExistException("Mobile number already registered.");
        } catch (DataAccessException e) {
            throw new HillTopUserApplicationException("Failed to get user by mobileNo from database.", e);
        }
    }

    /**
     * This method is used to validate user login.
     *
     * @param loginRequestDto loginRequestDto
     * @return logged in user.
     */
    public User loginUser(LoginRequestDto loginRequestDto) {
        try {
            Optional<User> optionalUser = userRepository.findByMobileNo(loginRequestDto.getMobileNo());
            if (optionalUser.isEmpty() ||
                    !passwordEncoder.matches(loginRequestDto.getPassword(), optionalUser.get().getPassword()) ||
                    loginRequestDto.getUserType() != optionalUser.get().getUserType()) {
                throw new InvalidLoginException("Invalid credentials.");
            }
            return optionalUser.get();
        } catch (DataAccessException e) {
            throw new HillTopUserApplicationException("Failed to get user from database.", e);
        }
    }
}
