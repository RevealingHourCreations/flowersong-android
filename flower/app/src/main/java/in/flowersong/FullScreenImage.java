package in.flowersong;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.MenuItem;
import java.util.ArrayList;
import adapter.ViewPagerAdapter;
import util.Typefaces;

/**
 * Created by shailb on 25/12/15.
 */
public class FullScreenImage extends ActionBarActivity {


    private int index = 0;
ArrayList<String> Imgurl;
    ArrayList<String>ColorArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fullscreenimage);
        // getLayoutInflater().inflate(R.layout.fullscreenimage, frameLayout);

        Bundle gt = getIntent().getExtras();
        String UrlString = gt.getString("IMGURL");
        String Name = gt.getString("FLOWER_NAME");
        index=gt.getInt("index");
        Imgurl=gt.getStringArrayList("Array");
        ColorArray=gt.getStringArrayList("ColorArray");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // set actionbar title
        if (Name != null && !Name.equals("")) {
            setActionBarTitle(FullScreenImage.this, Name, getSupportActionBar());
        } else {
            setActionBarTitle(FullScreenImage.this, Name, getSupportActionBar());
        }

        ShowData(Imgurl,ColorArray);
//        img.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                index = ++index % scaleTypes.length;
//                ImageView.ScaleType currScaleType = scaleTypes[index];
//                img.setScaleType(currScaleType);
//                //Toast.makeText(FullScreenImage.this, "ScaleType: " + currScaleType, Toast.LENGTH_SHORT).show();
//            }
//        });
//        // final int stub_id=R.drawable.asoka;
//        //img.setImageResource(stub_id);
//        ShowData(UrlString);

//        if(Imgurl!=null && !Imgurl.isEmpty()){
//            try {
//                listAdapter = new FullScreenImage_Adapter(FullScreenImage.this, Imgurl);
//                listView.setAdapter(listAdapter);
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//Toast.makeText(getApplicationContext(),index,Toast.LENGTH_SHORT).show();

    }

    public void ShowData(final ArrayList<String> Imgurl, final ArrayList<String>ColorArray) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if(Imgurl!=null && !Imgurl.isEmpty()) {
                        ViewPagerAdapter adapter = new ViewPagerAdapter(FullScreenImage.this, Imgurl, ColorArray);
                        ViewPager myPager = (ViewPager) findViewById(R.id.myfivepanelpager);
                        myPager.setAdapter(adapter);
                        myPager.setCurrentItem(index);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return false;
    }

}
