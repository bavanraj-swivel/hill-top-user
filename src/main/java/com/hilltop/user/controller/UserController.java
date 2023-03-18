package com.hilltop.user.controller;

import com.hilltop.user.domain.entity.User;
import com.hilltop.user.domain.request.LoginRequestDto;
import com.hilltop.user.domain.request.UserRequestDto;
import com.hilltop.user.domain.response.LoginResponseDto;
import com.hilltop.user.domain.response.ResponseWrapper;
import com.hilltop.user.enumeration.ErrorMessage;
import com.hilltop.user.enumeration.SuccessMessage;
import com.hilltop.user.exception.HillTopUserApplicationException;
import com.hilltop.user.exception.InvalidLoginException;
import com.hilltop.user.exception.UserExistException;
import com.hilltop.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * User controller
 */
@RestController
@Slf4j
@RequestMapping("/api/user")
public class UserController extends BaseController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * This method is used to register user.
     *
     * @param userRequestDto userRequestDto
     * @return success/ error response.
     */
    @PostMapping("")
    public ResponseEntity<ResponseWrapper> registerUser(@RequestBody UserRequestDto userRequestDto) {
        try {
            if (!userRequestDto.isRequiredFieldsAvailable()) {
                log.debug("Required fields missing. data: {}", userRequestDto.toLogJson());
                return getBadRequestErrorResponse(ErrorMessage.MISSING_REQUIRED_FIELDS);
            }
            if (!userRequestDto.isValidMobileNo())
                return getBadRequestErrorResponse(ErrorMessage.INVALID_MOBILE_NO);
            userService.addUser(userRequestDto);
            return getSuccessResponse(SuccessMessage.SUCCESSFULLY_ADDED, null);
        } catch (UserExistException e) {
            log.debug("User already exist for mobileNo: {}.", userRequestDto.getMobileNo(), e);
            return getBadRequestErrorResponse(ErrorMessage.MOBILE_NO_EXIST);
        } catch (HillTopUserApplicationException e) {
            log.error("Failed to add user. ", e);
            return getInternalServerError();
        }
    }

    /**
     * This method is used to log in user.
     *
     * @param loginRequestDto loginRequestDto
     * @return success/ error response.
     */
    @PostMapping("/login")
    public ResponseEntity<ResponseWrapper> loginUser(@RequestBody LoginRequestDto loginRequestDto) {
        try {
            if (!loginRequestDto.isRequiredFieldsAvailable()) {
                log.debug("Required fields missing. data: {}", loginRequestDto.toLogJson());
                return getBadRequestErrorResponse(ErrorMessage.MISSING_REQUIRED_FIELDS);
            }
            User user = userService.loginUser(loginRequestDto);
            return getSuccessResponse(SuccessMessage.SUCCESSFULLY_LOGGED_IN, new LoginResponseDto(user));
        } catch (InvalidLoginException e) {
            log.error("User credentials didn't match.", e);
            return getBadRequestErrorResponse(ErrorMessage.INVALID_LOGIN);
        } catch (HillTopUserApplicationException e) {
            log.error("Failed to log in.", e);
            return getInternalServerError();
        }
    }
}
