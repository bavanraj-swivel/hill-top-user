package com.hilltop.user.controller;

import com.hilltop.user.domain.request.UserRequestDto;
import com.hilltop.user.domain.response.ResponseWrapper;
import com.hilltop.user.enumeration.ErrorMessage;
import com.hilltop.user.enumeration.SuccessMessage;
import com.hilltop.user.exception.HillTopUserApplicationException;
import com.hilltop.user.exception.UserExistException;
import com.hilltop.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/api/v1/user")
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
}
