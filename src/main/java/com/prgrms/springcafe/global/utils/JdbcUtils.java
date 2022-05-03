package com.prgrms.springcafe.global.utils;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class JdbcUtils {

    private JdbcUtils() {
    }

    public static LocalDateTime toLocalDateTime(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }
        return timestamp.toLocalDateTime();
    }
}
