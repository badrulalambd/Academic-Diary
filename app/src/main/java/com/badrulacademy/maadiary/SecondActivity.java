package com.badrulacademy.maadiary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SecondActivity extends AppCompatActivity {

    public static String CAT = "";
    public static String TITLE = "";
    public static String DESCRIPTION = "";
    public static Bitmap MY_BITMAP = null;

    private int lastExpandedPosition = -1;

    private ExpandableListView expandableListView;
    TextView tv_title, tv_description;
    ImageView cover_image;

    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        expandableListView = findViewById(R.id.expandableListview_id);
        tv_title = findViewById(R.id.tv_title_id);
        tv_description = findViewById(R.id.tv_description_id);
        cover_image = findViewById(R.id.cover_image_id);

        tv_title.setText(TITLE);
        tv_description.setText(DESCRIPTION);
        //Set image after Checking, is there content in Bitmap or not?...
        if(MY_BITMAP != null)
            cover_image.setImageBitmap(MY_BITMAP);


        prepareListData();

        ////////////////////////////////////////////////////////
        // LISTENER and METHOD with ExpandableListview ...............
        //..............................
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                String groupName = listDataHeader.get(i);
                Toast.makeText(SecondActivity.this, "Group_Name : " + groupName, Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int i) {
                String groupName = listDataHeader.get(i);
                Toast.makeText(SecondActivity.this, groupName + " is Collapsed", Toast.LENGTH_SHORT).show();
            }
        });
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                String childName = listDataChild.get(listDataHeader.get(i)).get(i1);
                Toast.makeText(SecondActivity.this, "Child_Name : " + childName, Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int i) {
                if(lastExpandedPosition != -1 && lastExpandedPosition != i){
                    expandableListView.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = i;
            }
        });

    }




    ////////////////////////////////////////////////
    //member class as CustomAdapter...........
    //.....................................................
    private class CustomAdapter extends BaseExpandableListAdapter{

        Context context = SecondActivity.this;

        @Override
        public int getGroupCount() {
            return listDataHeader.size();
        }

        @Override
        public int getChildrenCount(int i) {
            return listDataChild.get(listDataHeader.get(i)).size();
        }

        @Override
        public Object getGroup(int i) {
            return listDataHeader.get(i);
        }

        @Override
        public Object getChild(int i, int i1) {
            return listDataChild.get(listDataHeader.get(i)).get(i1);
        }

        @Override
        public long getGroupId(int i) {
            return i;
        }

        @Override
        public long getChildId(int i, int i1) {
            return i1;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
            String headerText = (String) getGroup(i);
            if(view == null){
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.group_layout, null);
            }
            TextView textView = view.findViewById(R.id.group_textview);
            textView.setText(headerText);
            return view;
        }

        @Override
        public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
            String childText = (String) getChild(i, i1);
            if(view == null){
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.child_layout, null);
            }
            TextView textView = view.findViewById(R.id.child_textview);
            textView.setText(childText);
            return view;
        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return true;
        }
    }



    /////////////////////////////////////////////////
    // User-define function.......
    //....................................
    private void prepareListData(){

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "";

        if(CAT.equals("PLAY")){
            url = "https://alamtechbd.maakindergarten.com/play.json";
        }else if(CAT.equals("NURSERY")){
            url = "https://alamtechbd.maakindergarten.com/nursery.json";
        }else if(CAT.equals("ONE")){
            url = "https://alamtechbd.maakindergarten.com/one.json";
        }else if(CAT.equals("TWO")){
            url = "https://alamtechbd.maakindergarten.com/two.json";
        }else if(CAT.equals("THREE")){
            url = "https://alamtechbd.maakindergarten.com/three.json";
        }else if(CAT.equals("FOUR")){
            url = "https://alamtechbd.maakindergarten.com/four.json";
        }else if(CAT.equals("FIVE")){
            url = "https://alamtechbd.maakindergarten.com/five.json";
        }

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Toast.makeText(SecondActivity.this,"onResponse in SecondActivity", Toast.LENGTH_SHORT).show();

                        listDataHeader = new ArrayList<>();
                        listDataChild = new HashMap<>();

                        for(int i=0; i<response.length(); i++){
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String date = jsonObject.getString("date");
                                String subject = jsonObject.getString("subject");
                                String topic = jsonObject.getString("topic");
                                //String img_url = jsonObject.getString("img_url");

                                listDataHeader.add(date);
                                List<String> child = new ArrayList<>();
                                child.add(topic);
                                listDataChild.put(listDataHeader.get(i), child);


                            }catch (Exception e){

                            }

                        }
                        CustomAdapter customAdapter = new CustomAdapter();
                        expandableListView.setAdapter(customAdapter);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(jsonArrayRequest);
    }

}