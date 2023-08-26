package ru.example.pastebox.repository;

import ru.example.pastebox.dto.PasteboxRequestDto;
import ru.example.pastebox.model.Pastebox;

import java.util.List;
import java.util.Optional;

/**
 * @author Zhurenkov Pavel 13.08.2023
 */
public interface PasteboxRepository {

    List<Pastebox> get(int publicListSize);

    Optional<Pastebox> getByHash(String hash);

    Pastebox add(Pastebox pastebox);

    void clear();
}
