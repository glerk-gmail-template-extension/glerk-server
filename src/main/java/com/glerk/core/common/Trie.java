package com.glerk.core.common;

import com.glerk.core.dto.AutocompleteEmailDto;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class Trie {
    private final TrieNode root;

    public Trie() {
        this.root = new TrieNode();
    }

    public void insert(String email, LocalDateTime updatedAt) {
        String localPart = email.split("@")[0].toLowerCase();
        TrieNode node = root;

        for (char c : localPart.toCharArray()) {
            node = node.children.computeIfAbsent(c, k -> new TrieNode());
        }

        node.addEmail(new AutocompleteEmailDto(email, updatedAt));
    }

    public List<AutocompleteEmailDto> query(String prefix) {
        prefix = prefix.toLowerCase();

        List<AutocompleteEmailDto> prefixMatches = getPrefixMatches(prefix);
        List<AutocompleteEmailDto> substringMatches = getSubstringMatches(prefix);
        List<AutocompleteEmailDto> similarMatches = getSimilarMatches(prefix);

        prefixMatches.sort(Comparator.comparing(AutocompleteEmailDto::getUpdatedAt).reversed());
        substringMatches.sort(Comparator.comparing(AutocompleteEmailDto::getUpdatedAt).reversed());
        similarMatches.sort(Comparator.comparing(AutocompleteEmailDto::getUpdatedAt).reversed());

        substringMatches.removeAll(prefixMatches);
        similarMatches.removeAll(prefixMatches);
        similarMatches.removeAll(substringMatches);

        List<AutocompleteEmailDto> result = new ArrayList<>(prefixMatches);
        result.addAll(substringMatches);
        result.addAll(similarMatches);

        return result.stream().limit(10).collect(Collectors.toList());
    }

    private List<AutocompleteEmailDto> getPrefixMatches(String prefix) {
        TrieNode prefixNode = findNode(prefix);

        if (prefixNode == null) {
            return new ArrayList<>();
        }

        return getAllEmailsFromNode(prefixNode);
    }

    private List<AutocompleteEmailDto> getSubstringMatches(String substring) {
        return getAllEmailsFromNode(root).stream()
                .filter(dto -> {
                    String localPart = dto.getEmail().split("@")[0].toLowerCase();
                    return localPart.contains(substring) && !localPart.startsWith(substring);
                })
                .collect(Collectors.toList());
    }

    private List<AutocompleteEmailDto> getSimilarMatches(String prefix) {
        return getAllEmailsFromNode(root).stream()
                .filter(dto -> {
                    String localPart = dto.getEmail().split("@")[0].toLowerCase();
                    return !localPart.contains(prefix) &&
                            LevenshteinDistance.compute(prefix, localPart.substring(0, Math.min(prefix.length(), localPart.length()))) == 1;
                })
                .collect(Collectors.toList());
    }

    private TrieNode findNode(String prefix) {
        TrieNode node = root;

        for (char c : prefix.toCharArray()) {
            node = node.children.get(c);
            if (node == null) {
                return null;
            }
        }

        return node;
    }

    private List<AutocompleteEmailDto> getAllEmailsFromNode(TrieNode node) {
        List<AutocompleteEmailDto> emails = new ArrayList<>(node.getEmails());

        for (TrieNode child : node.children.values()) {
            emails.addAll(getAllEmailsFromNode(child));
        }

        return emails;
    }
}
