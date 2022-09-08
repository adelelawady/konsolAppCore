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
import com.konsol.core.web.rest.errors.*;
import com.konsol.core.web.rest.vm.KeyAndPasswordVM;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing the current user's account.
 */
//@RestController
//@RequestMapping("/api")
@Service
public class AccountResource implements AccountApiDelegate {

    private static class AccountResourceException extends RuntimeException {

        private AccountResourceException(String message) {
            super(message);
        }
    }

    private final Logger log = LoggerFactory.getLogger(AccountResource.class);

    private final UserRepository userRepository;

    private final UserService userService;

    private final MailService mailService;

    private final HttpServletRequest request;

    private final UserMapper userMapper;

    private final UtilitsMapper utilitsMapper;

    public AccountResource(
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
     * {@code GET  /account} : get the current user.
     *
     * @return the current user.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user couldn't be returned.
     */
    @Override
    //@GetMapping("/account")
    public ResponseEntity<com.konsol.core.service.api.dto.AdminUserDTO> getAccount() {
        return ResponseEntity.ok(
            userService
                .getUserWithAuthorities()
                .map(userMapper::userToAdminUserDTO)
                .orElseThrow(() -> new AccountResourceException("User could not be found"))
        );
    }

    /**
     * {@code POST  /account} : update the current user information.
     *
     * @param userDTO the current user information.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is already used.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user login wasn't found.
     */
    //@PostMapping("/account")
    @Override
    public ResponseEntity<Void> saveAccount(@Valid @RequestBody AdminUserDTO userDTO) {
        String userLogin = SecurityUtils
            .getCurrentUserLogin()
            .orElseThrow(() -> new AccountResourceException("Current user login not found"));
        Optional<User> existingUser = userRepository.findOneByEmailIgnoreCase(userDTO.getEmail());
        if (existingUser.isPresent() && (!existingUser.get().getLogin().equalsIgnoreCase(userLogin))) {
            throw new EmailAlreadyUsedException();
        }
        Optional<User> user = userRepository.findOneByLogin(userLogin);
        if (!user.isPresent()) {
            throw new AccountResourceException("User could not be found");
        }
        userService.updateUser(
            userDTO.getFirstName(),
            userDTO.getLastName(),
            userDTO.getEmail(),
            userDTO.getLangKey(),
            userDTO.getImageUrl()
        );
        return ResponseEntity.ok().build();
    }

    /**
     * {@code POST  /account/change-password} : changes the current user's password.
     *
     * @param passwordChangeDto current and new password.
     * @throws InvalidPasswordException {@code 400 (Bad Request)} if the new password is incorrect.
     */
    //@PostMapping(path = "/account/change-password")
    @Override
    public ResponseEntity<Void> changePassword(@RequestBody PasswordChangeDTO passwordChangeDto) {
        if (isPasswordLengthInvalid(passwordChangeDto.getNewPassword())) {
            throw new InvalidPasswordException();
        }
        userService.changePassword(passwordChangeDto.getCurrentPassword(), passwordChangeDto.getNewPassword());
        return ResponseEntity.ok().build();
    }

    /**
     * {@code POST   /account/reset-password/init} : Send an email to reset the password of the user.
     *
     * @param mail the mail of the user.
     */
    // @PostMapping(path = "/account/reset-password/init")

    @Override
    public ResponseEntity<Void> requestPasswordReset(@RequestBody String mail) {
        Optional<User> user = userService.requestPasswordReset(mail);
        if (user.isPresent()) {
            mailService.sendPasswordResetMail(user.get());
        } else {
            // Pretend the request has been successful to prevent checking which emails really exist
            // but log that an invalid attempt has been made
            log.warn("Password reset requested for non existing mail");
        }
        return ResponseEntity.ok().build();
    }

    /**
     * {@code POST   /account/reset-password/finish} : Finish to reset the password of the user.
     *
     * @param keyAndPassword the generated key and the new password.
     * @throws InvalidPasswordException {@code 400 (Bad Request)} if the password is incorrect.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the password could not be reset.
     */
    //@PostMapping(path = "/account/reset-password/finish")
    @Override
    public ResponseEntity<Void> finishPasswordReset(@RequestBody com.konsol.core.service.api.dto.KeyAndPasswordVM keyAndPassword) {
        if (isPasswordLengthInvalid(keyAndPassword.getNewPassword())) {
            throw new InvalidPasswordException();
        }
        Optional<User> user = userService.completePasswordReset(keyAndPassword.getNewPassword(), keyAndPassword.getKey());

        if (!user.isPresent()) {
            throw new AccountResourceException("No user was found for this reset key");
        }
        return ResponseEntity.ok().build();
    }

    private static boolean isPasswordLengthInvalid(String password) {
        return (StringUtils.isEmpty(password) || password.length() < 4 || password.length() > 100);
    }
}
