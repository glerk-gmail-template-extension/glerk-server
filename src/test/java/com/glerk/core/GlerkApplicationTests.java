package com.glerk.core;

import com.glerk.core.common.LevenshteinDistance;
import com.glerk.core.common.Tokenizer;
import com.glerk.core.common.Trie;
import com.glerk.core.dto.AutocompleteEmailDto;
import com.glerk.core.service.AutocompleteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GlerkApplicationTests {

    @Autowired
    AutocompleteService autocompleteService;

    @Test
    void printEmailAutocomplete() {
        Trie trie = new Trie();
        Map<String, LocalDateTime> emails = new HashMap<>();

        emails.put("jieun@example.com", LocalDateTime.of(2024, Month.JULY, 30, 17, 0));
        emails.put("jiji@test.com", LocalDateTime.of(2024, Month.JULY, 29, 0, 0));
        emails.put("eji@website.org", LocalDateTime.of(2024, Month.JULY, 30, 17, 0));
        emails.put("toy3045@website.org", LocalDateTime.of(2024, Month.MAY, 30, 17, 0));
        emails.put("test@ji.org", LocalDateTime.of(2024, Month.APRIL, 1, 4, 0));
        emails.put("hojin@domain.com", LocalDateTime.of(2024, Month.APRIL, 1, 14, 0));

        for (String email : emails.keySet()) {
            LocalDateTime time = emails.get(email);

            trie.insertPrefix(email, time);

            List<String> tokenList = Tokenizer.emailTokenize(email);
            for (String emailToken : tokenList.subList(1, tokenList.size())) {
                trie.insertSuffix(emailToken, email, time);
            }
        }

        List<AutocompleteEmailDto> emailDtos = trie.query("ji");

        assertNotNull(emailDtos);
        assertEquals(4, emailDtos.size());

        List<String> expectedEmails = Arrays.asList(
                "jieun@example.com",
                "jiji@test.com",
                "eji@website.org",
                "hojin@domain.com"
        );

        for (int i = 0; i < expectedEmails.size(); i++) {
            assertEquals(expectedEmails.get(i), emailDtos.get(i).getEmail());
        }
    }

    @Test
    void levDistance() {
        String text = "tey3";
        List<String> compareTextList = Arrays.asList("toy3045@website.org", "test@ji.org", "rey3");
        Map<String, Integer> expectedResults = Map.of(
                "toy3045@website.org", 4,
                "test@ji.org", 2,
                "rey3", 1
        );

        for (String compare : compareTextList) {
            String localPart = compare.split("@")[0];
            int count = LevenshteinDistance.levDistance(text, localPart);

            assertEquals(expectedResults.get(compare), count,
                    "Levenshtein distance for '" + text + "' vs '" + compare + "' should be " + expectedResults.get(compare));
        }
    }
}
