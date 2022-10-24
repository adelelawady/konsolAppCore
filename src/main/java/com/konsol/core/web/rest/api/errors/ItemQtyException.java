package com.konsol.core.web.rest.api.errors;

import javax.annotation.Nullable;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import org.zalando.problem.ThrowableProblem;

@SuppressWarnings("java:S110") // Inheritance tree of classes should not be too deep
public class ItemQtyException extends ThrowableProblem {

    private static final long serialVersionUID = 1L;

    public ItemQtyException(String message, @Nullable String details, @Nullable ThrowableProblem cause) {
        throw Problem.builder().withTitle(message).withStatus(Status.BAD_REQUEST).withCause(cause).withDetail(details).build();
    }
}
