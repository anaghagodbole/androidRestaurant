package com.project.user.restaurant;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class Add extends AppCompatActivity {


    private String TAG = MainActivity.class.getSimpleName();
    private EditText name;
    private EditText address;
    private Button btn;
    String rname, raddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);


        name = (EditText) findViewById(R.id.restoname);
        address = (EditText) findViewById(R.id.restoaddr);
        btn = (Button) findViewById(R.id.submit);

        }

     public void senddatatoserver(View v) {

         rname = name.getText().toString();
         raddress = address.getText().toString();

         JSONObject post_dict = new JSONObject();

         try {
             post_dict.put("abc", rname);
             post_dict.put("xyz", raddress);
         } catch (JSONException e) {
             e.printStackTrace();
         }
         if (post_dict.length() > 0) {
             new SendJsonDataToServer().execute(String.valueOf(post_dict));
         }

     }

    class  SendJsonDataToServer extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... params) {

            String JsonResponse=null;
            String JsonData=params[0];

            HttpURLConnection urlConnection=null;
            BufferedReader br=null;
            try{
                URL url=new URL("http://192.168.0.9:8000/resto/restaurantusingp/");
                urlConnection =(HttpURLConnection)url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type","application/json");
                urlConnection.setRequestProperty("Accept","application/json");

                Writer writer=new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream()));
                writer.write(JsonData);
                writer.close();

                InputStream inputStream=urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if(inputStream==null){
                    return null;

                }

                br=new BufferedReader(new InputStreamReader(inputStream));
                String inputLine;
                while((inputLine=br.readLine())!=null)

                buffer.append(inputLine+"\n");
                JsonResponse=buffer.toString();

                Log.i(TAG,JsonResponse);
                return JsonResponse;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if(urlConnection!=null){
                    urlConnection.disconnect();
                }
                if(br!=null){
                    try{
                        br.close();
                    }catch(final IOException e){

                        Log.e(TAG, "Error closing stream", e);

                    }
                }
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
        }

    }

    }










