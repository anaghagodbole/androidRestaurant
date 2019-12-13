package com.project.user.restaurant;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class AddItems extends AppCompatActivity {

    EditText itemname,itemprice;
    Button btn;
    private int pos;
    private String TAG=null;
    String item,price;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_items);

        itemname = (EditText) findViewById(R.id.itemname);
        itemprice = (EditText) findViewById(R.id.itemprice);
        btn = (Button) findViewById(R.id.add);

        Bundle bundle = getIntent().getExtras();
        pos = bundle.getInt("pos");
        Log.e(TAG, "position" + pos);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                senddatatoserver();
            //    Toast.makeText()
            }
        });


    }

        public void senddatatoserver(){

        item=itemname.getText().toString();
        price=itemprice.getText().toString();

            JSONObject post_item=new JSONObject();

            try {

                post_item.put("abc",item);
                post_item.put("xyz",price);

            }catch (JSONException e) {
                e.printStackTrace();
                }
            if(post_item.length()>0) {
                new  SendJsonitemDataToServer().execute(String.valueOf(post_item));
            }
            }

   class SendJsonitemDataToServer extends AsyncTask<String,String,String>{

       @Override
       protected String doInBackground(String... params){
           String JSONResponse=null;
           String JSONData=params[0];

           HttpURLConnection conn=null;
           BufferedReader br=null;

           try{
               URL url=new URL("http://192.168.0.9:8000/resto/"+pos+"/restaurantusingid/");
               conn=(HttpURLConnection)url.openConnection();
               conn.setDoOutput(true);
               conn.setRequestMethod("POST");
               conn.setRequestProperty("Content-Type","application/json");
               conn.setRequestProperty("Accept","application/json");

               Writer writer=new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
               writer.write(JSONData);
               writer.close();

               InputStream inputstream=conn.getInputStream();
               StringBuffer buffer=new StringBuffer();
               if(inputstream==null){
                   return null;
               }

               br=new BufferedReader(new InputStreamReader(inputstream));
               String inputLine;
               while((inputLine=br.readLine())!=null)

                   buffer.append(inputLine+"\n");
               JSONResponse=buffer.toString();
               Log.e(TAG,"hello");
               Log.e(TAG,JSONResponse);
               return JSONResponse;


           } catch (MalformedURLException e) {
               e.printStackTrace();
           } catch (IOException e) {
               e.printStackTrace();
           }finally {


                   if(conn!=null){
                       conn.disconnect();
                   }

                   if(br!=null){
                       try{
                           br.close();
                       }

                        catch (IOException e) {
                           e.printStackTrace();
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

