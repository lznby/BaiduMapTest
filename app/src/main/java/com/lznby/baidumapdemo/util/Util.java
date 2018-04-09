package com.lznby.baidumapdemo.util;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lznby.baidumapdemo.json.Fire;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Util {
    //okhttp方式
    public static void sendRequestWithOkHttp(){
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            //.url("https://360.com")//原okhttp请求
                            .url("http://39.108.138.218/map.json")
                            //.url("http://192.168.1.89:8080/show/tsetServlet")
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    //showResponse(responseData);//原okhttp请求
                    //parseXMLWithPull(responseData);//Pull解析
                    //parseXMLWithSAX(responseData);//SAX解析parseJSONWithJSONObject(responseData);//JSONObject解析JSON
                    parseJSONWithGSON(responseData);//GSON解析JSON

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    //GSON解析JSON
    private static void parseJSONWithGSON(String jsonData){
        Gson gson = new Gson();
        Log.d("Fire",jsonData);
        List<Fire> testList = gson.fromJson(jsonData,new TypeToken<List<Fire>>(){}.getType());
        for(Fire fire : testList){
            Log.d("Fire","id is " + fire.getAddress());
            Log.d("Fire","id is " + fire.getArea_id());
            Log.d("Fire","id is " + fire.getDw_phone());
            Log.d("Fire","id is " + fire.getFz_department());
            Log.d("Fire","id is " + fire.getFzr_name());
            Log.d("Fire","id is " + fire.getFzr_phone());
            Log.d("Fire","id is " + fire.getId());
            Log.d("Fire","id is " + fire.getJcd_name());
            Log.d("Fire","id is " + fire.getJcd_phone());
            Log.d("Fire","id is " + fire.getJcd_type());
            Log.d("Fire","id is " + fire.getNode_id());
            Log.d("Fire","id is " + fire.getPoint());
            Log.d("Fire","id is " + fire.getPressure());
            Log.d("Fire","id is " + fire.getRemark());
            Log.d("Fire","id is " + fire.getState());
            Log.d("Fire","id is " + fire.getTime());

        }
    }
}
