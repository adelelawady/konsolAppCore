package com.konsol.core.web.rest.api.errors;

import javax.annotation.Nullable;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import org.zalando.problem.ThrowableProblem;

@SuppressWarnings("java:S110") // Inheritance tree of classes should not be too deep
public class InvoiceException extends ThrowableProblem {

    private static final long serialVersionUID = 1L;

    public InvoiceException(String title, @Nullable String details) {
        throw Problem.builder().withTitle(title).withStatus(Status.BAD_REQUEST).withDetail(details).build();
    }
}
