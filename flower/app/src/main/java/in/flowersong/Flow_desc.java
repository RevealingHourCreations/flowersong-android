package in.flowersong;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.ShareActionProvider;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import SetterGetter.Flower_Desc;
import adapter.Flower_Desc_adapter;
import util.AppController;
import util.ContentItem;
import util.Typefaces;
import util.Utils;

public class Flow_desc extends observable.BaseActivity implements ObservableScrollViewCallbacks, Animation.AnimationListener {
    private static final String TAG = Flow_desc.class.getSimpleName();

    private ObservableListView listView;
    private Flower_Desc_adapter listAdapter;
    private List<Flower_Desc> feedItems;
    JsonObjectRequest jsonObjReq;
    ProgressBar pb;
    Flower_Desc MainDesc;
    FrameLayout NextPrevButton;
    ActionBar ab;
    Animation animSlideUp, animSideDown;
    String UrlString;
    String ActionBarName;
    String ServiceUrl;
    boolean FromSignfic = false;
    TextView BtnPrev, BtnNext;
    public static FrameLayout ChaptDesc;
    // Keep reference to the ShareActionProvider from the menu
    private ShareActionProvider mShareActionProvider;
    private final ArrayList<ContentItem> mItems = getSampleContent();
    public static String ShareLink = "http://flowersong.in/flowers";
    TextView txtfamily, txtothernam, txtclolorM, txtpetails, txtsize, txtcultivation, txtclimate, txttype,txtrows;
    TextView txt1, txt2, txt3, txt4, txt5, txt6, txt7, txttypetxt,txtrowstxt;
    RelativeLayout firstItem, typerelative, othernamerel, familyrelat, colourrelat, petailrela, sizerelat, cultivationrel, climaterelat,rowsrelative;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flower_desc_layout);

        Bundle gt = getIntent().getExtras();
        UrlString = gt.getString("URLNAME");
        ActionBarName = gt.getString("ACIONBARNAME");
        FromSignfic = gt.getBoolean("FROMSIGNI");


        if (UrlString != null) {
            try {
                String[] split;

                if (UrlString.contains("#")) {
                    split = UrlString.split("#");
                    if (!FromSignfic) {
                        ShareLink = "http://flowersong.in/flowers/" + split[0];
                        ServiceUrl = "http://flowersong.in/flowers/" + split[0] + ".json";
                    } else {
                        ShareLink = "http://flowersong.in" + split[0];
                        ServiceUrl = "http://flowersong.in" + split[0] + ".json";
                    }
                } else {
                    if (!FromSignfic) {
                        ShareLink = "http://flowersong.in/flowers/" + UrlString;
                        ServiceUrl = "http://flowersong.in/flowers/" + UrlString + ".json";
                    } else {
                        ShareLink = "http://flowersong.in" + UrlString;
                        ServiceUrl = "http://flowersong.in" + UrlString + ".json";
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        txtrows= (TextView) findViewById(R.id.txtrows);
        txttype = (TextView) findViewById(R.id.txttype);
        txtfamily = (TextView) findViewById(R.id.txtFamilyName);
        txtothernam = (TextView) findViewById(R.id.txtothername);
        txtclolorM = (TextView) findViewById(R.id.txtColor);
        txtpetails = (TextView) findViewById(R.id.txtpetals);
        txtsize = (TextView) findViewById(R.id.txtsize);
        txtcultivation = (TextView) findViewById(R.id.txtCultivation);
        txtclimate = (TextView) findViewById(R.id.txtclimate);

        typerelative = (RelativeLayout) findViewById(R.id.typerelative);
        firstItem = (RelativeLayout) findViewById(R.id.mainrelative);
        familyrelat = (RelativeLayout) findViewById(R.id.familyRelative);
        colourrelat = (RelativeLayout) findViewById(R.id.colorrelative);
        petailrela = (RelativeLayout) findViewById(R.id.petalrelative);
        sizerelat = (RelativeLayout) findViewById(R.id.sizerelative);
        cultivationrel = (RelativeLayout) findViewById(R.id.cultivationrelative);
        climaterelat = (RelativeLayout) findViewById(R.id.climaterelative);
        othernamerel = (RelativeLayout) findViewById(R.id.othernamerelative);
        rowsrelative= (RelativeLayout) findViewById(R.id.rowsrelative);

        txtrowstxt= (TextView) findViewById(R.id.txtrowstxt);
        txttypetxt= (TextView) findViewById(R.id.texttypetxt);
        txt1 = (TextView) findViewById(R.id.textView5);
        txt2 = (TextView) findViewById(R.id.txtoth);
        txt3 = (TextView) findViewById(R.id.text2);
        txt4 = (TextView) findViewById(R.id.txt3);
        txt5 = (TextView) findViewById(R.id.txt4);
        txt6 = (TextView) findViewById(R.id.txt5);
        txt7 = (TextView) findViewById(R.id.txt6);

        ChaptDesc = (FrameLayout) findViewById(R.id.chapterDesFram);
        BtnPrev = (TextView) findViewById(R.id.btnprev);
        BtnNext = (TextView) findViewById(R.id.btnnext);

        pb = (ProgressBar) findViewById(R.id.progressBar1);
        listView = (ObservableListView) findViewById(R.id.list);
        listView.setScrollViewCallbacks(this);
        NextPrevButton = (FrameLayout) findViewById(R.id.NxtPrv);
        ab = getSupportActionBar();
        feedItems = new ArrayList<Flower_Desc>();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // set actionbar title
        if (ActionBarName != null && !ActionBarName.equals("")) {
            setActionBarTitle(Flow_desc.this, ActionBarName, getSupportActionBar());
        } else {
            setActionBarTitle(Flow_desc.this, UrlString, getSupportActionBar());
        }

        try {
            if (Typefaces.get(Flow_desc.this, "magmaw04_light") != null) {
                BtnPrev.setTypeface(Typefaces.get(Flow_desc.this, "magmaw04_light"));
                BtnNext.setTypeface(Typefaces.get(Flow_desc.this, "magmaw04_light"));

                txtfamily.setTypeface(Typefaces.get(Flow_desc.this, "magmaw04_light"));
                txtothernam.setTypeface(Typefaces.get(Flow_desc.this, "magmaw04_light"));
                txtclolorM.setTypeface(Typefaces.get(Flow_desc.this, "magmaw04_light"));
                txtpetails.setTypeface(Typefaces.get(Flow_desc.this, "magmaw04_light"));
                txtsize.setTypeface(Typefaces.get(Flow_desc.this, "magmaw04_light"));
                txtcultivation.setTypeface(Typefaces.get(Flow_desc.this, "magmaw04_light"));
                txtclimate.setTypeface(Typefaces.get(Flow_desc.this, "magmaw04_light"));
                txttype.setTypeface(Typefaces.get(Flow_desc.this, "magmaw04_light"));
                txtrows.setTypeface(Typefaces.get(Flow_desc.this, "magmaw04_light"));

            }

            if (Typefaces.get(Flow_desc.this, "megmaIIW04_semibold") != null) {
                txtrowstxt.setTypeface(Typefaces.get(Flow_desc.this, "megmaIIW04_semibold"));
                txttypetxt.setTypeface(Typefaces.get(Flow_desc.this, "megmaIIW04_semibold"));
                txt1.setTypeface(Typefaces.get(Flow_desc.this, "megmaIIW04_semibold"));
                txt2.setTypeface(Typefaces.get(Flow_desc.this, "megmaIIW04_semibold"));
                txt3.setTypeface(Typefaces.get(Flow_desc.this, "megmaIIW04_semibold"));
                txt4.setTypeface(Typefaces.get(Flow_desc.this, "megmaIIW04_semibold"));
                txt5.setTypeface(Typefaces.get(Flow_desc.this, "megmaIIW04_semibold"));
                txt6.setTypeface(Typefaces.get(Flow_desc.this, "megmaIIW04_semibold"));
                txt7.setTypeface(Typefaces.get(Flow_desc.this, "megmaIIW04_semibold"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // load the animation
        animSlideUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
        animSideDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        // set animation listener
        animSlideUp.setAnimationListener(this);
        animSideDown.setAnimationListener(this);
        // These two lines not needed,
        // just to get the look of facebook (changing background color & hiding the icon)
//		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3b5998")));
//		getActionBar().setIcon(
//				   new ColorDrawable(getResources().getColor(android.R.color.transparent)));

        // We first check for cached request


        //next button click listener
        BtnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (Utils.haveNetworkConnection(Flow_desc.this)) {
                        if (BtnPrev.getTag() != null && !BtnPrev.getTag().toString().equals("")) {
                            try {
                                if (jsonObjReq != null) {
                                    AppController.getInstance().getRequestQueue().cancelAll(jsonObjReq);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            ServiceUrl = BtnPrev.getTag().toString();
                            if (ServiceUrl != null && !ServiceUrl.equals("")) {
                                makeJsonObjectRequest(ServiceUrl + ".json");
                            } else {

                            }

                        } else {
                            // Toast.makeText(ChapterActivity.this, getResources().getString(R.string.InternetConnection), Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        //next button click listener
        BtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (Utils.haveNetworkConnection(Flow_desc.this)) {
                        if (BtnNext.getTag() != null && !BtnNext.getTag().toString().equals("")) {
                            try {
                                if (jsonObjReq != null) {
                                    AppController.getInstance().getRequestQueue().cancelAll(jsonObjReq);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            ServiceUrl = BtnNext.getTag().toString();
                            if (ServiceUrl != null && !ServiceUrl.equals("")) {
                                makeJsonObjectRequest(ServiceUrl + ".json");
                            } else {

                            }

                        } else {
                            // Toast.makeText(ChapterActivity.this, getResources().getString(R.string.InternetConnection), Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //call Asynk to get data
        if (Utils.haveNetworkConnection(Flow_desc.this)) {
            makeJsonObjectRequest(ServiceUrl);
        } else {
            Toast.makeText(Flow_desc.this, getResources().getString(R.string.InternetConnection), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * call api for json
     */
    private void makeJsonObjectRequest(String url) {

        try {
            pb.setVisibility(View.VISIBLE);

        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        }

        jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    if (response != null) {
                        if (feedItems != null && !feedItems.isEmpty()) {
                            feedItems.clear();
                        }
                        if (MainDesc != null) {
                            MainDesc = null;
                        }
                        feedItems = jsonParsing(response.toString());
                        if (feedItems != null && !feedItems.isEmpty()) {
                            ShowData(feedItems, MainDesc);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.Volleyerror), Toast.LENGTH_SHORT).show();
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

    public void ShowData(final List<Flower_Desc> list, final Flower_Desc desc) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (MainDesc.getName() != null && !MainDesc.getName().equals("")) {
                    setActionBarTitle(Flow_desc.this, MainDesc.getName(), getSupportActionBar());
                } else {
                    setActionBarTitle(Flow_desc.this, "", getSupportActionBar());
                }

                try {
                    if (BtnPrev.getTag() != null && !BtnPrev.getTag().toString().equals("")) {
                        BtnPrev.setTextColor(getResources().getColor(R.color.white));
                    } else {
                        BtnPrev.setTextColor(getResources().getColor(R.color.disabletext));
                    }
                    if (BtnNext.getTag() != null && !BtnNext.getTag().toString().equals("")) {
                        BtnNext.setTextColor(getResources().getColor(R.color.white));
                    } else {
                        BtnNext.setTextColor(getResources().getColor(R.color.disabletext));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    pb.setVisibility(View.GONE);
                    if (list != null && !list.isEmpty()) {
                        try {
                            listAdapter = new Flower_Desc_adapter(Flow_desc.this, list, MainDesc);
                            listView.setAdapter(listAdapter);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    // TODO: handle exception
                }
                //info data
                try {
                    if (MainDesc.getFamily().isEmpty() || MainDesc.getFamily().equals("") || MainDesc.getFamily().equals("null")) {
                        familyrelat.setVisibility(View.GONE);
                    } else {
                        familyrelat.setVisibility(View.VISIBLE);
                        txtfamily.setText(MainDesc.getFamily());
                    }

                    if (MainDesc.getOther_names().isEmpty() || MainDesc.getOther_names().equals("") || MainDesc.getOther_names().equals("null")) {
                        othernamerel.setVisibility(View.GONE);
                    } else {
                        othernamerel.setVisibility(View.VISIBLE);
                        txtothernam.setText(MainDesc.getOther_names());
                    }

                    if (MainDesc.getColour().isEmpty() || MainDesc.getColour().equals("") || MainDesc.getColour().equals("null")) {
                        colourrelat.setVisibility(View.GONE);
                    } else {
                        colourrelat.setVisibility(View.VISIBLE);
                        txtclolorM.setText(MainDesc.getColour());
                    }

                    if (MainDesc.getType().isEmpty() || MainDesc.getType().equals("") || MainDesc.getType().equals("null")) {
                        typerelative.setVisibility(View.GONE);
                    } else {
                        typerelative.setVisibility(View.VISIBLE);
                        txttype.setText(MainDesc.getType());
                    }

                    if (MainDesc.getRows().isEmpty() || MainDesc.getRows().equals("") || MainDesc.getRows().equals("null")) {
                        rowsrelative.setVisibility(View.GONE);
                    } else {
                        rowsrelative.setVisibility(View.VISIBLE);
                        txtrows.setText(MainDesc.getRows());
                    }

                    if (MainDesc.getPetals().isEmpty() || MainDesc.getPetals().equals("") || MainDesc.getPetals().equals("null")) {
                        petailrela.setVisibility(View.GONE);
                    } else {
                        petailrela.setVisibility(View.VISIBLE);
                        txtpetails.setText(MainDesc.getPetals());
                    }

                    if (MainDesc.getSize().isEmpty() || MainDesc.getSize().equals("") || MainDesc.getSize().equals("null")) {
                        sizerelat.setVisibility(View.GONE);
                    } else {
                        sizerelat.setVisibility(View.VISIBLE);
                        txtsize.setText(MainDesc.getSize());
                    }

                    if (MainDesc.getCultivation().isEmpty() || MainDesc.getCultivation().equals("") || MainDesc.getCultivation().equals("null")) {
                        cultivationrel.setVisibility(View.GONE);
                    } else {
                        cultivationrel.setVisibility(View.VISIBLE);
                        txtcultivation.setText(MainDesc.getCultivation());
                    }

                    if (MainDesc.getClimate().isEmpty() || MainDesc.getClimate().equals("") || MainDesc.getClimate().equals("null")) {
                        climaterelat.setVisibility(View.GONE);
                    } else {
                        climaterelat.setVisibility(View.VISIBLE);
                        txtclimate.setText(MainDesc.getClimate());
                    }

                } catch (Exception e) {

                }
            }
        });
    }

    //parse search list responce
    public List<Flower_Desc> jsonParsing(String jsonString) {
        try {
            ArrayList list = new ArrayList<Flower_Desc>();
            Flower_Desc flowerDescs = new Flower_Desc();
            MainDesc = new Flower_Desc();
            JSONObject jsonObject = new JSONObject(jsonString);

            if (jsonObject.has("_id") && !jsonObject.isNull("_id")) {
                JSONObject id = jsonObject.getJSONObject("_id");
                MainDesc.setId(id.getString("$oid"));
            }
            if (jsonObject.has("name") && !jsonObject.isNull("name")) {
                MainDesc.setName(jsonObject.getString("name"));
            }
            if (jsonObject.has("bot_name") && !jsonObject.isNull("bot_name")) {
                MainDesc.setBot_name(jsonObject.getString("bot_name"));
            }

            if (jsonObject.has("climate") && !jsonObject.isNull("climate")) {
                MainDesc.setClimate(jsonObject.getString("climate"));
            }
            if (jsonObject.has("place") && !jsonObject.isNull("place")) {
                MainDesc.setCultivation(jsonObject.getString("place"));
            }

            if (jsonObject.has("colour") && !jsonObject.isNull("colour")) {
                MainDesc.setColour(jsonObject.getString("colour"));
            }
            if (jsonObject.has("comment") && !jsonObject.isNull("comment")) {
                MainDesc.setComment(jsonObject.getString("comment"));
            }
            if (jsonObject.has("description") && !jsonObject.isNull("description")) {
                MainDesc.setDescription(jsonObject.getString("description"));
            }
            if (jsonObject.has("type") && !jsonObject.isNull("type")) {
                MainDesc.setType(jsonObject.getString("type"));
            }
            if (jsonObject.has("rows") && !jsonObject.isNull("rows")) {
                MainDesc.setRows(jsonObject.getString("rows"));
            }
            if (jsonObject.has("family") && !jsonObject.isNull("family")) {
                MainDesc.setFamily(jsonObject.getString("family"));
            }
            if (jsonObject.has("image_url") && !jsonObject.isNull("image_url")) {
                MainDesc.setImage_url(jsonObject.getString("image_url"));
            }
            if (jsonObject.has("name") && !jsonObject.isNull("name")) {
                MainDesc.setName(jsonObject.getString("name"));
            }
            if (jsonObject.has("other_names") && !jsonObject.isNull("other_names")) {
                MainDesc.setOther_names(jsonObject.getString("other_names"));
            }
            if (jsonObject.has("petals") && !jsonObject.isNull("petals")) {
                MainDesc.setPetals(jsonObject.getString("petals"));
            }
            if (jsonObject.has("significance") && !jsonObject.isNull("significance")) {
                MainDesc.setSignificance(jsonObject.getString("significance"));
            }
            if (jsonObject.has("size") && !jsonObject.isNull("size")) {
                MainDesc.setSize(jsonObject.getString("size"));
            }
            if (jsonObject.has("source") && !jsonObject.isNull("source")) {
                MainDesc.setSource(jsonObject.getString("source"));
            }
            if (jsonObject.has("thumbnail_url") && !jsonObject.isNull("thumbnail_url")) {
                MainDesc.setThumbnail_url(jsonObject.getString("thumbnail_url"));
            }
            if (jsonObject.has("url") && !jsonObject.isNull("url")) {
                MainDesc.setUrl(jsonObject.getString("url"));
            }

            //new added array i.e quote
            if (jsonObject.has("quotes") && !jsonObject.isNull("quotes")) {
                StringBuffer sb = new StringBuffer("");
                JSONArray quote = jsonObject.getJSONArray("quotes");
                for (int q = 0; q < quote.length(); q++) {
                    String jsonObject1 = quote.get(q).toString();
                   // sb.append(jsonObject1);
                   // System.out.println("==appended string==" + sb.toString());
                    if (q + 1 == quote.length()) {
                        sb.append(jsonObject1.replaceAll("<blockquote>", "").replaceAll("</blockquote>",""));

                    }else{
                        sb.append(jsonObject1.replaceAll("<blockquote>", "").replaceAll("</blockquote>","<font><center>*</center></font>"));

                    }
                }
                MainDesc.setQuote(sb.toString());
            }


            //previous
            if (jsonObject.has("previous")) {
                if (!jsonObject.isNull("previous")) {
                    JSONObject Prev = jsonObject.getJSONObject("previous");
                    if (Prev.has("url")) {
                        MainDesc.setPrevUrl(Prev.getString("url"));
                        BtnPrev.setTag("" + Prev.getString("url"));
                    } else {
                        BtnPrev.setTag("");
                    }
                } else {
                    BtnPrev.setTag("");
                }

            }
            if (jsonObject.has("next")) {
                if (!jsonObject.isNull("next")) {
                    JSONObject Prev = jsonObject.getJSONObject("next");
                    if (Prev.has("url")) {
                        MainDesc.setNextUrl(Prev.getString("url"));
                        BtnNext.setTag("" + Prev.getString("url"));
                    } else {
                        BtnNext.setTag("");
                    }
                } else {
                    BtnNext.setTag("");
                }

            }

            //list.add(flowerDescs);


            if (jsonObject.has("variants") && !jsonObject.isNull("variants")) {
                JSONArray jsonArray = jsonObject.getJSONArray("variants");
                for (int i = 0; i < jsonArray.length(); i++) {
                    Flower_Desc flowerDesc = new Flower_Desc();
                    JSONObject c = jsonArray.getJSONObject(i);

                    if (c.has("_id") && !c.isNull("_id")) {
                        JSONObject id = c.getJSONObject("_id");
                        flowerDesc.setId(id.getString("$oid"));
                    }

                    if (c.has("name") && !c.isNull("name")) {
                        flowerDesc.setName(c.getString("name"));
                    }
                    if (c.has("bot_name") && !c.isNull("bot_name")) {
                        flowerDesc.setBot_name(c.getString("bot_name"));
                    }
                    if (c.has("colour") && !c.isNull("colour")) {
                        flowerDesc.setColour(c.getString("colour"));
                    }
                    if (c.has("comment") && !c.isNull("comment")) {
                        flowerDesc.setComment(c.getString("comment"));
                    }
                    if (c.has("description") && !c.isNull("description")) {
                        flowerDesc.setDescription(c.getString("description"));
                    }
                    if (c.has("image_url") && !c.isNull("image_url")) {
                        flowerDesc.setImage_url(c.getString("image_url"));
                    }
                    if (c.has("significance") && !c.isNull("significance")) {
                        flowerDesc.setSignificance(c.getString("significance"));
                    }
                    if (c.has("source") && !c.isNull("source")) {
                        flowerDesc.setSource(c.getString("source"));
                    }
                    if (c.has("url") && !c.isNull("url")) {
                        flowerDesc.setUrl(c.getString("url"));
                    }
                    /// flowerDesc.setThumbnail_url(c.getString("thumbnail_url"));

                    if (c.has("quotes") && !c.isNull("quotes")) {
                        StringBuffer sb = new StringBuffer("");
                        JSONArray quote = c.getJSONArray("quotes");
                        for (int q = 0; q < quote.length(); q++) {
                            String jsonObject1 = quote.get(q).toString();
                            if (q + 1 == quote.length()) {
                                sb.append(jsonObject1.replaceAll("<blockquote>", "").replaceAll("</blockquote>",""));

                            }else{
                                sb.append(jsonObject1.replaceAll("<blockquote>", "").replaceAll("</blockquote>","<font><center>*</center></font>"));

                            }
                        }
                        flowerDesc.setQuote(sb.toString());
                    }


                    list.add(flowerDesc);
                }
            } else {
                list.add(MainDesc);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onScrollChanged(int i, boolean b, boolean b1) {

    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

        if (ab == null) {
            return;
        }
        if (scrollState == ScrollState.UP) {
            //if (ab.isShowing()) {
                try {
                    if (NextPrevButton.getVisibility() == View.VISIBLE) {
                        NextPrevButton.setVisibility(View.INVISIBLE);
                        NextPrevButton.startAnimation(animSlideUp);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    NextPrevButton.clearAnimation();
                    NextPrevButton.setVisibility(View.INVISIBLE);
                }
            //}


        } else if (scrollState == ScrollState.DOWN) {
           // if (!ab.isShowing()) {
                //ab.show();
                ShowView();


            //}
        }
    }

    //set status of connection using UI Thread
    public void ShowView() {
        Thread background = new Thread() {
            public void run() {

                try {
                    // Thread will sleep for 1 seconds
                    sleep(1 * 500);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                if (NextPrevButton.getVisibility() == View.INVISIBLE) {
                                    NextPrevButton.setVisibility(View.VISIBLE);
                                    NextPrevButton.startAnimation(animSideDown);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                NextPrevButton.clearAnimation();
                                NextPrevButton.setVisibility(View.VISIBLE);
                            }
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        background.start();
        // start thread

    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        try {
            if (animation == animSlideUp) {
                NextPrevButton.clearAnimation();
//                if (ab.isShowing()) {
//                    ab.hide();
//                }
            }
            if (animation == animSideDown) {
                NextPrevButton.clearAnimation();


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }


    /**
     * Parsing json reponse and passing the data to feed view list adapter
     */
//    private void parseJsonFeed(JSONObject response) {
//        try {
//            JSONArray feedArray = response.getJSONArray("feed");
//
//            for (int i = 0; i < feedArray.length(); i++) {
//                JSONObject feedObj = (JSONObject) feedArray.get(i);
//
//                FeedItem item = new FeedItem();
//                item.setId(feedObj.getInt("id"));
//                item.setName(feedObj.getString("name"));
//
//                // Image might be null sometimes
//                String image = feedObj.isNull("image") ? null : feedObj
//                        .getString("image");
//                item.setImge(image);
//                item.setStatus(feedObj.getString("status"));
//                item.setProfilePic(feedObj.getString("profilePic"));
//                item.setTimeStamp(feedObj.getString("timeStamp"));
//
//                // url might be null sometimes
//                String feedUrl = feedObj.isNull("url") ? null : feedObj
//                        .getString("url");
//                item.setUrl(feedUrl);
//
//                feedItems.add(item);
//            }
//
//            // notify data changes to list adapater
//            listAdapter.notifyDataSetChanged();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu resource
        getMenuInflater().inflate(R.menu.main_menu_m, menu);

        //info
        MenuItem info = menu.findItem(R.id.action_info);
        info.setVisible(true);

        //search
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchItem.setVisible(false);

        // Retrieve the share menu item
        MenuItem shareItem = menu.findItem(R.id.menu_share);
        shareItem.setVisible(true);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        setShareIntent(1);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_search:
                //mSearchView.setIconified(false);
                return true;
            case R.id.action_info:
                ShowChaptDesc();
                return true;

        }

        return false;
    }

    private void setShareIntent(int position) {
        // BEGIN_INCLUDE(update_sap)
        if (mShareActionProvider != null) {
            // Get the currently selected item, and retrieve it's share intent
            ContentItem item = mItems.get(position);
            Intent shareIntent = item.getShareIntent(Flow_desc.this, Flow_desc.ShareLink);

            // Now update the ShareActionProvider with the new share intent
            mShareActionProvider.setShareIntent(shareIntent);
        }
        // END_INCLUDE(update_sap)
    }

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

    static ArrayList<ContentItem> getSampleContent() {
        ArrayList<ContentItem> items = new ArrayList<ContentItem>();

        items.add(new ContentItem(ContentItem.CONTENT_TYPE_TEXT, "http://flowersong.in/"));
        items.add(new ContentItem(ContentItem.CONTENT_TYPE_TEXT, "http://flowersong.in/"));
        items.add(new ContentItem(ContentItem.CONTENT_TYPE_TEXT, "http://flowersong.in/"));

        return items;
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

    public static void ShowChaptDesc() {
        try {
            if (ChaptDesc.getVisibility() == View.VISIBLE) {
                ChaptDesc.setVisibility(View.INVISIBLE);
            } else {
                ChaptDesc.setVisibility(View.VISIBLE);

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
