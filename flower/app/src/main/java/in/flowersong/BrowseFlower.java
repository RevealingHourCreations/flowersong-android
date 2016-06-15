package in.flowersong;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.nhaarman.listviewanimations.appearance.simple.SwingLeftInAnimationAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import SetterGetter.FlowerListDto;
import adapter.BrowseFlower_Adapter;
import util.AppController;
import util.Constant;
import util.Utils;

/**
 * Created by shailb on 3/11/15.
 */
public class BrowseFlower extends BaseActivity implements OnDismissCallback {
    ListView ListFlower;
    BrowseFlower_Adapter adapter;
    private static final int INITIAL_DELAY_MILLIS = 300;
    ArrayList<FlowerListDto> list;
    JsonArrayRequest jsonObjReq;
    ProgressBar pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.browser_flower, frameLayout);
        pb = (ProgressBar) findViewById(R.id.progressBar1);
        ListFlower = (ListView) findViewById(R.id.FlowerList);
        setActionBarTitle(BrowseFlower.this, "Browse", getSupportActionBar());

//bottom jump animation
//        SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(new SwipeDismissAdapter(adapter, this));
//        swingBottomInAnimationAdapter.setAbsListView(listView);
//        assert swingBottomInAnimationAdapter.getViewAnimator() != null;
//        swingBottomInAnimationAdapter.getViewAnimator().setInitialDelayMillis(INITIAL_DELAY_MILLIS);
//        ListFlower.setAdapter(swingBottomInAnimationAdapter);


//list view animation
//        AlphaAnimationAdapter
//        ScaleInAnimationAdapter
//        SwingBottomInAnimationAdapter
//        SwingLeftInAnimationAdapter
//        SwingRightInAnimationAdapter


        //call Asynk to get data
        if (Utils.haveNetworkConnection(BrowseFlower.this)) {
            makeJsonArrayRequest(Constant.BROWSE_FLOWER);
        } else {
            Toast.makeText(BrowseFlower.this, getResources().getString(R.string.InternetConnection), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onDismiss(ViewGroup viewGroup, int[] ints) {

    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        if (position != 1) {
            ChangeActivity(BrowseFlower.this, position);
        }
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
                                        ShowData(list);
                                    }else{
                                        Toast.makeText(BrowseFlower.this, "No results found.", Toast.LENGTH_SHORT).show();
//                                ClearData();
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
    public ArrayList<FlowerListDto> jsonParsing(String jsonString) {
        try {
            ArrayList list = new ArrayList<FlowerListDto>();
            JSONArray jsonArray = new JSONArray(jsonString);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject c = jsonArray.getJSONObject(i);

                FlowerListDto flowerListDto = new FlowerListDto();
                if (c.has("thumbnail_url")) {
                    flowerListDto.setThumbnail_url(c.getString("thumbnail_url"));
                }
                if (c.has("name")) {
                    flowerListDto.setName(c.getString("name"));
                }


                JSONArray slug = c.getJSONArray("_slugs");
                for (int j = 0; j < slug.length(); j++) {
                    flowerListDto.setSlugs(slug.get(j).toString());
                }


                list.add(flowerListDto);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public void ShowData(final ArrayList<FlowerListDto> list) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    //   pb.setVisibility(View.GONE);
                    if (list != null && !list.isEmpty()) {

                        adapter = new BrowseFlower_Adapter(BrowseFlower.this, list, ListFlower, pb);
                        SwingLeftInAnimationAdapter animationAdapter = new SwingLeftInAnimationAdapter(adapter);
                        animationAdapter.setAbsListView(ListFlower);
                        ListFlower.setAdapter(animationAdapter);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
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
