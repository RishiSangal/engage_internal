package com.example.sew.helpers;

import android.util.Log;


/**
 * The type S log.
 */

public class SLog {
    enum LOG_TYPE {D, E, I, V}

    /**
     * D.
     *
     * @param tag the tag
     * @param msg the msg
     */
    public static void d(String tag, String msg) {
        printLargeLog(modifyTag(tag), msg, LOG_TYPE.D);
    }


    /**
     * E.
     *
     * @param tag the tag
     * @param msg the msg
     */
    public static void e(String tag, String msg) {
//            error(tag,msg);
        printLargeLog(modifyTag(tag), msg, LOG_TYPE.E);
    }

    /**
     * .
     *
     * @param tag the tag
     * @param msg the msg
     */
    public static void i(String tag, String msg) {
        printLargeLog(modifyTag(tag), msg, LOG_TYPE.I);


    }

    /**
     * V.
     *
     * @param tag the tag
     * @param msg the msg
     */
    public static void v(String tag, String msg) {
        printLargeLog(modifyTag(tag), msg, LOG_TYPE.V);
    }

    private static String modifyTag(String currTag) {
        return String.format("IMRKJ %s", currTag);
    }

    private static void printLargeLog(String tag, String content, LOG_TYPE logType) {
        try {
            int charLimit = 3800;
            if (content.length() > charLimit) {
                String subString = new String(content.substring(0, charLimit));
                String subString_1 = new String(content.substring(charLimit));
                if (content.contains("API_Request_Call"))
                    subString_1 = content.substring(0, content.indexOf(":")) + ": " + subString_1;
                printLog(tag, subString, logType);
                printLargeLog(tag, subString_1, logType);
            } else {
                printLog(tag, content, logType);
            }
        } catch (Error ignored) {

        } catch (Exception ignored) {
        }

    }

    private static void printLog(String tag, String content, LOG_TYPE logType) {
        switch (logType) {
            case D:
                Log.d(tag, content);
                break;
            case E:
                Log.e(tag, content);
                break;
            case I:
                Log.i(tag, content);
                break;
            case V:
                Log.v(tag, content);
                break;
        }
    }
}
