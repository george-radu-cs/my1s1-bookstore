package com.georgeradu.bookstore.utils;

import java.util.ArrayList;
import java.util.List;

public class StringCombinationGenerator {
    // given an input with multiple whitespaces, will generate all possible combinations of words with all possible
    // number of lengths. Example "one two three" will generate: "one", "two", "three", "one two", "one three",
    // "two three", "one two three"
    public static List<String> generateWordsCombinations(String input) {
        String[] words = input.split("\\s+"); // split input into words
        List<String> combinations = new ArrayList<>(); // will be filled with all combinations in the helper function
        generateCombinationsHelper(combinations, words, 0, new StringBuilder());
        return combinations;
    }

    private static void generateCombinationsHelper(
            List<String> combinations, String[] words, int index, StringBuilder current
    ) {
        if (index == words.length) {
            combinations.add(current.toString().trim());
            return;
        }
        generateCombinationsHelper(combinations, words, index + 1, current.append(words[index]).append(" "));
        current.setLength(current.length() - words[index].length() - 1);
        generateCombinationsHelper(combinations, words, index + 1, current);
    }
}