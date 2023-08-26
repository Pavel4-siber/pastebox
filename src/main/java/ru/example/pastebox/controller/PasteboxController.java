package ru.example.pastebox.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.example.pastebox.dto.ErrorsPresentation;
import ru.example.pastebox.dto.PasteboxRequestDto;
import ru.example.pastebox.dto.PasteboxResponseDto;
import ru.example.pastebox.dto.PasteboxUrlResponseDto;
import ru.example.pastebox.service.PasteboxService;

import java.util.List;
import java.util.Locale;

/**
 * @author Zhurenkov Pavel 13.08.2023
 */
@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class PasteboxController {

    private final PasteboxService pasteboxService;

    private final MessageSource messageSource;

    @GetMapping
    public ResponseEntity<List<PasteboxResponseDto>> getPasteboxList(){
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(pasteboxService.getList());
    }

    @GetMapping("/{hash}")
    public ResponseEntity<PasteboxResponseDto> getByHash(@PathVariable("hash") String hash){
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(pasteboxService.getByHash(hash));
    }

    @PostMapping(consumes = {"text/plain", "application/json"})
    public ResponseEntity<?> addPastebox(@RequestBody PasteboxRequestDto pasteboxRequest){
        if (pasteboxRequest.data() == null || pasteboxRequest.data().isBlank()){
            final var message = this.messageSource.getMessage("tasks.create.details.errors.not_set", new Object[0], Locale.ENGLISH);
            return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON)
                    .body(new ErrorsPresentation(List.of(message)));
        } else
        return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(pasteboxService.add(pasteboxRequest));
    }
}
