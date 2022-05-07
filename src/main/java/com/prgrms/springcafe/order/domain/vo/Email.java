package com.prgrms.springcafe.order.domain.vo;

import java.util.Objects;
import java.util.regex.Pattern;

import com.prgrms.springcafe.global.error.exception.InvalidValueException;

public class Email {
    private static final int MAX_EMAIL_LENGTH = 50;
    private static final String ERROR_EMAIL_BLANK_MESSAGE = "이메일이 비어있습니다.";
    private static final String ERROR_EMAIL_LENGTH_OVER_MAX_LENGTH = "이메일은 50자 이상을 넘을 수 없습니다.";
    private static final String ERROR_WRONG_EMAIL_MESSAGE = "이메일 형식이 잘못되었습니다.";
    private static final Pattern EMAIL = Pattern.compile("\\b[\\w\\.-]+@[\\w\\.-]+\\.\\w{2,4}\\b");

    private final String email;

    public Email(String email) {
        this.email = email;
        validateEmail(email);
    }

    private void validateEmail(String email) {
        if (Objects.isNull(email) || email.isBlank()) {
            throw new InvalidValueException(ERROR_EMAIL_BLANK_MESSAGE);
        }
        if (isWrongEmail(email)) {
            throw new InvalidValueException(ERROR_WRONG_EMAIL_MESSAGE);
        }
        if (email.length() > MAX_EMAIL_LENGTH) {
            throw new InvalidValueException(ERROR_EMAIL_LENGTH_OVER_MAX_LENGTH);
        }
    }

    private boolean isWrongEmail(String email) {
        return !EMAIL.matcher(email).matches();
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Email otherEmail = (Email)o;
        return this.email.equals(otherEmail.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmail());
    }
}
