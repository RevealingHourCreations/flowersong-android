package in.flowersong;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.nhaarman.listviewanimations.appearance.simple.SwingLeftInAnimationAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;

import SetterGetter.FlowerListDto;
import adapter.Search_Adapter;
import util.AppController;
import util.Constant;
import util.Typefaces;
import util.Utils;

/**
 * Created by shaileshgaikwad on 8/26/15.
 */

public class BaseActivity extends ActionBarActivity implements FragmentDrawer.FragmentDrawerListener {
    //actionbar init
    public static Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    float mActionBarHeight;
    RelativeLayout profileBox;
    private static boolean isLaunch = true;
    protected FrameLayout frameLayout;
    protected static int position = 0;
    EditText SearchEdt;
    ListView listView;
    JsonArrayRequest jsonObjReq;
    Button btnClearList;
    ArrayList<FlowerListDto> list;
    String records, size;
    int start = 0;
    Search_Adapter adt;
    String query;
    RecyclerView DrawerRecycler;
    TextView TxtPageCount;
    //  AnimatedDotsView red;
    boolean WorkingState = false;
    // DrawerLayout drawerLayout;
    ProgressBar pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.baseactivity);
        TxtPageCount = (TextView) findViewById(R.id.txtpagecount);
       // TxtPageCount.setTypeface(Typefaces.get(BaseActivity.this, "CharlotteSans_nn"));
        DrawerRecycler = (RecyclerView) findViewById(R.id.drawerList);
        btnClearList = (Button) findViewById(R.id.clear_list);
        btnClearList.setVisibility(View.INVISIBLE);
        frameLayout = (FrameLayout) findViewById(R.id.content_frame);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        profileBox = (RelativeLayout) findViewById(R.id.profileBox);
        SearchEdt = (EditText) findViewById(R.id.searchedittext);
        SearchEdt.setTypeface(Typefaces.get(BaseActivity.this, "magmaw04_light"));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setLogo(getResources().getDrawable(R.drawable.abc_btn_check_to_on_mtrl_000));
//        getSupportActionBar().setIcon(getResources().getDrawable(R.drawable.abc_btn_radio_material));

        pb = (ProgressBar) findViewById(R.id.progressBar2);
//        pb.getIndeterminateDrawable().setColorFilter(
//                getResources().getColor(R.color.ProgressBarColor),
//                android.graphics.PorterDuff.Mode.SRC_IN);
        // drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        setActionBarTitle(BaseActivity.this, getString(R.string.Flower_Song), getSupportActionBar());
        drawerFragment = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

// set width near about 80 % of screen width
        ViewGroup.LayoutParams params = drawerFragment.getView().getLayoutParams();
        params.width = AppController.DrawerWidth;
        drawerFragment.getView().setLayoutParams(params);


        final TypedArray styledAttributes = getTheme().obtainStyledAttributes(new int[]{android.R.attr.actionBarSize});
        mActionBarHeight = styledAttributes.getDimension(0, 0);

//        red = (AnimatedDotsView) findViewById(R.id.adv_2);
//        red.setVisibility(View.GONE);
        // red.startAnimation();
        // VisibleGoneDrawerList(true);

        listView = (ListView) findViewById(R.id.SearchlistView);

        profileBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    drawerFragment.CloseDra();
                    ChangeActivity(BaseActivity.this, 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //navigation drawer in %=done
        // scroll problem=resolved
        //remove quotes=done
        //if text not entered remove close icon if list not exits=done
        //in onresume show existing list if avilable=done
        //list below edittext=done
        //home in drawer=done
        //check internet connection=done

        //close time=done
        //preocessing dialog
        //image chapta=done
        //first close drawer then go to next activity=not done
        //change font of hint and edit text=done
        //change progress bar color
//search

        SearchEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //close virtual keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(SearchEdt.getWindowToken(),
                            InputMethodManager.RESULT_UNCHANGED_SHOWN);
                    //close drawer
                    //CloseDrawer();
                    if (SearchEdt.getText().toString() != null && !SearchEdt.getText().toString().equals("")) {
                        //call asynk tsk
                        if (Utils.haveNetworkConnection(BaseActivity.this)) {
                            SearchService(SearchEdt.getText().toString());
                        } else {
                            Toast.makeText(BaseActivity.this, getResources().getString(R.string.InternetConnection), Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(BaseActivity.this, "Please enter text.", Toast.LENGTH_LONG).show();
                        // SearchEdt.setError("Please enter text.");
                    }
                    //call activity
                    //ChangeActivity(BaseActivity.this, 0);

                    handled = true;
                }
                return handled;
            }
        });

        SearchEdt.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                try {
                    if (s.length() <= 0) {
                                /* hide keyboard */
                        ((InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE))
                                .toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
                        if (list != null && list.size() > 0) {

                            if (btnClearList.getVisibility() == View.INVISIBLE || btnClearList.getVisibility() == View.GONE) {
                                btnClearList.setVisibility(View.VISIBLE);
                            }

                        } else {
                            btnClearList.setVisibility(View.INVISIBLE);
                        }
                    } else {
                        btnClearList.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        SearchEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OncliclOfEdittext();
            }
        });
        btnClearList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

//                        ((InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE))
//                                .toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);

                    btnClearList.setVisibility(View.INVISIBLE);
                    SearchEdt.setText("");
                    TxtPageCount.setText("");
                    ClearData();
                    ((InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE))
                            .toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
                    // listView.setBackgroundColor(Color.parseColor("#00000000"));
                    //VisibleGoneDrawerList(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        //  drawerLayout.setDrawerListener(new RightMenuListener());
    }
//closer drawer method form baseactivity class
    @Override
    public void CloseDrawerList(boolean Cl) {
        if (!Cl) {
            OpenChapter();
        }
    }



    public void SearchService(String Word) {
        try {
            query = URLEncoder.encode(Word, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (list != null && !list.isEmpty()) {
                list.clear();

            }
            records = "";
            size = "";
            start = 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        //check internet connection then call api
        if (Utils.haveNetworkConnection(BaseActivity.this)) {
            makeJsonArrayRequestBaseAct("http://flowersong.in/flowers.json?query=" + query + "", false);
        } else {
            Toast.makeText(BaseActivity.this, getResources().getString(R.string.InternetConnection), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onDrawerItemSelected(View view, int position) {

    }

    public void CloseDrawer() {
        drawerFragment.CloseDra();
    }

//method used to set the font to action bar title
    public void setActionBarTitle(Context context, String title, ActionBar actionBar) {
        try {
            if (Typefaces.get(context, "magmaw04_light") != null) {
                SpannableString SpanString = new SpannableString(title);
                SpanString.setSpan(new util.TypefaceSpan(context, Typeface.createFromAsset(getAssets(), "fonts/magmaw04_light.ttf").toString()), 0, SpanString.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                actionBar.setTitle(SpanString);
            } else {
                actionBar.setTitle(title);
            }
        } catch (Exception e) {

            e.printStackTrace();

        }
    }

//change activity in UI thread for smmooth animation
    public void ChangeActivity(final Context context, final int ActivityNo) {
        Thread background = new Thread() {
            public void run() {

                try {
                    // Thread will sleep for 1 seconds
                    sleep(1 * 300);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            if (ActivityNo == 0) {
                                Intent i = new Intent(context, FlowerSong.class);
                                startActivity(i);
                                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                } else {
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                }
                                ((Activity) context).finish();
                                // context.this.finish();
                            }
                            if (ActivityNo == 1) {
                                Intent i = new Intent(context, BrowseFlower.class);
                                startActivity(i);
                                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                } else {
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                }
                                ((Activity) context).finish();
                                // BaseActivity.this.finish();
                            }
                            if (ActivityNo == 2) {
                                Intent i = new Intent(context, Flower_Significances.class);
                                i.putExtra("STRING", Constant.BIRTH_CEN_LIB);
                                startActivity(i);
                                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                } else {
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                }
                                ((Activity) context).finish();
                                // BaseActivity.this.finish();
                            }
                            if (ActivityNo == 3) {
                                Intent i = new Intent(context, About.class);
                                i.putExtra("STRING", Constant.COMP_WORK);
                                startActivity(i);
                                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                } else {
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                }
                                ((Activity) context).finish();
                                //BaseActivity.this.finish();
                            }


                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        background.start();

    }


    /**
     * Method to make json array request where response starts with [
     * */
    private void makeJsonArrayRequestBaseAct(String url,final boolean MoreData) {
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
                                    list = jsonParsingForSearch(response.toString());
                                    if (list != null && !list.isEmpty()) {
                                        //ShowData(list);
                                        ShowDataSearch(list);
                                    }else{
                                        Toast.makeText(BaseActivity.this, "No results found.", Toast.LENGTH_SHORT).show();
                                    }
                                }else{

                                        Toast.makeText(BaseActivity.this, "No results found.", Toast.LENGTH_SHORT).show();
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
    public ArrayList<FlowerListDto> jsonParsingForSearch(String jsonString) {
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

    //new word search...
    public void ClearData() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    //  btnClearList.setVisibility(View.INVISIBLE);
                    if (list != null && !list.isEmpty()) {
                        list.clear();
                    }

                    TxtPageCount.setText("");
                    listView.setAdapter(null);
                    VisibleGoneDrawerList(true);
                    if (pb.getVisibility() == View.VISIBLE) {
                        pb.setVisibility(View.GONE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void ShowDataSearch(final ArrayList<FlowerListDto> list) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    //  btnClearList.setVisibility(View.VISIBLE);
                    //listView.setBackgroundColor(Color.parseColor("#ffffff"));
                    if (list != null && !list.isEmpty()) {
                        VisibleGoneDrawerList(false);
                        if (listView.getVisibility() == View.GONE) {

                            listView.setVisibility(View.VISIBLE);

                        }
                        if (list != null && !list.isEmpty()) {

                            adt = new Search_Adapter(BaseActivity.this, list,listView,pb,query);
                            SwingLeftInAnimationAdapter animationAdapter = new SwingLeftInAnimationAdapter(adt);
                            animationAdapter.setAbsListView(listView);
                            listView.setAdapter(animationAdapter);
                        }
                        if (TxtPageCount.getVisibility() == View.INVISIBLE) {
                            TxtPageCount.setVisibility(View.VISIBLE);
                        }
                        if (pb.getVisibility() == View.VISIBLE) {
                            pb.setVisibility(View.GONE);

                            if (btnClearList.getVisibility() == View.INVISIBLE || btnClearList.getVisibility() == View.GONE) {
                                btnClearList.setVisibility(View.VISIBLE);
                            }
                        }

//                        TxtPageCount.setText(arr.size() + " of " + records + " results");
                    } else {
                        TxtPageCount.setText("");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void VisibleGoneDrawerList(final boolean VG) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (VG) {
                        listView.setAdapter(null);
                        if (list != null) {
                            list.clear();
                        }

                        if (listView.getVisibility() == View.VISIBLE) {
                            listView.setVisibility(View.GONE);

                        }
                        if (DrawerRecycler.getVisibility() == View.GONE || DrawerRecycler.getVisibility() == View.INVISIBLE) {
                            DrawerRecycler.invalidate();
                            DrawerRecycler.setVisibility(View.VISIBLE);
                        }
                    } else {
                        if (DrawerRecycler.getVisibility() == View.VISIBLE) {
                            DrawerRecycler.setVisibility(View.GONE);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void OpenChapter() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (listView.getVisibility() == View.VISIBLE) {
                        listView.setVisibility(View.GONE);
                        TxtPageCount.setVisibility(View.INVISIBLE);
                    }
                    if (DrawerRecycler.getVisibility() == View.GONE) {
                        DrawerRecycler.invalidate();
                        DrawerRecycler.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    // TODO: handle exception
                }
            }
        });

    }

    public void OncliclOfEdittext() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (list != null && !list.isEmpty() && list.size() >= 1) {
                        if (listView.getVisibility() == View.INVISIBLE || listView.getVisibility() == View.GONE) {
                            listView.setVisibility(View.VISIBLE);
                            TxtPageCount.setVisibility(View.VISIBLE);
                        }
                        if (DrawerRecycler.getVisibility() == View.VISIBLE) {
                            //DrawerRecycler.invalidate();
                            DrawerRecycler.setVisibility(View.GONE);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    // TODO: handle exception
                }
            }
        });
    }



    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();
//        try {
//            listView.setAdapter(null);
//            list.clear();
//            list = null;
//            arr.clear();
//            arr=null;
//        } catch (Exception e) {
//
//        }
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
        try {
            listView.setAdapter(null);
            list.clear();
            list = null;
        } catch (Exception e) {

        }
        try {
            if (jsonObjReq != null) {
                AppController.getInstance().getRequestQueue().cancelAll(jsonObjReq);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }


}
