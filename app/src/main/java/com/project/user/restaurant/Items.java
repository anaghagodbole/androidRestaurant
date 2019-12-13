package com.project.user.restaurant;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Items extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    private int pos;
    private ListView ls;
    private String name;
    private String address;
    private TextView tx1;
    private TextView tx2;
    ArrayList<HashMap<String, String>> ItemsList;

  // ListView ls=(ListView) findViewById(R.id.ls);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);
      //  Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);

         tx1=(TextView)findViewById(R.id.rname);
         tx2=(TextView)findViewById(R.id.raddr);

        //tx1.setText("Rastaurant name:"+name);
      //  tx2.setText("Restaurant address"+address);

         ls=(ListView)findViewById(R.id.ls);

         //tx1.setText("Restaurant name"+name);
      //  tx2.setText("Restaurant address"+address);

        ItemsList=new ArrayList<>();
        Bundle bundle=getIntent().getExtras();
        pos=bundle.getInt("pos");
        Log.e(TAG,"position"+pos);

        new GetItems().execute();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


               Intent intent=new Intent(getApplicationContext(),AddItems.class);
                Bundle bundle = new Bundle();
                bundle.putInt("pos",pos);
                intent.putExtras(bundle);
                startActivity(intent);
              //  startActivity(new Intent(Items.this,AddItems.class));

            }
        });


    }

    private class GetItems extends AsyncTask<Void, Void, Void>{

        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(Items.this,"Json Data is downloading",Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(Void... arg0){

            String url="  http://192.168.0.9:8000/resto/"+pos+"/restaurantitem/";
           // Log.e(TAG,"url"+url);

            HttpHandler sh=new HttpHandler();
            String jsonstr=sh.makeServiceCall(url);

           // Log.e(TAG,"list"+jsonstr);
            if(jsonstr!=null) {
                try {
                    JSONObject jobject = new JSONObject(jsonstr);
                    String name=jobject.getString("name");
                    String address=jobject.getString("address");
                    tx1.setText("Rastaurant name:"+name);
                    tx2.setText("Restaurant address"+address);
                    JSONArray arr = jobject.getJSONArray("Items");
                    for(int i=0;i<arr.length();i++){
                        JSONObject jsonObj = arr.getJSONObject(i);
                        String nameofitem=jsonObj.getString("nameofitem");
                        String price=jsonObj.getString("price");

                        HashMap<String,String> hm=new HashMap<>();
                        hm.put("nameofitem",nameofitem);
                        hm.put("price",price);

                        ItemsList.add(hm);


                    }

                    Log.e(TAG,"itemlis"+ItemsList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else{

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            super.onPostExecute(result);
            ListAdapter adapter = new SimpleAdapter(getBaseContext(),ItemsList, R.layout.listitem, new String[]{"nameofitem","price"}, new int[]{R.id.nameofitem,R.id.price});
            ls.setAdapter(adapter);



        }


    }

}
