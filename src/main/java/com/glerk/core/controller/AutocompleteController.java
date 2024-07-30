package com.glerk.core.controller;

import com.glerk.core.config.User.CurrentUser;
import com.glerk.core.dto.AutocompleteEmailDto;
import com.glerk.core.entity.User;
import com.glerk.core.service.AutocompleteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/autocomplete")
public class AutocompleteController {

    private final AutocompleteService autocompleteService;

    public AutocompleteController(AutocompleteService autocompleteService) {
        this.autocompleteService = autocompleteService;
    }

    @GetMapping
    public ResponseEntity<List<AutocompleteEmailDto>> getAutocompleteEmails(@RequestParam(value = "email") String email,
                                                                            @CurrentUser User user) {
        List<AutocompleteEmailDto> autocompleteEmailDtos = autocompleteService.getAutocompleteEmails(email, user);
        return ResponseEntity.ok().body(autocompleteEmailDtos);
    }
}
