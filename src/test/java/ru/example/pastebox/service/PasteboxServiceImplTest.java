package ru.example.pastebox.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.example.pastebox.dto.PasteboxResponseDto;
import ru.example.pastebox.exception.NotFoundPasteboxException;
import ru.example.pastebox.model.Pastebox;
import ru.example.pastebox.model.PublicStatus;
import ru.example.pastebox.repository.PasteboxRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * @author Zhurenkov Pavel 14.08.2023
 */
@SpringBootTest
class PasteboxServiceImplTest {

    @Autowired
    PasteboxService pasteboxService;

    @MockBean
    PasteboxRepository pasteboxRepository;

    @Test
    void getExistHash() {
        Pastebox pastebox = Pastebox.builder()
                .id(1L)
                .hash("1")
                .data("test")
                .status(PublicStatus.PUBLIC)
                .lifetime(LocalDateTime.now().plusSeconds(10))
                .build();

        when(pasteboxRepository.getByHash("1")).thenReturn(Optional.of(pastebox));
        PasteboxResponseDto expected = new PasteboxResponseDto("test", true);
        PasteboxResponseDto actual = pasteboxService.getByHash("1");

        assertEquals(expected, actual);

    }

    @Test
    void notExistHash(){
        when(pasteboxRepository.getByHash(anyString())).thenThrow(NotFoundPasteboxException.class);
        assertThrows(NotFoundPasteboxException.class, () -> pasteboxService.getByHash("exception"));
    }

}
