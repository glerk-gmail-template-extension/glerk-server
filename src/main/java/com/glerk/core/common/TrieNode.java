package com.glerk.core.common;

import com.glerk.core.dto.AutocompleteEmailDto;

import java.util.*;

public class TrieNode {
    Map<Character, TrieNode> children = new HashMap<>();
    Set<AutocompleteEmailDto> emails = new HashSet<>();

    void addEmail(AutocompleteEmailDto email) {
        emails.add(email);
    }

    Set<AutocompleteEmailDto> getEmails() {
        return emails;
    }
}
