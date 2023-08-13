package ru.example.pastebox.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.example.pastebox.dto.PasteboxRequestDto;
import ru.example.pastebox.dto.PasteboxResponseDto;
import ru.example.pastebox.dto.PasteboxUrlResponseDto;
import ru.example.pastebox.service.PasteboxService;

import java.util.Collections;
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
    public List<PasteboxResponseDto> getPasteboxList(){
        return pasteboxService.getList();
    }

    @GetMapping("/{hash}")
    public PasteboxResponseDto getByHash(@PathVariable("hash") String hash){
        return pasteboxService.getByHash(hash);

    }

    @PostMapping
    public PasteboxUrlResponseDto addPastebox(@RequestBody PasteboxRequestDto pasteboxRequest){
        return pasteboxService.add(pasteboxRequest);
    }

}
