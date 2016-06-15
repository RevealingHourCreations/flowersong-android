package in.flowersong;

import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import SetterGetter.FlowerListDto;
import SetterGetter.Significances;
import adapter.Signigicanece_ExpandableListAdapter;
import util.AppController;
import util.Utils;

public class Flower_Significances extends BaseActivity {
    private static ExpandableListView expandableListView;
    private static Signigicanece_ExpandableListAdapter adapter;
    ProgressBar pb;
    JsonArrayRequest jsonObjReq;
    ArrayList<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.signigicance_layout, frameLayout);
        pb = (ProgressBar) findViewById(R.id.progressBar);
        expandableListView = (ExpandableListView) findViewById(R.id.simple_expandable_listview);

        // Setting group indicator null for custom indicator
        expandableListView.setGroupIndicator(null);
        setActionBarTitle(Flower_Significances.this, getString(R.string.Significance), getSupportActionBar());
        // setItems();
        if (Utils.haveNetworkConnection(Flower_Significances.this)) {
            makeJsonArrayRequest("http://flowersong.in/significances.json");
        } else {
            Toast.makeText(Flower_Significances.this, getResources().getString(R.string.InternetConnection), Toast.LENGTH_SHORT).show();
        }

        setListener();

    }

    private void makeJsonArrayRequest(String url) {

        try {
            pb.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        }
        jsonObjReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {

                            try {
                                if (response != null) {
                                    jsonParsing(response.toString());
//                                    list = jsonParsing(response.toString());
//                                    if (list != null && !list.isEmpty()) {
//                                        ShowData(list, SubList);
//                                    }
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(),
                                        "", Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }

                        // hide the progress dialog
                        try {
                            pb.setVisibility(View.GONE);
                        } catch (Exception e) {
                            e.printStackTrace();
                            // TODO: handle exception
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        "", Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                try {
                    pb.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                    // TODO: handle exception
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }


    //parse search list responce
    public void jsonParsing(String jsonString) {
        try {
            ArrayList<String> header = new ArrayList<String>();
            List<Significances> child1 = null;
            // Hash map for both _significance_row and child
            HashMap<String, List<Significances>> hashMap = new HashMap<String, List<Significances>>();
            //SubList=new ArrayList<>();
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                // Array list for _significance_row
                child1 = new ArrayList<Significances>();
                JSONObject c = jsonArray.getJSONObject(i);
                FlowerListDto flowerListDto = new FlowerListDto();
                if (c.has("word")) {
                    header.add(c.getString("word"));
                }

                JSONArray occourance = c.getJSONArray("occurrences");
                for (int j = 0; j < occourance.length(); j++) {
                    // Array list for child items
                    Significances significances = new Significances();
                    JSONObject Occ = occourance.getJSONObject(j);
                    if (Occ.has("significance")) {
                        //SubList.add(Occ.getString("significance"));
                        significances.setUrl(Occ.getString("url"));
                        significances.setSignificance(Occ.getString("significance"));
                        if(Occ.has("name")) {
                            significances.setName(Occ.getString("name"));
                        }
                        child1.add(significances);
                    }
                    // Adding _significance_row and childs to hash map

                }
                hashMap.put(header.get(i), child1);
            }

            ShowData(hashMap,header);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void ShowData(  final HashMap<String, List<Significances>> hashMaps, final ArrayList<String> headers ) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    adapter = new Signigicanece_ExpandableListAdapter(Flower_Significances.this, headers, hashMaps);
                    expandableListView.setAdapter(adapter);
                    //   pb.setVisibility(View.GONE);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


//    // Setting headers and childs to expandable listview
//    void setItems() {
//
//        // Array list for _significance_row
//        ArrayList<String> header = new ArrayList<String>();
//
//        // Array list for child items
//        List<String> child1 = new ArrayList<String>();
//        List<String> child2 = new ArrayList<String>();
//        List<String> child3 = new ArrayList<String>();
//        List<String> child4 = new ArrayList<String>();
//
//        // Hash map for both _significance_row and child
//        HashMap<String, List<String>> hashMap = new HashMap<String, List<String>>();
//
//        // Adding headers to list
//        for (int i = 1; i < 5; i++) {
//            header.add("Group " + i);
//
//        }
//        // Adding child data
//        for (int i = 1; i < 5; i++) {
//            child1.add("Group 1  - " + " : Child" + i);
//
//        }
//        // Adding child data
//        for (int i = 1; i < 5; i++) {
//            child2.add("Group 2  - " + " : Child" + i);
//
//        }
//        // Adding child data
//        for (int i = 1; i < 6; i++) {
//            child3.add("Group 3  - " + " : Child" + i);
//
//        }
//        // Adding child data
//        for (int i = 1; i < 7; i++) {
//            child4.add("Group 4  - " + " : Child" + i);
//
//        }
//
//        // Adding _significance_row and childs to hash map
//        hashMap.put(header.get(0), child1);
//        hashMap.put(header.get(1), child2);
//        hashMap.put(header.get(2), child3);
//        hashMap.put(header.get(3), child4);
//
//        adapter = new Signigicanece_ExpandableListAdapter(Flower_Significances.this, header, hashMap);
//
//        // Setting adpater over expandablelistview
//        expandableListView.setAdapter(adapter);
//    }

    // Setting different listeners to expandablelistview
    void setListener() {

//        // This listener will show toast on group click
//        expandableListView.setOnGroupClickListener(new OnGroupClickListener() {
//
//            @Override
//            public boolean onGroupClick(ExpandableListView listview, View view,
//                                        int group_pos, long id) {
//
//                Toast.makeText(Flower_Significances.this,
//                        "You clicked : " + adapter.getGroup(group_pos),
//                        Toast.LENGTH_SHORT).show();
//                return false;
//            }
//        });

        // This listener will expand one group at one time
        // You can remove this listener for expanding all groups
        expandableListView
                .setOnGroupExpandListener(new OnGroupExpandListener() {

                    // Default position
                    int previousGroup = -1;

                    @Override
                    public void onGroupExpand(int groupPosition) {
                        if (groupPosition != previousGroup)

                            // Collapse the expanded group
                            expandableListView.collapseGroup(previousGroup);
                        previousGroup = groupPosition;
                    }

                });

//        // This listener will show toast on child click
//        expandableListView.setOnChildClickListener(new OnChildClickListener() {
//
//            @Override
//            public boolean onChildClick(ExpandableListView listview, View view,
//                                        int groupPos, int childPos, long id) {
//                Toast.makeText(
//                        Flower_Significances.this,
//                        "You clicked : " + adapter.getChild(groupPos, childPos),
//                        Toast.LENGTH_SHORT).show();
//                return false;
//            }
//        });
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        if (position != 2) {
            ChangeActivity(Flower_Significances.this, position);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            if (jsonObjReq != null) {
                AppController.getInstance().getRequestQueue().cancelAll(jsonObjReq);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            if (jsonObjReq != null) {
                AppController.getInstance().getRequestQueue().cancelAll(jsonObjReq);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //you may call the cancel() method but if it is not handled in doInBackground() method
        try {
            if (jsonObjReq != null) {
                AppController.getInstance().getRequestQueue().cancelAll(jsonObjReq);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
