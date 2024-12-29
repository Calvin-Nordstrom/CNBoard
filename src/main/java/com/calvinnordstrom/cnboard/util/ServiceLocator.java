package com.calvinnordstrom.cnboard.util;

import java.util.HashMap;
import java.util.Map;

public class ServiceLocator {
    private static ServiceLocator instance;
    private final Map<String, Object> services = new HashMap<>();

    private ServiceLocator() {}

    public <T> void register(String key, T service) {
        services.put(key, service);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> type) {
        Object service = services.get(key);
        if (type.isInstance(service)) {
            return (T) service;
        } else {
            throw new IllegalArgumentException("Service not found for key: " + key);
        }
    }

    public static ServiceLocator getInstance() {
        if (instance == null) {
            instance = new ServiceLocator();
        }
        return instance;
    }
}
