package com.example.travelofrecord.Function;

import java.util.Random;

public class RandomResult {

    public String getRandomResult() {

        Random ran = new Random();
        String result = "";
        int type;
        char c;
        int i;

        for (int j = 0; j < 6; j++) {

            type = ran.nextInt(2);

            if (type == 0) {

                i = 0;

                while(i < 65) {

                    i = ran.nextInt(91);

                }

                c = (char) i;
                result += c;

            } else {

                i = ran.nextInt(9);
                result += String.valueOf(i+1);

            }
        }

        return result;

    } // getRandomResult()

}
