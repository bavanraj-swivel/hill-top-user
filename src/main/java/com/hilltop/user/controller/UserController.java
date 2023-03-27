package com.hilltop.user.controller;

import com.hilltop.user.domain.request.LoginRequestDto;
import com.hilltop.user.domain.request.UserRequestDto;
import com.hilltop.user.domain.response.LoginResponseDto;
import com.hilltop.user.domain.response.ResponseWrapper;
import com.hilltop.user.enumeration.ErrorMessage;
import com.hilltop.user.enumeration.SuccessMessage;
import com.hilltop.user.exception.HillTopUserApplicationException;
import com.hilltop.user.exception.TokenException;
import com.hilltop.user.exception.UserExistException;
import com.hilltop.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

/**
 * User controller
 */
@RestController
@Slf4j
@RequestMapping("/api/v1/user")
public class UserController extends BaseController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    public UserController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
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
                return getBadRequestErrorResponse(ErrorMessage.MISSING_REQUIRED_FIELDS, HttpStatus.BAD_REQUEST);
            }
            if (!userRequestDto.isValidMobileNo())
                return getBadRequestErrorResponse(ErrorMessage.INVALID_MOBILE_NO, HttpStatus.BAD_REQUEST);
            userService.addUser(userRequestDto);
            return getSuccessResponse(SuccessMessage.SUCCESSFULLY_ADDED, null, HttpStatus.CREATED);
        } catch (UserExistException e) {
            log.debug("User already exist for mobileNo: {}.", userRequestDto.getMobileNo(), e);
            return getBadRequestErrorResponse(ErrorMessage.MOBILE_NO_EXIST, HttpStatus.BAD_REQUEST);
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
                return getBadRequestErrorResponse(ErrorMessage.MISSING_REQUIRED_FIELDS, HttpStatus.BAD_REQUEST);
            }
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginRequestDto.getMobileNo(), loginRequestDto.getPassword()));
            String token = userService.generateToken(loginRequestDto.getMobileNo());
            return getSuccessResponse(SuccessMessage.SUCCESSFULLY_LOGGED_IN, new LoginResponseDto(token), HttpStatus.OK);
        } catch (HillTopUserApplicationException e) {
            log.error("Failed to log in.", e);
            return getInternalServerError();
        }
    }

    /**
     * This method is used to validate token.
     *
     * @param token token
     * @return success/ error response.
     */
    @GetMapping("/validate-token")
    public ResponseEntity<ResponseWrapper> validateToken(@RequestParam String token) {
        try {
            userService.validateToken(token);
            return getSuccessResponse(SuccessMessage.VALID_TOKEN, null, HttpStatus.OK);
        } catch (TokenException e) {
            log.error("Invalid token: {}", token);
            return getBadRequestErrorResponse(ErrorMessage.INVALID_TOKEN, HttpStatus.UNAUTHORIZED);
        } catch (HillTopUserApplicationException e) {
            log.error("Failed to validate token.", e);
            return getInternalServerError();
        }
    }
}
