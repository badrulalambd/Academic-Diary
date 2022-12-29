package com.badrulacademy.maadiary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    ListView listView;

    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
    HashMap<String, String> hashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove TitleBar......
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove NotificationBar....
        /*getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        setContentView(R.layout.activity_main);


        listView = findViewById(R.id.listview_id);

        crateTable();
    }



    //...............................
    //Member class as MyAdapter class....................
    private class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            LayoutInflater inflater = getLayoutInflater();
            View myView = inflater.inflate(R.layout.list_layout, null);

            TextView textView_cat = myView.findViewById(R.id.textview_category_id);
            TextView textView_title = myView.findViewById(R.id.textview_title_id);
            TextView textView_description = myView.findViewById(R.id.textview_description_id);
            ImageView imageView = myView.findViewById(R.id.imageview_id);
            LinearLayout list_item = myView.findViewById(R.id.layout_list_id);

            hashMap = arrayList.get(position);

            String cat = hashMap.get("cat");
            String title = hashMap.get("title");
            String description = hashMap.get("description");
            String img_url = hashMap.get("img_url");

            Picasso.get()
                    .load(img_url)
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(imageView);
            textView_cat.setText(cat);
            textView_title.setText(title);
            textView_description.setText(description);

            //...........................................
            //It sets background color for Category .....
            Random rnd = new Random();
            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            textView_cat.setBackgroundColor(color);

            //...............................
            //Set Listener for every layout_item.
            list_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SecondActivity.CAT = cat;
                    SecondActivity.TITLE = title;
                    SecondActivity.DESCRIPTION = description;

                    //............................
                    // With this method Images is converted into Bitmap
                    Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
                    SecondActivity.MY_BITMAP = bitmap;

                    startActivity(new Intent(MainActivity.this, SecondActivity.class));
                }
            });


            return myView;
        }
    }




    ///////////////////////////////////////////
    ///////////////////////////////////////////
    //.........................................
    //createTable,,User define function createTable......
    public void crateTable(){
        // Instantiate the RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "https://alamtechbd.maakindergarten.com/school.json";
        //Request an array response from the provided url......
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Toast.makeText(MainActivity.this, "onResponse", Toast.LENGTH_SHORT).show();
                        try {
                            for(int i=0; i<response.length(); i++){
                                JSONObject jsonObject = response.getJSONObject(i);
                                String cat = jsonObject.getString("cat");
                                String title = jsonObject.getString("title");
                                String description = jsonObject.getString("description");
                                String img_url = jsonObject.getString("img_url");

                                hashMap = new HashMap<>();
                                hashMap.put("cat", cat);
                                hashMap.put("title", title);
                                hashMap.put("description", description);
                                hashMap.put("img_url", img_url);
                                arrayList.add(hashMap);
                            }
                            //........................................
                            //Set adapter in the listView...........
                            MyAdapter myAdapter = new MyAdapter();
                            listView.setAdapter(myAdapter);

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Exception is occuring", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }


}