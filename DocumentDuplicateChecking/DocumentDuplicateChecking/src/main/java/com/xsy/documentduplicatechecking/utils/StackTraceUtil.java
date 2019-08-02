package com.xsy.documentduplicatechecking.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

public class StackTraceUtil {

    public static String getStackTrace(Throwable exception) {
        StringWriter sw = null;
        PrintWriter pw = null;
        try {
            sw = new StringWriter();
            pw = new PrintWriter(sw);
            exception.printStackTrace(pw);
            return sw.toString();
        } finally {
            if (pw != null) {
                pw.close();
            }
        }
    }
}
