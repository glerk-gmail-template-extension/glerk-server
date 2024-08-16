package com.glerk.core.service;

import com.glerk.core.common.Trie;
import com.glerk.core.dto.AutocompleteEmailDto;
import com.glerk.core.entity.User;
import com.glerk.core.repository.TemplateEmailRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class AutocompleteService {

    private final TemplateEmailRepository templateEmailRepository;

    public AutocompleteService(TemplateEmailRepository templateEmailRepository) {
        this.templateEmailRepository = templateEmailRepository;
    }

    public List<AutocompleteEmailDto> getAutocompleteEmails(String query, User user) {
        List<AutocompleteEmailDto> templateEmailDtos = templateEmailRepository.findDistinctEmailsByCreatedByOrderByUpdatedAtDesc(user.getId());

        Trie trie = new Trie();

        for (AutocompleteEmailDto templateEmailDto : templateEmailDtos) {
            trie.insert(templateEmailDto.getEmail(), templateEmailDto.getUpdatedAt());
        }

        return trie.query(query);
    }
}
