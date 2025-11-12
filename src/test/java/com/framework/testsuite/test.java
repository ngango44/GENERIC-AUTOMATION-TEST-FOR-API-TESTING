package com.framework.testsuite;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class test {
    public static void main(String[] args) {
        System.out.println("ịweopgjơpeg");
        String regex = "\\@[a-zA-Z0-9\\.\\[\\]\\-\\_\\s]+\\@";
        String text = "contains:@sheet1.tc001.name[2]@,doggie";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        int count = 0;
        while (matcher.find()) {
            System.out.println(count);
            String dynamicValue = matcher.group();
            System.out.println(dynamicValue);
            String replaceValueFromDynamicValue = dynamicValue.replace("@", "").replace("@","").replace("["," ").replace("]","");;
            String[] splitDynamicValue = replaceValueFromDynamicValue.split("\\.");
            System.out.println(Arrays.toString(splitDynamicValue));
            count++;
        }
    }
}
