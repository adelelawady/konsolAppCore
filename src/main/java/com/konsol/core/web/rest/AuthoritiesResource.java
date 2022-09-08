package com.konsol.core.web.rest;

import com.konsol.core.service.UserService;
import com.konsol.core.web.api.AuthoritiesApiDelegate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

//@RestController
//@RequestMapping("/api")
@Service
public class AuthoritiesResource implements AuthoritiesApiDelegate {

    private static final List<String> ALLOWED_ORDERED_PROPERTIES = Collections.unmodifiableList(
        Arrays.asList("id", "login", "firstName", "lastName", "email", "activated", "langKey")
    );

    private final Logger log = LoggerFactory.getLogger(AuthoritiesResource.class);

    private final UserService userService;

    public AuthoritiesResource(UserService userService) {
        this.userService = userService;
    }

    private boolean onlyContainsAllowedProperties(Pageable pageable) {
        return pageable.getSort().stream().map(Sort.Order::getProperty).allMatch(ALLOWED_ORDERED_PROPERTIES::contains);
    }

    /**
     * Gets a list of all roles.
     *
     * @return a string list of all roles.
     */
    // @GetMapping("/authorities")
    @Override
    public ResponseEntity<List<String>> getAuthorities() {
        return ResponseEntity.ok(userService.getAuthorities());
    }
}
