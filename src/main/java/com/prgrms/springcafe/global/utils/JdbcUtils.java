package com.prgrms.springcafe.global.utils;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import com.prgrms.springcafe.global.error.exception.EntityNotFoundException;

public class JdbcUtils {

    private static final int EXECUTE_VALUE = 1;

    private JdbcUtils() {
    }

    public static LocalDateTime toLocalDateTime(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }
        return timestamp.toLocalDateTime();
    }

    public static void validateExecute(int execute) {
        if (execute != EXECUTE_VALUE) {
            throw new EntityNotFoundException("정상적으로 SQL문이 실행되었지만 적용된 것이 없습니다.");
        }
    }
}
