package com.pepcus.appstudent.util;

import java.util.Comparator;

public class Sortbyname implements Comparator<String> {

    public int compare(String a, String b) {
        try {
            if (a.trim() != null && a.trim().length() > 2 && b.trim() != null && b.trim().length() > 2) {
                String compareOne = (a.toLowerCase().split(ApplicationConstants.COMMA_SEPARATOR)[1]
                        .replaceAll(ApplicationConstants.REGEX_FOR_SPACE, "")
                        + a.trim().toLowerCase().split(ApplicationConstants.COMMA_SEPARATOR)[2]
                                .replaceAll(ApplicationConstants.REGEX_FOR_SPACE, "")).trim();
                String compareTwo = (b.toLowerCase().split(ApplicationConstants.COMMA_SEPARATOR)[1]
                        .replaceAll(ApplicationConstants.REGEX_FOR_SPACE, "")
                        + b.trim().toLowerCase().split(ApplicationConstants.COMMA_SEPARATOR)[2]
                                .replaceAll(ApplicationConstants.REGEX_FOR_SPACE, "")).trim();
                return compareOne.compareTo(compareTwo);
            } else {
                return -1;
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
        return 0;
    }
}