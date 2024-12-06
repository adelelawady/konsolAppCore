package com.konsol.core.web.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.konsol.core.security.jwt.JWTFilter;
import com.konsol.core.security.jwt.TokenProvider;
import com.konsol.core.service.mapper.UserMapper;
import com.konsol.core.web.api.AuthenticateApiDelegate;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

/**
 * Controller to authenticate users.
 */
//@RestController
//@RequestMapping("/api")
@Service
public class UserJWTController implements AuthenticateApiDelegate {

    private final TokenProvider tokenProvider;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final Logger log = LoggerFactory.getLogger(UserJWTController.class);

    private final HttpServletRequest request;

    private final UserMapper userMapper;

    public UserJWTController(
        TokenProvider tokenProvider,
        AuthenticationManagerBuilder authenticationManagerBuilder,
        HttpServletRequest request,
        UserMapper userMapper
    ) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.request = request;
        this.userMapper = userMapper;
    }

    @Override
    public ResponseEntity<com.konsol.core.service.api.dto.JWTToken> authorize(
        @Valid @RequestBody com.konsol.core.service.api.dto.LoginVM loginVM
    ) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            loginVM.getUsername(),
            loginVM.getPassword()
        );

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.createToken(authentication, loginVM.getRememberMe());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        com.konsol.core.service.api.dto.JWTToken jwtToken = new com.konsol.core.service.api.dto.JWTToken();
        jwtToken.setIdToken(jwt);
        return new ResponseEntity<>(jwtToken, httpHeaders, HttpStatus.OK);
    }

    /**
     * {@code GET  /authenticate} : check if the user is authenticated, and return its login.
     *
     * @return the login if the user is authenticated.
     */
    @Override
    // @GetMapping("/authenticate")
    public ResponseEntity<String> isAuthenticated() {
        log.debug("REST request to check if the current user is authenticated");
        return ResponseEntity.ok(request.getRemoteUser());
    }

    /**
     * Object to return as body in JWT Authentication.
     */
    static class JWTToken {

        private String idToken;

        JWTToken(String idToken) {
            this.idToken = idToken;
        }

        @JsonProperty("id_token")
        String getIdToken() {
            return idToken;
        }

        void setIdToken(String idToken) {
            this.idToken = idToken;
        }
    }
}
