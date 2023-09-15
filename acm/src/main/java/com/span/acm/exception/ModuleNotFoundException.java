package com.span.acm.exception;

import javax.persistence.EntityNotFoundException;

public class ModuleNotFoundException extends EntityNotFoundException {
    public ModuleNotFoundException(String message) {
        super(message);
    }
}
