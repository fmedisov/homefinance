package ru.medisov.home_finance.common.utils;

import ru.medisov.home_finance.common.model.TagModel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModelUtils {
    public static List<TagModel> parseTags(String tagsString) {
        List<TagModel> result = new ArrayList<>();

        if (tagsString == null || tagsString.length() < 2) {
            return result;
        }

        List<String> listOfTagStrings = Arrays.asList(tagsString.split(":"));
        listOfTagStrings.forEach(s -> result.add(new TagModel().setName(s)));

        return result;
    }

    // from format '2017-12-31'
    public static LocalDateTime parseDateTime(String dateString) {
        return LocalDateTime.parse(dateString + "T00:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}
