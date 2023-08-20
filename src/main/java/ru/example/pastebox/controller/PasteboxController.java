package ru.example.pastebox.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.example.pastebox.dto.PasteboxRequestDto;
import ru.example.pastebox.dto.PasteboxResponseDto;
import ru.example.pastebox.dto.PasteboxUrlResponseDto;
import ru.example.pastebox.service.PasteboxService;

import java.util.List;

/**
 * @author Zhurenkov Pavel 13.08.2023
 */
@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class PasteboxController {

    private final PasteboxService pasteboxService;

    @GetMapping
    public ResponseEntity<List<PasteboxResponseDto>> getPasteboxList(){
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(pasteboxService.getList());
    }

    @GetMapping("/{hash}")
    public ResponseEntity<PasteboxResponseDto> getByHash(@PathVariable("hash") String hash){
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(pasteboxService.getByHash(hash));
    }

    @PostMapping
    public ResponseEntity<PasteboxUrlResponseDto> addPastebox(@RequestBody PasteboxRequestDto pasteboxRequest){
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(pasteboxService.add(pasteboxRequest));
    }
}
