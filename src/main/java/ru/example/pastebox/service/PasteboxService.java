package ru.example.pastebox.service;

import ru.example.pastebox.dto.PasteboxRequestDto;
import ru.example.pastebox.dto.PasteboxResponseDto;
import ru.example.pastebox.dto.PasteboxUrlResponseDto;

import java.util.List;

/**
 * @author Zhurenkov Pavel 13.08.2023
 */
public interface PasteboxService {

    List<PasteboxResponseDto> getList();

    PasteboxResponseDto getByHash(String hash);

    PasteboxUrlResponseDto add(PasteboxRequestDto pasteboxRequest);
}
