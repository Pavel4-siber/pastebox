package ru.example.pastebox.repository;

import org.springframework.stereotype.Repository;
import ru.example.pastebox.dto.PasteboxRequestDto;
import ru.example.pastebox.model.Pastebox;
import ru.example.pastebox.model.PublicStatus;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Zhurenkov Pavel 13.08.2023
 */

@Repository
public class PasteboxRepositoryImpl implements PasteboxRepository {

    private final Map<String, Pastebox> map = new ConcurrentHashMap<>();

    @Override
    public List<Pastebox> get(int publicListSize) {
        LocalDateTime now = LocalDateTime.now();

        return map.values().stream()
                .filter(pastebox -> pastebox.getStatus().name() == PublicStatus.PUBLIC.name())
                .filter(pastebox -> pastebox.getLifetime().isAfter(now))
                .sorted(Comparator.comparing(Pastebox::getId).reversed())
                .limit(publicListSize)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Pastebox> getByHash(String hash) {
        return Optional.ofNullable(map.get(hash));
    }

    @Override
    public Pastebox add(Pastebox pastebox) {
        map.put(pastebox.getHash(), pastebox);
        return pastebox;
    }

    @Override
    public void clear(){
        map.clear();
    }
}
