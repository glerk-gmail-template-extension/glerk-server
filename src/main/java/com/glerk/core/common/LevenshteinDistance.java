package com.glerk.core.common;

public class LevenshteinDistance {

    public static int levDistance(String text1, String text2) {
        int rowLen = text1.length() + 1;
        int colLen = text2.length() + 1;
        int[][] mat = new int[rowLen][colLen];

        for (int i = 0; i < rowLen; i++) {
            mat[i][0] = i;
        }

        for (int j = 0; j < colLen; j++) {
            mat[0][j] = j;
        }

        for (int i = 1; i < rowLen; i++) {
            for (int j = 1; j < colLen; j++) {
                char a = text1.charAt(i - 1);
                char b = text2.charAt(j - 1);

                int match = (a == b) ? 0 : 1;
                int replace = mat[i - 1][j - 1] + match;
                int insert = mat[i - 1][j] + 1;
                int delete = mat[i][j - 1] + 1;

                mat[i][j] = Math.min(replace, Math.min(insert, delete));
            }
        }

        return mat[rowLen - 1][colLen - 1];
    }
}
