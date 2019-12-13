package com.project.user.restaurant;
import android.util.Log;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import static android.content.ContentValues.TAG;

public class HttpHandler {

    HttpHandler(){

    }

    public String makeServiceCall(String reqUrl){
        String response = null;
        try{
            URL url=new URL(reqUrl);
            HttpURLConnection conn=(HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            InputStream in=new BufferedInputStream(conn.getInputStream());
            response=convertStreamToString(in);
            }
         catch (Exception e){
            Log.e(TAG, "Exception: " + e.getMessage());
         }

         return response;
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader=new BufferedReader(new InputStreamReader(is));
        StringBuilder sb=new StringBuilder();

        String line;
        try{
            while( (line=reader.readLine())!=null){
                sb.append(line).append("\n");

            }
        }catch(IOException e) {
          e.printStackTrace();
        }finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }
}

