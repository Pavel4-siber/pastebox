package ru.example.pastebox.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.example.pastebox.dto.PasteboxRequestDto;
import ru.example.pastebox.dto.PasteboxResponseDto;
import ru.example.pastebox.dto.PasteboxUrlResponseDto;
import ru.example.pastebox.exception.NotFoundPasteboxException;
import ru.example.pastebox.model.Pastebox;
import ru.example.pastebox.model.PublicStatus;
import ru.example.pastebox.repository.PasteboxRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * @author Zhurenkov Pavel 13.08.2023
 */
@Service
@RequiredArgsConstructor
public class PasteboxServiceImpl implements PasteboxService {

    private final PasteboxRepository pasteboxRepository;

    @Value("${app.public_list-size}")
    private int publicListSize;

    @Value(("${app.host}"))
    private String host;

    private AtomicLong idGenerator = new AtomicLong(0);

    @Override
    public List<PasteboxResponseDto> getList() {
        List<Pastebox> pasteboxs = pasteboxRepository.get(publicListSize);
        return pasteboxs.stream().map(pastebox -> new PasteboxResponseDto(pastebox.getData(), true))
                .collect(Collectors.toList());
    }

    @Override
    public PasteboxResponseDto getByHash(String hash) {
        Optional<Pastebox> pastebox = pasteboxRepository.getByHash(hash);
        return pastebox.map(paste -> new PasteboxResponseDto(paste.getData(), true))
                .orElseThrow(() -> new NotFoundPasteboxException("Pastebox not found with hash =" + hash));
    }

    @Override
    public PasteboxUrlResponseDto add(PasteboxRequestDto pasteboxRequest) {
        Long hash = generatedId();
        Pastebox pastebox = Pastebox.builder()
                .id(hash)
                .data(pasteboxRequest.data())
                .hash(Long.toHexString(hash))
                .lifetime(LocalDateTime.now().plusSeconds(pasteboxRequest.expirationTime()))
                .status(PublicStatus.valueOf(pasteboxRequest.publicStatus()))
                .build();

        Pastebox pasteboxEntity = pasteboxRepository.add(pastebox);

        return new PasteboxUrlResponseDto(host + "/" + pasteboxEntity.getHash());
    }

    private Long generatedId(){
        return idGenerator.getAndIncrement();
    }
}
