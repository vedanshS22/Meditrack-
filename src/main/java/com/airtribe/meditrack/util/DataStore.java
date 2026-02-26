package com.airtribe.meditrack.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class DataStore<T> {

    private final Map<String, T> store = new ConcurrentHashMap<>();

    public void save(String id, T entity) {
        store.put(id, entity);
    }

    public Optional<T> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    public List<T> findAll() {
        return new ArrayList<>(store.values());
    }

    public boolean delete(String id) {
        return store.remove(id) != null;
    }
}

