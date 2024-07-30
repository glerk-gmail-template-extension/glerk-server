package com.glerk.core.common;

import com.glerk.core.dto.AutocompleteEmailDto;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.*;

@Getter
public class Node {
    private final Map<Character, Node> children;
    private final List<AutocompleteEmailDto> prefixData;
    private final List<AutocompleteEmailDto> suffixData;

    public Node() {
        this.children = new HashMap<>();
        this.prefixData = new ArrayList<>();
        this.suffixData = new ArrayList<>();
    }

    public void insertBigOneFirst(List<AutocompleteEmailDto> dataList, AutocompleteEmailDto data) {
        Comparator<AutocompleteEmailDto> comparator = Comparator.comparing(AutocompleteEmailDto::getUpdatedAt);

        int index = Collections.binarySearch(dataList, data, comparator);
        if (index < 0) {
            index = -index - 1;
        }

        dataList.add(index, data);
    }

    public void prefixDataInsert(String email, LocalDateTime time) {
        insertBigOneFirst(this.prefixData, new AutocompleteEmailDto(email, time));
    }

    public void suffixDataInsert(String email, LocalDateTime time) {
        insertBigOneFirst(this.suffixData, new AutocompleteEmailDto(email, time));
    }
}
