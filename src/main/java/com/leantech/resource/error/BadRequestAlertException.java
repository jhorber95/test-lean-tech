
package com.leantech.resource.error;


public class BadRequestAlertException extends RuntimeException {

    public BadRequestAlertException(String mensaje) {
        super(mensaje);
    }
}

