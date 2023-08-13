package ru.example.pastebox.dto;

import ru.example.pastebox.model.PublicStatus;

/**
 * @author Zhurenkov Pavel 13.08.2023
 */
public record PasteboxRequestDto(String data,
                                 long expirationTime,
                                 String publicStatus){
}
