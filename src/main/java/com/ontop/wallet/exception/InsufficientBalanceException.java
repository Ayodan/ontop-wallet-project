package com.ontop.wallet.exception;

public class InsufficientBalanceException extends AbstractException{

    public InsufficientBalanceException(String code, String message) {
        super(code, message);
    }
}
