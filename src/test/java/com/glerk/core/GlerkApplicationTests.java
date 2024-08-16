package com.glerk.core;

import com.glerk.core.dto.AutocompleteEmailDto;
import com.glerk.core.entity.User;
import com.glerk.core.repository.TemplateEmailRepository;
import com.glerk.core.service.AutocompleteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ActiveProfiles("test")
@SpringBootTest
class GlerkApplicationTests {

    @Mock
    private TemplateEmailRepository templateEmailRepository;

    private AutocompleteService autocompleteService;
    private User mockUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockUser = new User();
        mockUser.setId(1L);

        List<AutocompleteEmailDto> mockEmails = Arrays.asList(
                new AutocompleteEmailDto("jieun@example.com", LocalDateTime.of(2024, Month.JULY, 30, 17, 0)),
                new AutocompleteEmailDto("jiji@test.com", LocalDateTime.of(2024, Month.JULY, 29, 0, 0)),
                new AutocompleteEmailDto("eji@website.org", LocalDateTime.of(2024, Month.AUGUST, 2, 17, 0)),
                new AutocompleteEmailDto("toy3045@website.org", LocalDateTime.of(2024, Month.MAY, 30, 17, 0)),
                new AutocompleteEmailDto("test@ji.org", LocalDateTime.of(2024, Month.APRIL, 1, 4, 0)),
                new AutocompleteEmailDto("hojin@domain.com", LocalDateTime.of(2024, Month.APRIL, 1, 14, 0))
        );

        when(templateEmailRepository.findDistinctEmailsByCreatedByOrderByUpdatedAtDesc(1L))
                .thenReturn(mockEmails);

        autocompleteService = new AutocompleteService(templateEmailRepository);
    }

    @Test
    void testGetAutocompleteEmails_WithPrefixJi() {
        List<AutocompleteEmailDto> result = autocompleteService.getAutocompleteEmails("ji", mockUser);

        assertEquals(4, result.size());
        assertEquals("jieun@example.com", result.get(0).getEmail());
        assertEquals("jiji@test.com", result.get(1).getEmail());
        assertEquals("eji@website.org", result.get(2).getEmail());
        assertEquals("hojin@domain.com", result.get(3).getEmail());
    }


    @Test
    void testGetAutocompleteEmails_WithNonExistentPrefix() {
        List<AutocompleteEmailDto> result = autocompleteService.getAutocompleteEmails("xyz", mockUser);

        assertEquals(0, result.size());
    }

    @Test
    void testGetAutocompleteEmails_WithSimilarPrefix() {
        List<AutocompleteEmailDto> result = autocompleteService.getAutocompleteEmails("houi", mockUser);

        assertEquals(1, result.size());
        assertEquals("hojin@domain.com", result.get(0).getEmail());
    }

}
