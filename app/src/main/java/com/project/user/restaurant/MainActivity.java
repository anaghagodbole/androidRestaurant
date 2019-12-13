package com.project.user.restaurant;

import android.content.ClipData;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import com.project.user.restaurant.Items;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private ListView lv;
    private String TAG = MainActivity.class.getSimpleName();
    ArrayList<HashMap<String, String>> RestoList;
    private int[] myarrays=new int[20];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RestoList=new ArrayList<>();
        lv=(ListView)findViewById(R.id.list);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               startActivity(new Intent(MainActivity.this,Add.class));

            }
        });

        new GetRestaurants().execute();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              //  String clickedItem=(String) lv.getItemAtPosition(position);
                // Toast.makeText(MainActivity.this,clickedItem,Toast.LENGTH_LONG).show();
             //   Log.e(TAG,"position"+myarrays[position]);
                int pos=myarrays[position];
                Intent intent;
                intent = new Intent(getApplicationContext(),Items.class);
                Bundle bundle = new Bundle();
                bundle.putInt("pos",pos);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });

        }

    private class GetRestaurants extends AsyncTask<Void, Void, Void>{

        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MainActivity.this,"Json Data is downloading",Toast.LENGTH_LONG).show();
            }

        @Override
        protected Void doInBackground(Void... arg0){

            HttpHandler sh=new HttpHandler();
            String url="http://192.168.0.9:8000/resto/restaurant/";
            String jsonstr=sh.makeServiceCall(url);

            if(jsonstr!=null) {

                // JSONObject jobject=new JSONObject();
                // JSONArray Restaurant=jobject.getJSONArray("Restaurant");
                try {
                    JSONArray Restaurant = new JSONArray(jsonstr);

                    for (int i = 0; i < Restaurant.length(); i++) {
                        JSONObject jsonObj = Restaurant.getJSONObject(i);
                        String name = jsonObj.getString("name");
                        String address = jsonObj.getString("address");
                        int ID=jsonObj.getInt("id");
                        myarrays[i]=ID;

                        HashMap<String,String> r=new HashMap<>();
                        r.put("name",name);
                        r.put("address",address);
                        r.put("ID", String.valueOf(ID));





                        RestoList.add(r);

                    }
                } catch (Exception e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());

                }

            }
            else{
                Log.e(TAG, "Couldn't get json from server.");
            }

            return null;
        }
        @Override
        protected void onPostExecute(Void result){
            super.onPostExecute(result);
            ListAdapter adapter = new SimpleAdapter(getBaseContext(),RestoList, R.layout.list_item, new String[]{"name","ID"},
                    new int[]{R.id.name,R.id.ID});
            lv.setAdapter(adapter);


        }

        }





}

