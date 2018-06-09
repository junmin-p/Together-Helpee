package com.example.junmp.togetherhelpee.common.util.text;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class TextParser {

    private static final int NOT_VALID = -1;
    private int day;
    private int time;

    public TextParser(String text) {
        this.day = getDay(text);
        this.time = getTime(text);
    }

    public  Date parseDate() throws ParseDateException, ParseTimeException {

        if (day == NOT_VALID) {
            throw new ParseDateException();
        }

        if (time == NOT_VALID)
            throw new ParseTimeException();

        Calendar cal = new GregorianCalendar(Locale.KOREA);
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_YEAR, day); // 하루를 더한다.
        cal.add(Calendar.HOUR, time); // 시간을 더한다
        return cal.getTime();
    }

    public void setDateText(ArrayList<String> values) {
        for (String value : values) {
            this.day = getDay(value);
            if (day != NOT_VALID) {
                break;
            }
        }
    }

    public void setTimeText(ArrayList<String> values) {
        for (String value : values) {
            time = getTime(value);
            if (time != NOT_VALID) {
                break;
            }
        }
    }



    private static int getDay(String text) {
        int day = NOT_VALID;


        if (isToday(text)) {
            day = 0;
        } else if (isTomorrow(text)) {
            day = 1;
        } else if (is2st(text)) {
            day = 2;
        } else if (is3th(text)) {
            day = 3;
        }

        return day;
    }

    /**
     * am , pm 여부
     * @param text
     * @return
     */
    private  int partUnit(String text) {
        if (isAm(text))
            return 1;
        else if (isPm(text))
            return 2;
        else
            return NOT_VALID;
    }

    private static int getTime(String text) {

        int time = NOT_VALID;

        try {
            time = Integer.parseInt(text.split("시")[0]);
            if (isPm(text))
                return time + 12;
            else
                return time;
        } catch (Exception ex) {
            if (is1oClock(text))
                time =  1;
            else if (is2oClock(text))
                time =  2;
            else if (is3oClock(text))
                time =  3;
            else if (is4oClock(text))
                time =  4;
            else if (is5oClock(text))
                time =  5;
            else if (is6oClock(text))
                time =  6;
            else if (is7oClock(text))
                time =  7;
            else if (is8oClock(text))
                time =  8;
            else if (is9oClock(text))
                time =  9;
            else if (is10oClock(text))
                time =  10;
            else if (is11oClock(text))
                time =  11;
            else if (is12oClock(text))
                time =  12;

            if (isPm(text))
                return time + 12;
            else
                return time;
        }


    }

    private static boolean isAm (String text) {
        return isArrayContains(text , Arrays.asList("오전" , "모전" , "아침" , "마침" , "아치" , "오저"));
    }

    private static boolean isPm (String text) {
        return isArrayContains(text , Arrays.asList("오후" , "오우" , "저녁" , "모후" , "저녕" , "저넝" , "저녘"));
    }

    private static boolean is1oClock(String text) {
        return isArrayContains(text , Arrays.asList("한시" , "하시" , "한씨"));
    }

    private static boolean is2oClock(String text) {
        return isArrayContains(text , Arrays.asList("두시"));
    }

    private static boolean is3oClock(String text) {
        return isArrayContains(text , Arrays.asList("세시" , "새시"));
    }

    private static boolean is4oClock(String text) {
        return isArrayContains(text , Arrays.asList("내시" , "네시"));
    }

    private static boolean is5oClock(String text) {
        return isArrayContains(text , Arrays.asList("다서씨" , "다서시" , "다섯시" , "다섯씨"));
    }

    private static boolean is6oClock(String text) {
        return isArrayContains(text , Arrays.asList("여서씨" , "여섯시" , "여섯씨"));
    }

    private static boolean is7oClock(String text) {
        return isArrayContains(text , Arrays.asList("일곱시" , "일고씨" , "일곱씨" , "일고시"));
    }

    private static boolean is8oClock(String text) {
        return isArrayContains(text , Arrays.asList("여덟시" , "여덜시" , "여덜씨" , "여덟씨"));
    }

    private static boolean is9oClock(String text) {
        return isArrayContains(text , Arrays.asList("아홉시" , "아홉씨" , "아호시" , "아호씨" , "아옵시" , "아옵씨" , "아으씨" , "아호씨" , "아웁씨" , "아우씨" , "아웁시" , "아우시"));
    }

    private static boolean is10oClock(String text) {
        return isArrayContains(text , Arrays.asList("열시" , "열씨"));
    }

    private static boolean is11oClock(String text) {
        return isArrayContains(text , Arrays.asList("여란시" , "열1시" , "열1씨" , "열안시" , "열안씨" , "여라시" , "열한시" , "여란씨" , "열한씨"));
    }

    private static boolean is12oClock(String text) {
        return isArrayContains(text , Arrays.asList("열두시" , "열두씨" , "여두시" , "여뚜시"));
    }

    private static boolean is3th(String text) {
        return isArrayContains(text , Arrays.asList("글피" , "글페" , "글패" , "믈피" , "믈페" , "믈패"  , "나흘뒤" , "나을뒤" , "사밀뒤" , "사밀후" , "사미루"));
    }

    private static boolean is2st(String text) {
        return isArrayContains(text , Arrays.asList("모레" , "모래" , "뭐래" , "뭐레" , "무레" , "무래" , "오래" , "오레" , "이틀후" , "이틀뒤" , "이틀" , "이일뒤" , "이일후"));
    }

    private static boolean isTomorrow(String text) {
        return isArrayContains(text , Arrays.asList("내일" , "나일" , "네일" , "내밀" , "네밀" , "나일" , "나밀" , "하루뒤"  , "일일뒤"));
    }

    private static boolean isToday(String text) {
        return isArrayContains(text , Arrays.asList("오늘" , "오날" , "오널" , "모늘" , "모날" , "모널" , "일일"));
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
