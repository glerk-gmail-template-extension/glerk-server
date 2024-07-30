package com.glerk.core.common;

import com.glerk.core.dto.AutocompleteEmailDto;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.glerk.core.common.LevenshteinDistance.levDistance;

public class Trie {
    private final Node head;
    private final Map<String, LocalDateTime> emails;

    public Trie() {
        this.head = new Node();
        this.emails = new HashMap<>();
    }

    public void insertPrefix(String email, LocalDateTime time) {
        Node currentNode = this.head;
        String localPart = email.split("@")[0];

        for (char emailChar : localPart.toCharArray()) {
            currentNode.getChildren().putIfAbsent(emailChar, new Node());
            currentNode = currentNode.getChildren().get(emailChar);
            currentNode.prefixDataInsert(email, time);
        }

        this.emails.put(email, time);
    }

    public void insertSuffix(String localPart, String email, LocalDateTime time) {
        Node currentNode = this.head;
        for (char localChar : localPart.toCharArray()) {
            currentNode.getChildren().putIfAbsent(localChar, new Node());
            currentNode = currentNode.getChildren().get(localChar);
            currentNode.suffixDataInsert(email, time);
        }

        this.emails.put(email, time);
    }

    public List<AutocompleteEmailDto> query(String email) {
        Node currentNode = this.head;
        List<String> suggestedEmails = new ArrayList<>();

        for (char emailChar : email.toCharArray()) {
            if (!currentNode.getChildren().containsKey(emailChar)) {
                suggestedEmails = suggestEmail(currentNode, email);
                break;
            } else {
                currentNode = currentNode.getChildren().get(emailChar);
            }
        }

        List<AutocompleteEmailDto> prefixResult = new ArrayList<>(currentNode.getPrefixData());
        List<AutocompleteEmailDto> suffixResult = new ArrayList<>();
        List<AutocompleteEmailDto> suggestedResult = new ArrayList<>();

        Set<String> seenEmail = new HashSet<>();
        for (AutocompleteEmailDto emailDto : prefixResult) {
            seenEmail.add(emailDto.getEmail());
        }

        for (AutocompleteEmailDto emailDto : currentNode.getSuffixData()) {
            if (!seenEmail.contains(emailDto.getEmail())) {
                suffixResult.add(emailDto);
                seenEmail.add(emailDto.getEmail());
            }
        }

        if (!suggestedEmails.isEmpty()) {
            suggestedResult = suggestedEmails.stream()
                    .filter(suggestedEmail -> !seenEmail.contains(suggestedEmail))
                    .map(suggestedEmail -> new AutocompleteEmailDto(suggestedEmail, emails.get(suggestedEmail)))
                    .collect(Collectors.toList());
        }

        Collections.reverse(prefixResult);
        Collections.reverse(suffixResult);

        List<AutocompleteEmailDto> autocompleteEmailDtos = new ArrayList<>();

        autocompleteEmailDtos.addAll(prefixResult);
        autocompleteEmailDtos.addAll(suffixResult);
        autocompleteEmailDtos.addAll(suggestedResult);

        return autocompleteEmailDtos;
    }

    public List<String> suggestEmail(Node node, String emailQuery) {
        List<AutocompleteEmailDto> candidate = new ArrayList<>(node.getPrefixData());
        candidate.addAll(node.getSuffixData());
        Collections.reverse(candidate);

        List<Pair<String, Integer>> levList = new ArrayList<>();
        for (AutocompleteEmailDto email : candidate) {
            String localPart = email.getEmail().split("@")[0];
            int levDistance = levDistance(emailQuery.toLowerCase(), localPart);
            levList.add(new Pair<>(email.getEmail(), levDistance));
        }

        levList.sort(Comparator.comparingInt(Pair::getValue));

        return levList.stream()
                .filter(pair -> pair.getValue() <= 2)
                .map(Pair::getKey)
                .collect(Collectors.toList());
    }
}
