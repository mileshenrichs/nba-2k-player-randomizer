package com._2kblacktoprandomizer.loganalyzer.models;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.HashMap;
import java.util.Map;

public enum RandomizationMode {
    ALL,
    GUARDS,
    CENTERS,
    FORWARDS,
    TOP;

    private static Map<String, RandomizationMode> serializationMap;

    static {
        serializationMap = new HashMap<>();
        serializationMap.put("all", ALL);
        serializationMap.put("guards", GUARDS);
        serializationMap.put("centers", CENTERS);
        serializationMap.put("forwards", FORWARDS);
        serializationMap.put("top", TOP);
    }

    @JsonCreator
    public static RandomizationMode forValue(String value) {
        return serializationMap.get(value);
    }
}
