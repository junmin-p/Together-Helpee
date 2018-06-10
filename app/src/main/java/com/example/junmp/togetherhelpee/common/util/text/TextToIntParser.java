package com.example.junmp.togetherhelpee.common.util.text;

import java.util.Arrays;
import java.util.List;

public class TextToIntParser {
    private static final int NOT_VALID = -1;
    private int intValue;

    public TextToIntParser(String text) {

        this.intValue = getInt(text);
    }

    public int parse() throws ParseIntException {

        if (intValue == NOT_VALID)
            throw new ParseIntException();

        return intValue;
    }

    public void setIntValue(List<String> values) {
        for (String value : values) {
            intValue = getInt(value);
            if (intValue != NOT_VALID) {
                break;
            }
        }
    }

    private int getInt(String text) {

        try {
            String num = text.replaceAll("/[^0-9]/g","");
            return Integer.parseInt(num);
        } catch (Exception e) {
            if (isArrayContains(text , Arrays.asList("한개" , "하나" , "하개" , "일개" , "하게" , "한게" , "항게" , "항개" , "앙개"))) {
                return 1;
            } else if (isArrayContains(text , Arrays.asList("두개" , "둘" , "울" , "두게" , "우게"))) {
                return 2;
            } else if (isArrayContains(text , Arrays.asList("세개" , "새개" , "세게" , "새게" , "셋"))) {
                return 3;
            } else if (isArrayContains(text , Arrays.asList("네개" , "내개" , "네게" , "내게" , "넷"))) {
                return 4;
            } else if (isArrayContains(text , Arrays.asList("다섯" , "다서" , "다썻" , "다서" , "다석" , "다썩" , "다섯" , "다서"))) {
                return 5;
            } else {
                return NOT_VALID;
            }
        }
    }

    private static boolean isArrayContains(String text , List<String> list) {
        for (String toDayChar : list) {
            if (text.trim().contains(toDayChar)) {
                return true;
            }
        }

        return false;
    }
}
