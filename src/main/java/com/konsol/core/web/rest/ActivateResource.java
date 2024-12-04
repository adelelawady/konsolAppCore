package com.konsol.core.web.rest;

import com.konsol.core.domain.User;
import com.konsol.core.repository.UserRepository;
import com.konsol.core.security.SecurityUtils;
import com.konsol.core.service.MailService;
import com.konsol.core.service.UserService;
import com.konsol.core.service.api.dto.AdminUserDTO;
import com.konsol.core.service.api.dto.PasswordChangeDTO;
import com.konsol.core.service.mapper.UserMapper;
import com.konsol.core.service.mapper.UtilitsMapper;
import com.konsol.core.web.api.AccountApiDelegate;
import com.konsol.core.web.api.ActivateApiDelegate;
import com.konsol.core.web.rest.errors.EmailAlreadyUsedException;
import com.konsol.core.web.rest.errors.InvalidPasswordException;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * REST controller for managing the current user's account.
 */
//@RestController
//@RequestMapping("/api")
@Service
public class ActivateResource implements ActivateApiDelegate {

    private static class AccountResourceException extends RuntimeException {

        private AccountResourceException(String message) {
            super(message);
        }
    }

    private final Logger log = LoggerFactory.getLogger(ActivateResource.class);

    private final UserRepository userRepository;

    private final UserService userService;

    private final MailService mailService;

    private final HttpServletRequest request;

    private final UserMapper userMapper;

    private final UtilitsMapper utilitsMapper;

    public ActivateResource(
        UserRepository userRepository,
        UserService userService,
        MailService mailService,
        HttpServletRequest request,
        UserMapper userMapper,
        UtilitsMapper utilitsMapper
    ) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.mailService = mailService;
        this.request = request;
        this.userMapper = userMapper;
        this.utilitsMapper = utilitsMapper;
    }

    /**
     * {@code GET  /activate} : activate the registered user.
     *
     * @param key the activation key.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user couldn't be activated.
     */
    @Override
    public ResponseEntity<Void> activateAccount(@RequestParam(value = "key") String key) {
        Optional<User> user = userService.activateRegistration(key);
        if (!user.isPresent()) {
            throw new AccountResourceException("No user was found for this activation key");
        }
        return ResponseEntity.ok().build();
    }

    private static boolean isPasswordLengthInvalid(String password) {
        return (StringUtils.isEmpty(password) || password.length() < 4 || password.length() > 100);
    }
}
