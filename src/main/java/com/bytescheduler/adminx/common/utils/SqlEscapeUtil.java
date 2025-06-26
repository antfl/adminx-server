package com.bytescheduler.adminx.common.utils;


/**
 * @author gavinhaydy
 */
public class SqlEscapeUtil {
    public static String escapeLike(String input) {
        if (input == null) return null;
        return input.replace("\\", "\\\\")
                .replace("_", "\\_")
                .replace("%", "\\%");
    }
}
