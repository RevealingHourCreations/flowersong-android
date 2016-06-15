package in.flowersong;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.JsonObjectRequest;

import util.AppController;
import util.Typefaces;

/**
 * Created by shaileshgaikwad on 6/24/15.
 */
public class About extends BaseActivity {
    TextView txtHeading;
    TextView txtDescription,txtsubtitle,txtsubtitledesc;
    ProgressBar pb;
    String StrHeading, StrDescription;
    RelativeLayout AllView;
    JsonObjectRequest jsonObjReq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.aboutflower, frameLayout);
        setActionBarTitle(About.this, getString(R.string.About), getSupportActionBar());
        pb = (ProgressBar) findViewById(R.id.progressBar1);
        txtHeading = (TextView) findViewById(R.id.txtheading);
        AllView = (RelativeLayout) findViewById(R.id.allview);
        AllView.setVisibility(View.VISIBLE);
        txtDescription = (TextView) findViewById(R.id.txtdescript);
        txtsubtitle= (TextView) findViewById(R.id.txtsubtitle);
        txtsubtitledesc= (TextView) findViewById(R.id.txtsubtitledesc);
        //set font here
        try {
            if (Typefaces.get(About.this, "magmaw04_light") != null) {
                txtDescription.setTypeface(Typefaces.get(About.this, "magmaw04_light"));
                txtsubtitledesc.setTypeface(Typefaces.get(About.this, "magmaw04_light"));
            }

            if (Typefaces.get(About.this, "megmaIIW04_semibold") != null) {
                txtHeading.setTypeface(Typefaces.get(About.this, "megmaIIW04_semibold"));
                txtsubtitle.setTypeface(Typefaces.get(About.this, "megmaIIW04_semibold"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        ShowData();


    }
//override method from baseactivity class
    @Override
    public void onDrawerItemSelected(View view, int position) {
        if (position != 3) {
            ChangeActivity(About.this, position);
        }
    }

//About data to show
    public void ShowData() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    StrHeading = "How it all began";
                    StrDescription = "From the start of the Ashram in 1927, the Mother used flowers in her interactions with the sadhaks who came to see her. From an early period, she gave significances to certain flowers. In this way there evolved a language of flowers by which the Mother communicated with the sadhaks. Within a few years, she had given names to hundreds of flowers. A list compiled by an Ashram gardener in 1930 contained 410 entries.\n" +
                            "\n" +
                            "In 1953, a French sadhak edited the Ashram's first book on flowers, 'Le role des fleurs'. This contained 636 significances. In the early 1970s, the Mother reviewed these significances, changing some of them, and added more than 240 new ones. At the same time she gave a brief comment on almost all the flowers. The result of this revision was ‘Flowers and Their Messages’, issued in 1973, which contained 879 significances.\n" +
                            "\n" +
                            "In 2000, the ashram published a new book ‘The Spiritual Significance of Flowers’ revising some botanical names and adding 19 new significances discovered in the notebooks of early Ashram gardeners." +
                            "\n";


                    String Subtitledesc="The information on this site has been compiled by Narad (Richard Eggenberger), a long-time member of the Sri Aurobindo Ashram and Auroville. Narad, who is a musician, poet, landscaper, and horticulturist, was given the task of creating the beautiful Matrimandir gardens by the Mother in 1969. He also worked personally with the Mother on the spiritual significance of flowers and edited the book 'Flowers and Their Messages'. ";


                    pb.setVisibility(View.GONE);
                    if ((StrHeading != null && !StrHeading.equals("")) && (StrDescription != null && !StrDescription.equals(""))) {
                        txtHeading.setText("" + StrHeading);
                        txtDescription.setText(StrDescription);
                        txtsubtitledesc.setText(Subtitledesc);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onPause() {
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
    public void onStop() {
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
    public void onBackPressed() {
        super.onBackPressed();
        About.this.finish();
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
}
