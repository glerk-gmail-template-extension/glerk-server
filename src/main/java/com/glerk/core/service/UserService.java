package com.glerk.core.service;

import com.glerk.core.entity.User;
import com.glerk.core.dto.UserDto;
import com.glerk.core.config.security.JWTUtils;
import com.glerk.core.exception.ErrorCode;
import com.glerk.core.exception.BusinessException;
import com.glerk.core.repository.UserRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    private final JWTUtils jwtUtils;

    private final GoogleIdTokenVerifier verifier;

    public UserService(@Value("${app.client-id}") String clientId, UserRepository userRepository, JWTUtils jwtUtils) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
        NetHttpTransport transport = new NetHttpTransport();

        JsonFactory jsonFactory = new GsonFactory();
        verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singleton(clientId))
                .build();
    }

    @Transactional(readOnly = true)
    public User findUser(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.INVALID_TOKEN));
    }

    public String loginOAuthGoogle(String idToken) {
        UserDto googleInfoDto = verifyIDToken(idToken);

        if (googleInfoDto == null) {
            throw new BusinessException(ErrorCode.GOOGLE_USER_NOT_FOUND);
        }

        User user = findUserByEmail(googleInfoDto.getEmail()).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        return jwtUtils.createToken(user);
    }

    public String signupOAuthGoogle(String idToken) {
        UserDto googleInfoDto = verifyIDToken(idToken);

        if (googleInfoDto == null) {
            throw new BusinessException(ErrorCode.GOOGLE_USER_NOT_FOUND);
        }

        Optional<User> optionalUser = this.findUserByEmail(googleInfoDto.getEmail());

        if (optionalUser.isPresent()) {
            throw new BusinessException(ErrorCode.USER_ALREADY_EXIST);
        }

        User user = createUser(googleInfoDto);
        return jwtUtils.createToken(user);
    }

    private User createUser(UserDto googleInfoDto) {
        User user = new User();
        user.setEmail(googleInfoDto.getEmail());
        user.setUsername(googleInfoDto.getUsername());
        user.setProfileUrl(googleInfoDto.getProfileUrl());
        user.setRoles("ROLE_USER");

        userRepository.save(user);
        return user;
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findUserByUserId(Long userId) {
        return userRepository.findById(userId);
    }

    private UserDto verifyIDToken(String idToken) {
        try {
            GoogleIdToken idTokenObj = verifier.verify(idToken);

            if (idTokenObj == null) return null;

            GoogleIdToken.Payload payload = idTokenObj.getPayload();
            String firstName = (String) payload.get("given_name");
            String lastName = (String) payload.get("family_name");
            String username = firstName + (lastName != null ? " " + lastName : "");
            String email = payload.getEmail();
            String pictureUrl = (String) payload.get("picture");

            return new UserDto(email, username, pictureUrl);
        } catch (GeneralSecurityException | IOException e) {
            return null;
        }
    }
}
