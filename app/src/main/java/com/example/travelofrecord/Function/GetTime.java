package com.example.travelofrecord.Function;

import java.text.SimpleDateFormat;

public class GetTime {

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


}
