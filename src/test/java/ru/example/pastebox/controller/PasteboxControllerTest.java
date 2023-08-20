package ru.example.pastebox.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import ru.example.pastebox.dto.ErrorsPresentation;
import ru.example.pastebox.dto.PasteboxRequestDto;
import ru.example.pastebox.dto.PasteboxResponseDto;
import ru.example.pastebox.dto.PasteboxUrlResponseDto;
import ru.example.pastebox.model.PublicStatus;
import ru.example.pastebox.repository.PasteboxRepository;
import ru.example.pastebox.service.PasteboxService;

import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * @author Zhurenkov Pavel 20.08.2023
 */
@ExtendWith(MockitoExtension.class)
class PasteboxControllerTest {

    @Mock
    PasteboxService pasteboxService;

    @Mock
    MessageSource messageSource;

    @InjectMocks
    PasteboxController pasteboxController;

    @Test
    void getPasteboxList_ReturnsValidListResponse() {

        var expected = List.of(new PasteboxResponseDto("1", true),
                new PasteboxResponseDto("2", false));

        when(this.pasteboxService.getList()).thenReturn(expected);

        var actual = this.pasteboxController.getPasteboxList();

        assertNotNull(actual);
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, actual.getHeaders().getContentType());
        assertEquals(expected, actual.getBody());
    }

    @Test
    void getByHash_ReturnsValidByHashResponse() {
        var expected = new PasteboxResponseDto("1", true);
        String hash = "1";
        when(this.pasteboxService.getByHash(hash)).thenReturn(expected);

        var actual = this.pasteboxController.getByHash(hash);

        assertNotNull(actual);
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, actual.getHeaders().getContentType());
        assertEquals(expected, actual.getBody());
    }

    @Test
    void addPastebox_DataIsValid_ReturnsValidResponse() {
        var expected = new PasteboxUrlResponseDto("http://pastebox.example.ru");
        var requestDto = new PasteboxRequestDto("1",100, PublicStatus.PUBLIC.name());
        when(this.pasteboxService.add(requestDto)).thenReturn(expected);

        var actual = this.pasteboxController.addPastebox(requestDto);

        assertNotNull(actual);
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, actual.getHeaders().getContentType());
        assertEquals(expected, actual.getBody());
    }

    @Test
    void addPastebox_DataInvalid_ReturnsValidResponse() {
        var locale = Locale.US;
        var errorMessage = "Data not set";

        var requestDto = new PasteboxRequestDto(null,100, PublicStatus.PUBLIC.name());
        doReturn(errorMessage).when(this.messageSource).getMessage("pastebox.errors.data_not_set", new Object[0], locale);
        var actual = this.pasteboxController.addPastebox(requestDto);

        assertNotNull(actual);
        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, actual.getHeaders().getContentType());
        assertEquals(new ErrorsPresentation(List.of(errorMessage)), actual.getBody());

        verifyNoInteractions(pasteboxService);
    }
}
