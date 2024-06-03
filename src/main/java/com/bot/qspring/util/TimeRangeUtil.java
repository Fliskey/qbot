package com.bot.qspring.util;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeRangeUtil {

    public static void main(String[] args) {
        String dayRegex = "\\d+天";
        Pattern pattern = Pattern.compile(dayRegex);
        Matcher matcher = pattern.matcher("asdvna;lvn1234天");
        if(matcher.find()){
            String str = matcher.group().replace("天", "");
            int duringDay = Integer.parseInt(str);
            int a = duringDay;
        }


    }

    static public List<LocalTime> getTimeRange(String str) {
        List<LocalTime> ret = new ArrayList<>();

        int flag = -1;

        if (str.contains("开")) {
            flag = 1;
        }
        if (str.contains("关")) {
            if (flag == 1) {
                flag = -1;
            } else {
                flag = 0;
            }
        }

        if (flag == -1) {
            return new ArrayList<>();
        }

        String timeRegexPatternArr[] = {
                "\\d{1,2}:\\d{1,2}:\\d{1,2}",
                "\\d{1,2}:\\d{1,2}",
                "\\d{1,2}点半",
                "\\d{1,2}点\\d{1,2}",
                "\\d{1,2}点",
        };

        String timeFormatPatternArr[] = {
                "HH:mm:ss",
                "HH:mm",
                "HH:mm",
                "HH:mm",
                "HH:mm",
        };


        for (int patternIndex = 0; patternIndex < timeRegexPatternArr.length; patternIndex++) {
            String timeRegexPattern = timeRegexPatternArr[patternIndex];
            Pattern pattern = Pattern.compile(timeRegexPattern);
            Matcher matcher = pattern.matcher(str);
            while (matcher.find()) {
                String timeGroup = matcher.group();
                str = str.replaceFirst(timeGroup, "");

                if (timeRegexPattern.contains("半")) {
                    timeGroup = timeGroup.replace("点半", ":30");
                }
                if (timeRegexPattern.equals("\\d{1,2}点\\d{1,2}")) {
                    timeGroup = timeGroup.replace("点", ":");
                }
                if (timeRegexPattern.contains("点")) {
                    timeGroup = timeGroup.replace("点", ":00");
                }

                if (timeGroup.split(":")[0].length() == 1) {
                    timeGroup = "0" + timeGroup;
                }
                if (timeGroup.split(":")[1].length() == 1) {
                    timeGroup = timeGroup.split(":")[0] + ":0" + timeGroup.split(":")[1];
                }

                String timeFormatPattern = timeFormatPatternArr[patternIndex];
                DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern(timeFormatPattern);
                LocalTime localTime = LocalTime.parse(timeGroup, timeFormat);
                ret.add(localTime);
            }
        }
        if (ret.size() != 2) {
            return new ArrayList<>();
        }

        if (ret.get(0).isBefore(ret.get(1)) && flag == 0 ||
                ret.get(1).isBefore(ret.get(0)) && flag == 1) {
            LocalTime temp = ret.get(0);
            ret.set(0, ret.get(1));
            ret.set(1, temp);
        }

        return ret;
    }
}
