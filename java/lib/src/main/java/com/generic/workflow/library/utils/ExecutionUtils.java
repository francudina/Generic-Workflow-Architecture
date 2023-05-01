package com.generic.workflow.library.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ExecutionUtils {

    private static final Logger log = Logger.getLogger(ExecutionUtils.class.getName());

    /**
     * Method converts any object to {@link Map} containing field name as key, and value as field value from given object.
     *
     * @param object value/object to convert to {@link Map}
     * @return {@link Map} containing field name as key, and value as field value from given object
     * @param <T> input object type
     */
    public static <T> Map<String, Object> objectToMap(T object) {
        try {
            return Stream.of(object.getClass().getDeclaredFields())
                    .filter(field -> {
                        try {
                            return field.get(object) != null;
                        } catch (IllegalAccessException e) {
                            return false;
                        }
                    })
                    .collect(
                            Collectors.toMap(
                                    Field::getName,
                                    field -> {
                                        try {
                                            return field.get(object);
                                        } catch (IllegalAccessException e) {
                                            log.warning(String.format("Cannot map input object to map for field %s! " +
                                                    "Input: %s", field.getName(), object));
                                            throw new RuntimeException(e);
                                        }
                                    }
                            )
                    );
        } catch (Exception e) {
            log.warning(String.format("Catching stream toMap exception: %s", printStackTrace(e)));
        }
        return new HashMap<>();
    }

    public static String printStackTrace(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }
}
