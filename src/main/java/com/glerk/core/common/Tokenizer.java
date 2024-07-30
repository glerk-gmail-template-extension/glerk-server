package com.glerk.core.common;

import java.util.ArrayList;
import java.util.List;

public class Tokenizer {

    public static List<String> emailTokenize(String email) {
        String localPart = email.split("@")[0];

        List<String> nameTokenList = new ArrayList<>();
        for (int i = 0; i < localPart.length(); i++) {
            nameTokenList.add(localPart.substring(i));
        }

        return nameTokenList;
    }
}
