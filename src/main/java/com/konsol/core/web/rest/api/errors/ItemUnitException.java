package com.konsol.core.web.rest.api.errors;

import com.konsol.core.web.rest.errors.BadRequestAlertException;
import com.konsol.core.web.rest.errors.ErrorConstants;
import javax.annotation.Nullable;
import org.zalando.problem.*;

@SuppressWarnings("java:S110") // Inheritance tree of classes should not be too deep
public class ItemUnitException extends ThrowableProblem {

    private static final long serialVersionUID = 1L;

    public ItemUnitException(String message, @Nullable String details) {
        throw Problem.builder().withTitle(message).withStatus(Status.BAD_REQUEST).withDetail(details).build();
    }
}
