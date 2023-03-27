package com.hilltop.user.controller;

import com.hilltop.user.domain.request.LoginRequestDto;
import com.hilltop.user.domain.request.UserRequestDto;
import com.hilltop.user.enumeration.ErrorMessage;
import com.hilltop.user.enumeration.SuccessMessage;
import com.hilltop.user.enumeration.UserType;
import com.hilltop.user.exception.HillTopUserApplicationException;
import com.hilltop.user.exception.TokenException;
import com.hilltop.user.exception.UserExistException;
import com.hilltop.user.service.UserService;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * User controller test
 * Unit tests for {@link  UserController}
 */
class UserControllerTest {

    private static final String MOBILE_NO = "0779090909";
    private static final String PASSWORD = "password";
    private static final String FAILED = "Failed.";
    private final String REGISTER_URI = "/api/v1/user";
    private final String LOGIN_URI = "/api/v1/user/login";
    private final String VALIDATE_TOKEN_URI = "/api/v1/user/validate-token?token=123";
    private final UserRequestDto userRequestDto = getUserRequestDto();
    private final LoginRequestDto loginRequestDto = getLoginRequestDto();
    UserController userController;
    @Mock
    private UserService userService;
    @Mock
    private AuthenticationManager authenticationManager;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        openMocks(this);
        userController = new UserController(userService, authenticationManager);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    /**
     * Unit tests for registerUser() method.
     */
    @Test
    void Should_ReturnOk_When_RegisterUserIsSuccessful() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(REGISTER_URI)
                        .content(userRequestDto.toLogJson())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value(SuccessMessage.SUCCESSFULLY_ADDED.getMessage()));
    }

    @Test
    void Should_ReturnBadRequest_When_MissingRequiredFields() throws Exception {
        UserRequestDto requestDto = userRequestDto;
        requestDto.setPassword(null);
        mockMvc.perform(MockMvcRequestBuilders.post(REGISTER_URI)
                        .content(requestDto.toLogJson())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ErrorMessage.MISSING_REQUIRED_FIELDS.getMessage()))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void Should_ReturnBadRequest_When_MobileNoDoesntMatchThePattern() throws Exception {
        UserRequestDto requestDto = userRequestDto;
        requestDto.setMobileNo("077123*");
        mockMvc.perform(MockMvcRequestBuilders.post(REGISTER_URI)
                        .content(requestDto.toLogJson())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ErrorMessage.INVALID_MOBILE_NO.getMessage()))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void Should_ReturnUserExistError_When_UserTryToRegisterWithExistingNo() throws Exception {
        doThrow(new UserExistException(FAILED)).when(userService).addUser(any());
        mockMvc.perform(MockMvcRequestBuilders.post(REGISTER_URI)
                        .content(userRequestDto.toLogJson())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ErrorMessage.MOBILE_NO_EXIST.getMessage()))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void Should_ReturnInternalServerError_When_RegisterUserIsFailedDueToInternalErrors() throws Exception {
        doThrow(new HillTopUserApplicationException(FAILED)).when(userService).addUser(any());
        mockMvc.perform(MockMvcRequestBuilders.post(REGISTER_URI)
                        .content(userRequestDto.toLogJson())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value(ErrorMessage.INTERNAL_SERVER_ERROR.getMessage()))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    /**
     * Unit tests for login() method.
     */
    @Test
    void Should_ReturnOk_When_LoginIsSuccessful() throws Exception {
        when(userService.generateToken(anyString())).thenReturn("token123");
        mockMvc.perform(MockMvcRequestBuilders.post(LOGIN_URI)
                        .content(loginRequestDto.toLogJson())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(SuccessMessage.SUCCESSFULLY_LOGGED_IN.getMessage()));
    }

    @Test
    void Should_ReturnBadRequest_When_CredentialFieldsAreMissing() throws Exception {
        LoginRequestDto requestDto = loginRequestDto;
        requestDto.setPassword(null);
        mockMvc.perform(MockMvcRequestBuilders.post(LOGIN_URI)
                        .content(requestDto.toLogJson())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ErrorMessage.MISSING_REQUIRED_FIELDS.getMessage()))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void Should_ReturnInternalServerError_When_LoginIsFailedDueToInternalErrors() throws Exception {
        doThrow(new HillTopUserApplicationException(FAILED)).when(userService).generateToken(anyString());
        mockMvc.perform(MockMvcRequestBuilders.post(LOGIN_URI)
                        .content(userRequestDto.toLogJson())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value(ErrorMessage.INTERNAL_SERVER_ERROR.getMessage()))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    /**
     * Unit tests for validateToken() method.
     */
    @Test
    void Should_ReturnOk_When_ValidateTokenSuccessful() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(VALIDATE_TOKEN_URI)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(SuccessMessage.VALID_TOKEN.getMessage()));
    }

    @Test
    void Should_ReturnUnauthorized_When_TokenIsNotValid() throws Exception {
        doThrow(new TokenException(FAILED, new JwtException("Token Expired")))
                .when(userService).validateToken(anyString());
        mockMvc.perform(MockMvcRequestBuilders.get(VALIDATE_TOKEN_URI)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value(ErrorMessage.INVALID_TOKEN.getMessage()));
    }

    @Test
    void Should_ReturnInternalServerError_When_ValidateTokenIsFailedDueToInternalErrors() throws Exception {
        doThrow(new HillTopUserApplicationException(FAILED)).when(userService).validateToken(anyString());
        mockMvc.perform(MockMvcRequestBuilders.get(VALIDATE_TOKEN_URI)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value(ErrorMessage.INTERNAL_SERVER_ERROR.getMessage()))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    /**
     * This method is used to mock userRequestDto.
     *
     * @return userRequestDto
     */
    private UserRequestDto getUserRequestDto() {
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setName("User");
        userRequestDto.setMobileNo(MOBILE_NO);
        userRequestDto.setPassword(PASSWORD);
        userRequestDto.setUserType(UserType.USER);
        return userRequestDto;
    }

    /**
     * This method is used to mock loginRequestDto.
     *
     * @return loginRequestDto
     */
    private LoginRequestDto getLoginRequestDto() {
        LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setMobileNo(MOBILE_NO);
        loginRequestDto.setPassword(PASSWORD);
        return loginRequestDto;
    }
}