package com.example.travelofrecord.Function;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GetAdress {

    String TAG = "주소 얻기";
    String nowAddr;

    // Geocoder - 위도, 경도 사용해서 주소 구하기.
    public String getAddress(Context context, double lat, double lng) {
        nowAddr ="현재 위치를 확인 할 수 없습니다.";
        Geocoder geocoder = new Geocoder(context, Locale.KOREA);
        List<Address> address;

        try
        {
            if (geocoder != null)
            {
                address = geocoder.getFromLocation(lat, lng, 1);
                if (address != null && address.size() > 0)
                {
                    nowAddr = address.get(0).getAddressLine(0).toString();
                    Log.d(TAG, "전체 주소 : " + nowAddr);

                }
            }
        }
        catch (IOException e)
        {
            Toast.makeText(context, "주소를 가져 올 수 없습니다.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        return nowAddr;
    } // getAddress


    public String editAddress1234(String location) {

        String address = null;
        String[] addressArray = location.split(" ");

        if (addressArray.length > 4) {
            address = addressArray[1] + " " + addressArray[2] + " " + addressArray[3] + " " + addressArray[4];
        } else if (addressArray.length > 3) {
            address = addressArray[1] + " " + addressArray[2] + " " + addressArray[3];
        } else if (addressArray.length > 2) {
            address = addressArray[1] + " " + addressArray[2];
        } else if (addressArray.length > 1) {
            address = addressArray[0] + " " + addressArray[1];
        } else {
            address = addressArray[0];
        }


        return address;

    }

    public String editAddress24(String location) {

        String address = null;
        String[] addressArray = location.split(" ");

        if (addressArray.length > 4) {
            address = addressArray[2] + " " + addressArray[4];
        } else if (addressArray.length > 3) {
            address = addressArray[2] + " " + addressArray[3];
        } else if (addressArray.length > 2) {
            address = addressArray[1] + " " + addressArray[2];
        } else if (addressArray.length > 1) {
            address = addressArray[0] + " " + addressArray[1];
        } else {
            address = addressArray[0];
        }



        return address;

    }

    public String editAddress13(String location) {

        String[] address = location.split(" ");

        String editAdrress = "";

        if (address.length > 3) {
            editAdrress = address[1] + " " + address[3];
        } else if (address.length > 2) {
            editAdrress = address[1] + " " + address[2];
        } else {
            editAdrress = address[0] + " " + address[1];
        }

        return editAdrress;

    }

}
