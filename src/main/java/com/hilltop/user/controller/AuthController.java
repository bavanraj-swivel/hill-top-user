package com.hilltop.user.controller;

import com.hilltop.user.domain.request.LoginRequestDto;
import com.hilltop.user.domain.response.LoginResponseDto;
import com.hilltop.user.domain.response.ResponseWrapper;
import com.hilltop.user.enumeration.ErrorMessage;
import com.hilltop.user.enumeration.SuccessMessage;
import com.hilltop.user.exception.HillTopUserApplicationException;
import com.hilltop.user.exception.TokenException;
import com.hilltop.user.service.JwtTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

/**
 * Auth controller
 */
@RestController
@RequestMapping("/api/v1/auth")
@Slf4j
public class AuthController extends BaseController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;

    public AuthController(AuthenticationManager authenticationManager, JwtTokenService jwtTokenService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenService = jwtTokenService;
    }

    /**
     * This method is used to log in user.
     *
     * @param loginRequestDto loginRequestDto
     * @return success/ error response.
     */
    @PostMapping("/sign-in")
    public ResponseEntity<ResponseWrapper> loginUser(@RequestBody LoginRequestDto loginRequestDto) {
        try {
            if (!loginRequestDto.isRequiredFieldsAvailable()) {
                log.debug("Required fields missing. data: {}", loginRequestDto.toLogJson());
                return getBadRequestErrorResponse(ErrorMessage.MISSING_REQUIRED_FIELDS, HttpStatus.BAD_REQUEST);
            }
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginRequestDto.getMobileNo(), loginRequestDto.getPassword()));
            String token = jwtTokenService.generateToken(loginRequestDto.getMobileNo());
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
            jwtTokenService.validateToken(token);
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
