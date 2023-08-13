package ru.example.pastebox.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Zhurenkov Pavel 13.08.2023
 */
@Data
@Builder
public class Pastebox {

    private Long id;
    private String data;
    private String hash;
    private LocalDateTime lifetime;
    private PublicStatus status;
}
