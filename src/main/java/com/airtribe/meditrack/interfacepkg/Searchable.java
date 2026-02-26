package com.airtribe.meditrack.interfacepkg;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public interface Searchable<T> {

    List<T> findAll();

    default List<T> search(Predicate<T> predicate) {
        return findAll().stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }
}

