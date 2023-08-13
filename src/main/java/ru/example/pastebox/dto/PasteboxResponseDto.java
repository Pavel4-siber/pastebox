package ru.example.pastebox.dto;

import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * @author Zhurenkov Pavel 13.08.2023
 */
public record PasteboxResponseDto(String data,
                                  boolean isPublic) {
}
