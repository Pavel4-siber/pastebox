package ru.example.pastebox.IT;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.example.pastebox.model.Pastebox;
import ru.example.pastebox.model.PublicStatus;
import ru.example.pastebox.repository.PasteboxRepository;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Zhurenkov Pavel 20.08.2023
 */

@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
public class PasteboxControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasteboxRepository pasteboxRepository;

    @AfterEach
    void destroy(){
        pasteboxRepository.clear();
    }


    @Test()
    //@WithMockUser(username = "USER", password = "user")
    void getPasteboxList_ReturnsValidResponse() throws Exception {
        initDB();
        var requestBuilder = get("/v1");
        this.mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json("""
                                [
                                {
                                "data": "test1",
                                "isPublic": true
                                },
                                {
                                "data": "test2",
                                "isPublic": true
                                }
                                ]
                                """)
                );
    }

    @Test
    void addPastebox_PayloadIsValid_ReturnsValidResponse() throws Exception {
        var requestBuilder = post("/v1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "data": "test",
                        "expirationTime": "100",
                        "publicStatus": "PUBLIC"
                        }
                        """);
        this.mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isCreated(),
                        header().exists(HttpHeaders.CONTENT_TYPE),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json("""
                                {
                                "url":"https://pastebox.example.ru/0"
                                }
                                """)
                );
        assertEquals(1, pasteboxRepository.get(10).size());
        assertNotNull(pasteboxRepository.get(10).get(0));
        assertEquals("test", pasteboxRepository.get(10).get(0).getData());
        assertEquals("PUBLIC", pasteboxRepository.get(10).get(0).getStatus().name());
    }

    @Test
    void addPastebox_PayloadIsInvalid_ReturnsValidResponse() throws Exception {
        var requestBuilder = post("/v1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "data": null,
                        "expirationTime": "100",
                        "publicStatus": "PUBLIC"
                        }
                        """);
        this.mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isBadRequest(),
                        header().doesNotExist(HttpHeaders.LOCATION),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json("""
                                {
                                "errors": ["Task details must be set"]
                                }
                                """)
                );
        assertTrue(pasteboxRepository.get(10).isEmpty());
    }

    private void initDB() {
        Pastebox pastebox1 = Pastebox.builder()
                .id(1L)
                .data("test1")
                .lifetime(LocalDateTime.now().plusSeconds(100))
                .hash("1")
                .status(PublicStatus.PUBLIC)
                .build();
        Pastebox pastebox2 = Pastebox.builder()
                .id(1L)
                .data("test2")
                .lifetime(LocalDateTime.now().plusSeconds(100))
                .hash("2")
                .status(PublicStatus.PUBLIC)
                .build();
        pasteboxRepository.add(pastebox1);
        pasteboxRepository.add(pastebox2);
    }

}
