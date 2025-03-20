package com.first_spring.demo.entities.utils;

import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.first_spring.demo.exceptions.InvalidFieldUpdateException;

/**
 * Utility class for dynamically updating entity fields.
 */
public class EntityUpdater {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Updates an entity dynamically using a map of fields while excluding certain
     * fields.
     *
     * @param entity         The entity to update.
     * @param updates        The map of field names and values to update.
     * @param excludedFields The set of fields to exclude from updating.
     * @param <T>            The type of entity.
     */
    public static <T> void updateEntityFields(T entity, Map<String, Object> updates, Set<String> excludedFields) {
        updates.forEach((key, value) -> {
            if (!excludedFields.contains(key) && value != null) { // Skip excluded & null fields
                try {
                    objectMapper.updateValue(entity, Map.of(key, value)); // Apply updates dynamically
                } catch (JsonMappingException e) {
                    throw new InvalidFieldUpdateException(key, e.getOriginalMessage());
                }
            }
        });
    }
}
