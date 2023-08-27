package com.example.travelofrecord.Function;

import android.annotation.SuppressLint;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Arrays;
import java.util.Locale;

public class GetTime {

    String TAG = "getTime";

    public String lastTime(String dateCreated) {

        String msg = null;

        long datePosted = Long.parseLong(dateCreated);
        long currentTime = System.currentTimeMillis();
        long lastTime = (currentTime - datePosted) / 1000;

        if (lastTime < 60) {
            msg = "방금 전";
        } else if ((lastTime /= 60) < 60) {
            msg = lastTime + "분 전";
        } else if ((lastTime /= 60) < 24) {
            msg = lastTime + "시간 전";
        } else if ((lastTime /= 24) < 7) {
            msg = lastTime + "일 전";
        } else if (lastTime < 14) {
            msg = "1주 전";
        } else if (lastTime < 21) {
            msg = "2주 전";
        } else if (lastTime < 28) {
            msg = "3주 전";
        } else if ((lastTime / 30) < 12) {
            msg = (lastTime / 30) + "달 전";
        } else {
            msg = (lastTime / 365) + "년 전";
        }

        return msg;
    }

    public Long getTime() {

        long currentTime = System.currentTimeMillis();

        return currentTime;
    }

    public String getFormatTime1(Long time) {

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        String formatTime = simpleDateFormat.format(time);

        return formatTime;
    }

    public String getFormatTime2(Long time) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMddHH");
        String formatTime = simpleDateFormat.format(time);

        return formatTime;
    }

    public String getFormatTime3(Long time) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMdd");
        String formatTime = simpleDateFormat.format(time);

        return formatTime;
    }

    public String getFormatTime4(Long time) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMdd");
        String formatTime = simpleDateFormat.format(time);
        String returnValue = "";

        if (formatTime.charAt(0) == '0') {

            returnValue = formatTime.substring(1, 2) + "월 " + formatTime.substring(2, 4) + "일";

        } else {

            returnValue = formatTime.substring(0, 2) + "월 " + formatTime.substring(2, 4) + "일";

        }

        return returnValue;
    }

    public String getFormatTime5(Long time) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
        String formatTime = simpleDateFormat.format(time);

        return formatTime;
    }

    public String getFormatTime6(Long time) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일 ");
        String formatTime = simpleDateFormat.format(time);

        return formatTime;
    }

    public String getFormatTime7(Long time) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy년 MM월");
        String formatTime = simpleDateFormat.format(time);

        String returnValue = "";

        if (formatTime.charAt(6) == '0') {

            returnValue = formatTime.substring(0, 4) + "년 " + formatTime.substring(7, 8) + "월";

        } else {

            returnValue = formatTime.substring(0, 4) + "년 " + formatTime.substring(6, 8) + "월";

        }

        return returnValue;

    }

    public String getDayOfWeek(String time) {

        String array[] = time.split("\\.");

        Log.d(TAG, "array.toString : " + Arrays.toString(array));
        Log.d(TAG, "array[0] : " + array[0] + " array[1] : " + array[1] + " array[2] : " + array[2]);

        int year = Integer.parseInt(array[0]);
        int month = Integer.parseInt(array[1]);
        int day = Integer.parseInt(array[2]);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDate date = LocalDate.of(year,month,day);

            DayOfWeek dayOfWeek = date.getDayOfWeek();
            return dayOfWeek.getDisplayName(TextStyle.FULL, Locale.KOREAN);

        }

        return "";

    }

    public boolean isSameDay(Long time) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
        String formatTime = simpleDateFormat.format(time);
        String currentTime = simpleDateFormat.format(System.currentTimeMillis());

        String array[] = formatTime.split("\\.");
        String array2[] = currentTime.split("\\.");

        if (array[2].equals(array2[2])) {
            return true;
        } else {
            return false;
        }

    }

    public boolean isSameMonth(Long time) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
        String formatTime = simpleDateFormat.format(time);
        String currentTime = simpleDateFormat.format(System.currentTimeMillis());

        String array[] = formatTime.split("\\.");
        String array2[] = currentTime.split("\\.");

        if (array[1].equals(array2[1])) {
            return true;
        } else {
            return false;
        }

    }

    public boolean isSameYear(Long time) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
        String formatTime = simpleDateFormat.format(time);
        String currentTime = simpleDateFormat.format(System.currentTimeMillis());

        String array[] = formatTime.split("\\.");
        String array2[] = currentTime.split("\\.");

        if (array[0].equals(array2[0])) {
            return true;
        } else {
            return false;
        }

    }


}
