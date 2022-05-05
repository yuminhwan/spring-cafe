package com.prgrms.springcafe.product.domain;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public enum Category {
    COFFEE, CAKE;

    public static List<String> toStringAll() {
        return Arrays.stream(Category.values())
            .map(Objects::toString)
            .collect(Collectors.toList());
    }
}
