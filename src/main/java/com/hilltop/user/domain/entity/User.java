package com.hilltop.user.domain.entity;

import com.hilltop.user.domain.request.UserRequestDto;
import com.hilltop.user.enumeration.UserType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.util.UUID;

/**
 * User entity
 */
@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Transient
    private static final String USER_ID_PREFIX = "uid-";
    @Transient
    private static final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Id
    private String id;
    private String name;
    private String mobileNo;
    private String password;
    @Enumerated(EnumType.STRING)
    private UserType userType;

    public User(UserRequestDto userRequestDto) {
        this.id = USER_ID_PREFIX + UUID.randomUUID();
        this.name = userRequestDto.getName();
        this.mobileNo = userRequestDto.getMobileNo();
        this.password = bCryptPasswordEncoder.encode(userRequestDto.getPassword());
        this.userType = userRequestDto.getUserType();
    }
}
