package adapter;

import android.app.Activity;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import in.flowersong.R;
import loadimage.ImageLoaderFullScreen;
import util.TouchImageView;
import util.Typefaces;

public class ViewPagerAdapter extends PagerAdapter {

    Activity activity;
    int imageArray[];
    ArrayList<String> array;
    ArrayList<String> colourarray;
    ImageLoaderFullScreen imageLoaderFullScreen;
    private static final ImageView.ScaleType[] scaleTypes = {ImageView.ScaleType.CENTER, ImageView.ScaleType.CENTER_CROP, ImageView.ScaleType.CENTER_INSIDE, ImageView.ScaleType.FIT_CENTER};
    //ImageView.ScaleType.FIT_XY
    private int index = 0;
    LayoutInflater mLayoutInflater;

    public ViewPagerAdapter(Activity act, ArrayList<String> array, ArrayList<String> colourarray) {
        activity = act;
        this.array = array;
        this.colourarray = colourarray;
        imageLoaderFullScreen = new ImageLoaderFullScreen(activity);
        mLayoutInflater = (LayoutInflater) activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return array.size();
    }

    public Object instantiateItem(ViewGroup collection, int position) {

        View itemView = mLayoutInflater.inflate(R.layout.fullscreen_row, collection, false);

        final ImageView imageView = (ImageView) itemView.findViewById(R.id.fullScreenImg);
        final TextView txtclorName = (TextView) itemView.findViewById(R.id.txtcolorFullscreen);
        try {
            if (Typefaces.get(activity, "magmaw04_light") != null) {
                txtclorName.setTypeface(Typefaces.get(activity, "magmaw04_light"));
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        final ProgressBar progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar3);
        String url = array.get(position);
        String StrcolrName = colourarray.get(position);
        try {
            txtclorName.setText("" + StrcolrName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            imageLoaderFullScreen.DisplayImageofwall("https://flowers-assets.s3-ap-southeast-1.amazonaws.com/img/" + url, imageView, 200, true, progressBar);

        } catch (Exception e) {
            e.printStackTrace();
        }


        //view.setBackgroundResource(array.[position]);

        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                index = ++index % scaleTypes.length;
                ImageView.ScaleType currScaleType = scaleTypes[index];
                imageView.setScaleType(currScaleType);
                //Toast.makeText(FullScreenImage.this, "ScaleType: " + currScaleType, Toast.LENGTH_SHORT).show();
            }
        });

        collection.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(View arg0, int arg1, Object arg2) {
        ((ViewPager) arg0).removeView((View) arg2);
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == ((View) arg1);
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}
