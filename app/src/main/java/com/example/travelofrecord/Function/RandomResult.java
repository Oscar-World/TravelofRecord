package com.example.travelofrecord.Function;

import com.example.travelofrecord.Data.PostData;
import com.example.travelofrecord.R;

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


    public PostData randomAd(int i) {

        String nickname = "";
        String writing = "";
        String profileImage = "";
        String postImage = "";
        int viewType = 1;

        if (i == 0) {

            nickname = "꽃봉우리";
            writing = "안녕하세요. 꽃봉우리 본점입니다. 저희는 돼지김치구이와 육회를 전문으로 하는 가게입니다.\n\n위치 : 수원시 팔달구 향교로 138\n전화번호 : 031-243-2937";
            profileImage = String.valueOf(R.drawable.ad1);
            postImage = String.valueOf(R.drawable.ad11);

        } else if (i == 1) {

            nickname = "대박집";
            writing = "최강 백반 대박집입니다.\n\n위치 : 수원시 팔달구 향교로 123\n전화번호 : 031-242-0311";
            profileImage = String.valueOf(R.drawable.ad2);
            postImage = String.valueOf(R.drawable.ad22);

        } else if (i == 2) {

            nickname = "영동돈까스";
            writing = "주차는 공영주차장을 이용해 주세요\n\n위치 : 수원시 팔달구 행궁로 106\n전화번호 : 031-222-8552";
            profileImage = String.valueOf(R.drawable.ad3);
            postImage = String.valueOf(R.drawable.ad33);

        } else {

            nickname = "동천홍";
            writing = "깨끗하고 깔끔한 분위기의 동천홍입니다.\n\n위치 : 수원시 팔달구 매산로130번길 12\n전화번호 : 031-224-2888";
            profileImage = String.valueOf(R.drawable.ad4);
            postImage = String.valueOf(R.drawable.ad44);

        }

        return new PostData(nickname, writing, profileImage, postImage, viewType);
    }


}
