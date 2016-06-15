package in.flowersong;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import java.util.HashMap;

import util.Typefaces;


/**
 * Created by shailb on 1/11/15.
 */
public class FlowerSong extends BaseActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener{
    private SliderLayout mDemoSlider;
    TextView txtdescr,txtheading,txtmother;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.flower_song, frameLayout);
        mDemoSlider = (SliderLayout) findViewById(R.id.slider);
        txtdescr= (TextView) findViewById(R.id.txtdesc);
        txtheading= (TextView) findViewById(R.id.txtheadingtxt);
        txtmother= (TextView) findViewById(R.id.txtmother);
        setActionBarTitle(FlowerSong.this, getString(R.string.Flower_Song), getSupportActionBar());

        try {
            if (Typefaces.get(FlowerSong.this, "magmaw04_light") != null) {
                txtdescr.setTypeface(Typefaces.get(FlowerSong.this, "magmaw04_light"));

            }
            if (Typefaces.get(FlowerSong.this, "megmaIIW04_semibold") != null) {
                txtmother.setTypeface(Typefaces.get(FlowerSong.this, "megmaIIW04_semibold"));
                txtheading.setTypeface(Typefaces.get(FlowerSong.this, "megmaIIW04_semibold"));
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        String text = "<small>- The Mother</small>";
        txtmother.setText(Html.fromHtml(text));

        String desc=" “When I give flowers, it is an answer to the aspiration coming from the very depths of your being.\n" +
                "\n" +
                "“It is an aspiration or a need - it depends on the person. It may fill a void, or else give you the impetus to progress, or it may help you to find the inner harmony to establish peace.\n" +
                "\n" +
                "“I give you flowers so that you may develop the Divine qualities they symbolize. And they can directly transmit into your soul all that they contain, pure, unalloyed. They possess a very subtle and very deep power and influence.” ";

        txtdescr.setText(desc);
        HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("a",R.drawable.slide1);
        file_maps.put("b", R.drawable.slide2);
        file_maps.put("c", R.drawable.slide3);
       // file_maps.put("d", R.drawable.slide1);


        for(String name : file_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    //.description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);

            mDemoSlider.addSlider(textSliderView);
        }


        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(this);
    }
    @Override
    protected void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        try {
            mDemoSlider.stopAutoCycle();
        }catch (Exception e){
            e.printStackTrace();
        }
        super.onStop();
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        if(position == 0){
            return;
        }
        ChangeActivity(FlowerSong.this, position);
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    @Override
    public void onSliderClick(BaseSliderView baseSliderView) {

    }
}
