package in.flowersong;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import SetterGetter.FlowerListDto;
import adapter.StickyDictiAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;
import util.AppController;
import util.Constant;
import util.Utils;

/**
 * Created by shaileshgaikwad on 6/30/15.
 */
public class DictionaryActivity extends BaseActivity implements SearchView.OnQueryTextListener {

    ArrayList<String> list;
    ArrayList<String> SubList;
    StickyDictiAdapter adt;
    private SearchView mSearchView;
    ProgressBar pb;
    StickyListHeadersListView stickyList;
    JsonArrayRequest jsonObjReq;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.dictionary, frameLayout);

        setActionBarTitle(DictionaryActivity.this, getString(R.string.Significance), getSupportActionBar());
        pb = (ProgressBar) findViewById(R.id.progressBar1);
        stickyList = (StickyListHeadersListView) findViewById(R.id.listdict);
        stickyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View arg1,
                                    int position, long arg3) {
                Intent i = new Intent(DictionaryActivity.this, Flow_desc.class);
                i.putExtra("STRING", parent.getItemAtPosition(position).toString());
                i.putExtra("STRINGFORURL", parent.getItemAtPosition(position).toString());
                startActivity(i);

            }
        });
        //call Asynk to get data
        if (Utils.haveNetworkConnection(DictionaryActivity.this)) {
            makeJsonArrayRequest(Constant.SIGNIFICANCES);
        } else {
            Toast.makeText(DictionaryActivity.this, getResources().getString(R.string.InternetConnection), Toast.LENGTH_SHORT).show();
        }

    }


    //sort array
    public void SortArray(CharSequence charSequence) {
        if (list != null && !list.isEmpty()) {
            adt.getFilter().filter(charSequence);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu resource
        getMenuInflater().inflate(R.menu.main_menu_m, menu);

        //info
        MenuItem info = menu.findItem(R.id.action_info);
        info.setVisible(false);

        //search
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchItem.setVisible(true);
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        mSearchView.setOnQueryTextListener(this);


        // Retrieve the share menu item
        MenuItem shareItem = menu.findItem(R.id.menu_share);
        shareItem.setVisible(false);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_search:
                mSearchView.setIconified(false);
                return true;
            case R.id.action_info:
                return true;

        }

        return false;
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        if (position != 2) {
            ChangeActivity(DictionaryActivity.this, position);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        try {
            SortArray(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * Method to make json array request where response starts with [
     */
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
                                    list = jsonParsing(response.toString());
                                    if (list != null && !list.isEmpty()) {
                                        ShowData(list,SubList);
                                    }
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(),
                                        getResources().getString(R.string.Volleyerror), Toast.LENGTH_SHORT).show();
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
                        getResources().getString(R.string.Volleyerror), Toast.LENGTH_SHORT).show();
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
    public ArrayList<String> jsonParsing(String jsonString) {
        try {
            SubList=new ArrayList<>();
            ArrayList list = new ArrayList<FlowerListDto>();
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject c = jsonArray.getJSONObject(i);
                FlowerListDto flowerListDto = new FlowerListDto();
                if (c.has("word")) {
                    list.add(c.getString("word"));
                }

                JSONArray occourance= c.getJSONArray("occurrences");
                for(int j =0;j<occourance.length();j++){
                    JSONObject Occ= occourance.getJSONObject(j);
                    if(Occ.has("significance")){
                        SubList.add(Occ.getString("significance"));
                    }
                }

            }
            return list;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }




    public void ShowData(final ArrayList<String> list, final ArrayList<String> Sublist){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    pb.setVisibility(View.GONE);
                    if (list != null && !list.isEmpty()) {
                        adt = new StickyDictiAdapter(DictionaryActivity.this, list,Sublist);
                        stickyList.setAdapter(adt);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (jsonObjReq != null) {
                AppController.getInstance().getRequestQueue().cancelAll(jsonObjReq);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        DictionaryActivity.this.finish();
    }
}
