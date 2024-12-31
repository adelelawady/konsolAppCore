package com.konsol.core.service;

import com.konsol.core.config.Constants;
import com.konsol.core.domain.Authority;
import com.konsol.core.domain.User;
import com.konsol.core.repository.AuthorityRepository;
import com.konsol.core.repository.UserRepository;
import com.konsol.core.security.AuthoritiesConstants;
import com.konsol.core.security.SecurityUtils;
import com.konsol.core.service.api.dto.AdminUserDTO;
import com.konsol.core.service.api.dto.AuthorityDTO;
import com.konsol.core.service.api.dto.UserDTO;
import com.konsol.core.service.error.EmailAlreadyUsedException;
import com.konsol.core.service.error.InvalidPasswordException;
import com.konsol.core.service.error.UsernameAlreadyUsedException;
import com.konsol.core.service.mapper.UserMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tech.jhipster.security.RandomUtil;

/**
 * Service class for managing users.
 */
@Service
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthorityRepository authorityRepository;

    private final CacheManager cacheManager;

    private final UserMapper userMapper;

    public UserService(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        AuthorityRepository authorityRepository,
        CacheManager cacheManager,
        UserMapper userMapper
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
        this.cacheManager = cacheManager;
        this.userMapper = userMapper;
    }

    public Optional<User> activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);
        return userRepository
            .findOneByActivationKey(key)
            .map(user -> {
                // activate given user for the registration key.
                user.setActivated(true);
                user.setActivationKey(null);
                userRepository.save(user);
                this.clearUserCaches(user);
                log.debug("Activated user: {}", user);
                return user;
            });
    }

    public Optional<User> completePasswordReset(String newPassword, String key) {
        log.debug("Reset user password for reset key {}", key);
        return userRepository
            .findOneByResetKey(key)
            .filter(user -> user.getResetDate().isAfter(Instant.now().minus(1, ChronoUnit.DAYS)))
            .map(user -> {
                user.setPassword(passwordEncoder.encode(newPassword));
                user.setResetKey(null);
                user.setResetDate(null);
                userRepository.save(user);
                this.clearUserCaches(user);
                return user;
            });
    }

    public Optional<User> requestPasswordReset(String mail) {
        return userRepository
            .findOneByEmailIgnoreCase(mail)
            .filter(User::isActivated)
            .map(user -> {
                user.setResetKey(RandomUtil.generateResetKey());
                user.setResetDate(Instant.now());
                userRepository.save(user);
                this.clearUserCaches(user);
                return user;
            });
    }

    public User registerUser(AdminUserDTO userDTO, String password) {
        userRepository
            .findOneByLogin(userDTO.getLogin().toLowerCase())
            .ifPresent(existingUser -> {
                boolean removed = removeNonActivatedUser(existingUser);
                if (!removed) {
                    throw new UsernameAlreadyUsedException();
                }
            });
        userRepository
            .findOneByEmailIgnoreCase(userDTO.getEmail())
            .ifPresent(existingUser -> {
                boolean removed = removeNonActivatedUser(existingUser);
                if (!removed) {
                    throw new EmailAlreadyUsedException();
                }
            });
        User newUser = new User();
        String encryptedPassword = passwordEncoder.encode(password);
        newUser.setLogin(userDTO.getLogin().toLowerCase());
        // new user gets initially a generated password
        newUser.setPassword(encryptedPassword);
        newUser.setFirstName(userDTO.getFirstName());
        newUser.setLastName(userDTO.getLastName());
        if (userDTO.getEmail() != null) {
            newUser.setEmail(userDTO.getEmail().toLowerCase());
        }
        newUser.setImageUrl(userDTO.getImageUrl());
        newUser.setLangKey(userDTO.getLangKey());
        // new user is not active
        newUser.setActivated(false);
        // new user gets registration key
        newUser.setActivationKey(RandomUtil.generateActivationKey());
        Set<Authority> authorities = handleCreatingUserAuthorities(userDTO);
        newUser.setAuthorities(authorities);
        userRepository.save(newUser);
        this.clearUserCaches(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    private boolean removeNonActivatedUser(User existingUser) {
        if (existingUser.isActivated()) {
            return false;
        }
        userRepository.delete(existingUser);
        this.clearUserCaches(existingUser);
        return true;
    }

    public User createUser(AdminUserDTO userDTO) {
        User user = new User();
        user.setLogin(userDTO.getLogin().toLowerCase());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail().toLowerCase());
        }
        user.setImageUrl(userDTO.getImageUrl());
        if (userDTO.getLangKey() == null) {
            user.setLangKey(Constants.DEFAULT_LANGUAGE); // default language
        } else {
            user.setLangKey(userDTO.getLangKey());
        }
        String encryptedPassword = passwordEncoder.encode(RandomUtil.generatePassword());
        user.setPassword(encryptedPassword);
        user.setResetKey(RandomUtil.generateResetKey());
        user.setResetDate(Instant.now());
        user.setActivated(true);
        Set<Authority> authorities = handleCreatingUserAuthorities(userDTO);
        user.setAuthorities(authorities);
        userRepository.save(user);
        this.clearUserCaches(user);
        log.debug("Created Information for User: {}", user);
        return user;
    }

    /**
     * Update all information for a specific user, and return the modified user.
     *
     * @param userDTO user to update.
     * @return updated user.
     */
    public Optional<AdminUserDTO> updateUser(AdminUserDTO userDTO) {
        return Optional
            .of(userRepository.findById(userDTO.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(user -> {
                this.clearUserCaches(user);
                user.setLogin(userDTO.getLogin().toLowerCase());
                user.setFirstName(userDTO.getFirstName());
                user.setLastName(userDTO.getLastName());
                if (userDTO.getEmail() != null) {
                    user.setEmail(userDTO.getEmail().toLowerCase());
                }
                user.setImageUrl(userDTO.getImageUrl());
                user.setActivated(userDTO.getActivated());
                user.setLangKey(userDTO.getLangKey());
                handleSavingUserAuthorities(userDTO, user);
                userRepository.save(user);
                this.clearUserCaches(user);
                log.debug("Changed Information for User: {}", user);
                return user;
            })
            .map(userMapper::userToAdminUserDTO);
    }

    public void handleSavingUserAuthorities(AdminUserDTO userDTO, User userDomain) {
        Set<Authority> managedAuthorities = userDomain.getAuthorities();
        Set<String> newAuthorities = userDTO.getAuthorities();

        // Check if current user is trying to modify an admin user
        boolean isTargetAdmin = managedAuthorities.stream().anyMatch(auth -> auth.getName().equals(AuthoritiesConstants.ADMIN));

        // Get current user's authorities
        String currentUserLogin = SecurityUtils
            .getCurrentUserLogin()
            .orElseThrow(() -> new RuntimeException("Current user login not found"));
        User currentUser = getUserByLogin(currentUserLogin).orElseThrow(() -> new RuntimeException("Current user not found"));
        boolean isCurrentUserSuperAdmin = currentUser
            .getAuthorities()
            .stream()
            .anyMatch(auth -> auth.getName().equals(AuthoritiesConstants.SUPER_ADMIN));
        boolean isCurrentUserAdmin = currentUser
            .getAuthorities()
            .stream()
            .anyMatch(auth -> auth.getName().equals(AuthoritiesConstants.ADMIN));

        // Only super admin can modify admin users
        if (isTargetAdmin && !isCurrentUserSuperAdmin) {
            throw new RuntimeException("Only super admins can modify admin users");
        }

        // Check if trying to grant admin privileges
        boolean willHaveAdmin = newAuthorities.contains(AuthoritiesConstants.ADMIN);
        boolean hadAdmin = managedAuthorities.stream().anyMatch(auth -> auth.getName().equals(AuthoritiesConstants.ADMIN));

        // Only super admin can grant admin privileges
        if (!hadAdmin && willHaveAdmin && !isCurrentUserSuperAdmin) {
            throw new RuntimeException("Only super admins can grant admin privileges");
        }

        // Prevent modification of SUPER_ADMIN role
        boolean hadSuperAdmin = managedAuthorities.stream().anyMatch(auth -> auth.getName().equals(AuthoritiesConstants.SUPER_ADMIN));
        boolean willHaveSuperAdmin = newAuthorities.contains(AuthoritiesConstants.SUPER_ADMIN);

        if (hadSuperAdmin != willHaveSuperAdmin) {
            throw new RuntimeException("SUPER_ADMIN role cannot be added or removed");
        }

        // Update authorities
        managedAuthorities.clear();
        newAuthorities
            .stream()
            .map(authorityRepository::findById)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .forEach(managedAuthorities::add);
    }

    public Set<Authority> handleCreatingUserAuthorities(AdminUserDTO userDTO) {
        Set<String> authorities = userDTO.getAuthorities();

        // Get current user's authorities
        String currentUserLogin = SecurityUtils
            .getCurrentUserLogin()
            .orElseThrow(() -> new RuntimeException("Current user login not found"));
        User currentUser = getUserByLogin(currentUserLogin).orElseThrow(() -> new RuntimeException("Current user not found"));
        boolean isCurrentUserSuperAdmin = currentUser
            .getAuthorities()
            .stream()
            .anyMatch(auth -> auth.getName().equals(AuthoritiesConstants.SUPER_ADMIN));

        // Check if trying to create an admin user
        boolean isCreatingAdmin = authorities.contains(AuthoritiesConstants.ADMIN);

        // Only super admin can create admin users
        if (isCreatingAdmin && !isCurrentUserSuperAdmin) {
            throw new RuntimeException("Only super admins can create admin users");
        }

        // Prevent creation of users with SUPER_ADMIN role
        if (authorities.contains(AuthoritiesConstants.SUPER_ADMIN)) {
            throw new RuntimeException("Cannot create users with SUPER_ADMIN role");
        }

        // Create authorities set
        Set<Authority> managedAuthorities = new HashSet<>();
        authorities
            .stream()
            .map(authorityRepository::findById)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .forEach(managedAuthorities::add);

        return managedAuthorities;
    }

    public void deleteUser(String login) {
        userRepository
            .findOneByLogin(login)
            .ifPresent(user -> {
                // Check if user has SUPER_ADMIN role
                boolean isSuperAdmin = user
                    .getAuthorities()
                    .stream()
                    .anyMatch(auth -> auth.getName().equals(AuthoritiesConstants.SUPER_ADMIN));

                if (isSuperAdmin) {
                    throw new RuntimeException("Users with SUPER_ADMIN role cannot be deleted");
                }

                // Get current user's authorities
                String currentUserLogin = SecurityUtils
                    .getCurrentUserLogin()
                    .orElseThrow(() -> new RuntimeException("Current user login not found"));
                User currentUser = getUserByLogin(currentUserLogin).orElseThrow(() -> new RuntimeException("Current user not found"));
                boolean isCurrentUserSuperAdmin = currentUser
                    .getAuthorities()
                    .stream()
                    .anyMatch(auth -> auth.getName().equals(AuthoritiesConstants.SUPER_ADMIN));

                // Check if target user is admin
                boolean isTargetAdmin = user.getAuthorities().stream().anyMatch(auth -> auth.getName().equals(AuthoritiesConstants.ADMIN));

                // Only super admin can delete admin users
                if (isTargetAdmin && !isCurrentUserSuperAdmin) {
                    throw new RuntimeException("Only super admins can delete admin users");
                }

                userRepository.delete(user);
                this.clearUserCaches(user);
                log.debug("Deleted User: {}", user);
            });
    }

    /**
     * Update basic information (first name, last name, email, language) for the current user.
     *
     * @param firstName first name of user.
     * @param lastName  last name of user.
     * @param email     email id of user.
     * @param langKey   language key.
     * @param imageUrl  image URL of user.
     */
    public void updateUser(String firstName, String lastName, String email, String langKey, String imageUrl) {
        SecurityUtils
            .getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                user.setFirstName(firstName);
                user.setLastName(lastName);
                if (email != null) {
                    user.setEmail(email.toLowerCase());
                }
                user.setLangKey(langKey);
                user.setImageUrl(imageUrl);
                userRepository.save(user);
                this.clearUserCaches(user);
                log.debug("Changed Information for User: {}", user);
            });
    }

    public void changePassword(String currentClearTextPassword, String newPassword) {
        SecurityUtils
            .getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                String currentEncryptedPassword = user.getPassword();
                if (!passwordEncoder.matches(currentClearTextPassword, currentEncryptedPassword)) {
                    throw new InvalidPasswordException();
                }
                String encryptedPassword = passwordEncoder.encode(newPassword);
                user.setPassword(encryptedPassword);
                userRepository.save(user);
                this.clearUserCaches(user);
                log.debug("Changed password for User: {}", user);
            });
    }

    public Page<AdminUserDTO> getAllManagedUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(userMapper::userToAdminUserDTO);
    }

    public Page<UserDTO> getAllPublicUsers(Pageable pageable) {
        return userRepository.findAllByIdNotNullAndActivatedIsTrue(pageable).map(userMapper::userToUserDTO);
    }

    public Optional<User> getUserWithAuthoritiesByLogin(String login) {
        return userRepository.findOneByLogin(login);
    }

    public Optional<User> getUserWithAuthorities() {
        return SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneByLogin);
    }

    /**
     * Not activated users should be automatically deleted after 3 days.
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void removeNotActivatedUsers() {
        userRepository
            .findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant.now().minus(3, ChronoUnit.DAYS))
            .forEach(user -> {
                log.debug("Deleting not activated user {}", user.getLogin());
                userRepository.delete(user);
                this.clearUserCaches(user);
            });
    }

    /**
     * Gets a list of all the authorities.
     * @return a list of all the authorities.
     */
    public List<String> getAuthorities() {
        return authorityRepository.findAll().stream().map(Authority::getName).collect(Collectors.toList());
    }

    public List<Authority> getAuthoritiesDomains() {
        return new ArrayList<>(authorityRepository.findAll());
    }

    public AuthorityDTO toAuthorityDTO(Authority authority) {
        AuthorityDTO authorityDTO = new AuthorityDTO();
        authorityDTO.category(authority.getCategory());
        authorityDTO.setId(authority.getName());
        authorityDTO.setDescription(authority.getDescription());
        return authorityDTO;
    }

    private void clearUserCaches(User user) {
        Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE)).evict(user.getLogin());
        if (user.getEmail() != null) {
            Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE)).evict(user.getEmail());
        }
    }

    private Optional<User> getUserByLogin(String login) {
        return userRepository.findOneByLogin(login);
    }

    public static void checkAuthority(String authority) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (
            authentication == null ||
            (
                authentication.getAuthorities().stream().noneMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(authority)) &&
                authentication
                    .getAuthorities()
                    .stream()
                    .noneMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(AuthoritiesConstants.ADMIN)) &&
                authentication
                    .getAuthorities()
                    .stream()
                    .noneMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(AuthoritiesConstants.SUPER_ADMIN))
            )
        ) {
            throw new AccessDeniedException("Access is denied Authority required : " + authority);
        }
    }

    public Optional<User> getCurrentUserLogin() {
        return SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneByLogin);
    }
}
