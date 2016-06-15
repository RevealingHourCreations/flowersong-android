package adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import SetterGetter.Flower_Desc;
import in.flowersong.FullScreenImage;
import in.flowersong.R;
import util.AppController;
import util.Typefaces;

/**
 * Created by shailb on 6/11/15.
 */
public class Flower_Desc_adapter extends BaseAdapter {
    private Activity activity;
    private List<Flower_Desc> feedItems;
    Flower_Desc desc;
    private loadimage.ImageLoader imageLoaders;
    ArrayList<String> ImgurlArray;
    ArrayList<String> ColorArray;

    public Flower_Desc_adapter(Activity activity, List<Flower_Desc> feedItems, Flower_Desc desc) {
        this.activity = activity;
        this.feedItems = feedItems;
        ImgurlArray = new ArrayList<>();
        ColorArray = new ArrayList<>();
        try {
            if (!this.feedItems.isEmpty() && this.feedItems != null && this.feedItems.size() >= 1) {
                this.desc = desc;

            } else {
                this.desc = this.feedItems.get(0);
            }
            imageLoaders = new loadimage.ImageLoader(this.activity);

            if (!this.feedItems.isEmpty() && this.feedItems != null && feedItems.size() == 1) {
                if (this.desc.getImage_url() != null) {
                    ImgurlArray.add(this.desc.getImage_url());
                }
                if (this.desc.getColour() != null) {
                    ColorArray.add(this.desc.getColour());
                }
            } else {

                if (!this.feedItems.isEmpty() && this.feedItems != null) {
                    if (this.desc.getImage_url() != null) {
                        ImgurlArray.add(this.desc.getImage_url());
                    }
                    if (this.desc.getColour() != null) {
                        ColorArray.add(this.desc.getColour());
                    }
                    for (int i = 0; i < feedItems.size(); i++) {
                        Flower_Desc flower_desc = this.feedItems.get(i);
                        if (flower_desc.getImage_url() != null) {
                            ImgurlArray.add(flower_desc.getImage_url());
                        }
                        if (flower_desc.getColour() != null) {
                            ColorArray.add(flower_desc.getColour());
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getCount() {
        return feedItems.size();
    }

    @Override
    public Object getItem(int i) {
        return feedItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    public class Holder {
        TextView txtdesc, txtcolor, txtsignificance, txtmothercommetn, varient;
        TextView txtfamily, txtothernam, txtclolorM, txtpetails, txtsize, txtcultivation, txtclimate;
        TextView MTxtDesc, MSignifiMo, Mmothercomm, txtWikipediaC, txtwikiC, MtxtName, MtxtSubName;
        WebView MtxtQuote, txtvarientquote;

        ImageView feedImageView, MainImg;
        RelativeLayout firstItem, othernamerel, familyrelat, colourrelat, petailrela, sizerelat, cultivationrel, climaterelat;
        RelativeLayout MainimgLinear;
        LinearLayout SecondTypeFeedLine, DescMainFeed;
        ProgressBar ImgLoadingProg, Mainimgprog;

        //labels txtview
        TextView txt8, txt9, txt10, txt11, txt12, txt13, txt14, txt15, txt16;

        WebView wb;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holderobj = null;
        final LayoutInflater vi = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final Flower_Desc record = feedItems.get(position);
        if (convertView == null) {
            convertView = (RelativeLayout) vi.inflate(R.layout.feed_item, parent, false);
            holderobj = new Holder();


            holderobj.MtxtName = (TextView) convertView.findViewById(R.id.txtname);
            holderobj.MtxtSubName = (TextView) convertView.findViewById(R.id.txtsubname);

            holderobj.txtWikipediaC = (TextView) convertView.findViewById(R.id.txtWikipedia);

            holderobj.txtwikiC = (TextView) convertView.findViewById(R.id.txtwiki);
            holderobj.Mainimgprog = (ProgressBar) convertView.findViewById(R.id.mainimgprogress);
            holderobj.ImgLoadingProg = (ProgressBar) convertView.findViewById(R.id.imgloadingprog);
            holderobj.MTxtDesc = (TextView) convertView.findViewById(R.id.txtdescMainFeed);
            holderobj.MSignifiMo = (TextView) convertView.findViewById(R.id.txtSignificanceMainfeed);
            holderobj.Mmothercomm = (TextView) convertView.findViewById(R.id.txtmothercommmain);
            holderobj.MtxtQuote = (WebView) convertView.findViewById(R.id.txtquote);

            holderobj.DescMainFeed = (LinearLayout) convertView.findViewById(R.id.MainLinearDesc);
            holderobj.MainimgLinear = (RelativeLayout) convertView.findViewById(R.id.linearmain);
            holderobj.MainImg = (ImageView) convertView.findViewById(R.id.ImgFlower);
            holderobj.SecondTypeFeedLine = (LinearLayout) convertView.findViewById(R.id.linearScondTypeFeed);


            holderobj.txtdesc = (TextView) convertView.findViewById(R.id.txtdescption);
            holderobj.txtcolor = (TextView) convertView.findViewById(R.id.txtColors);
            holderobj.txtsignificance = (TextView) convertView.findViewById(R.id.txtsignificance_mother);
            holderobj.txtmothercommetn = (TextView) convertView.findViewById(R.id.txtmothercomment);
            holderobj.txtvarientquote = (WebView) convertView.findViewById(R.id.txtquotessub);

            holderobj.txtfamily = (TextView) convertView.findViewById(R.id.txtFamilyName);
            holderobj.txtothernam = (TextView) convertView.findViewById(R.id.txtothername);
            holderobj.txtclolorM = (TextView) convertView.findViewById(R.id.txtColor);
            holderobj.txtpetails = (TextView) convertView.findViewById(R.id.txtpetals);
            holderobj.txtsize = (TextView) convertView.findViewById(R.id.txtsize);
            holderobj.txtcultivation = (TextView) convertView.findViewById(R.id.txtCultivation);
            holderobj.txtclimate = (TextView) convertView.findViewById(R.id.txtclimate);

            holderobj.feedImageView = (ImageView) convertView.findViewById(R.id.feedImage1);


            holderobj.varient = (TextView) convertView.findViewById(R.id.txtvarients);
            holderobj.firstItem = (RelativeLayout) convertView.findViewById(R.id.mainrelative);
            holderobj.familyrelat = (RelativeLayout) convertView.findViewById(R.id.familyRelative);
            holderobj.colourrelat = (RelativeLayout) convertView.findViewById(R.id.colorrelative);
            holderobj.petailrela = (RelativeLayout) convertView.findViewById(R.id.petalrelative);
            holderobj.sizerelat = (RelativeLayout) convertView.findViewById(R.id.sizerelative);
            holderobj.cultivationrel = (RelativeLayout) convertView.findViewById(R.id.cultivationrelative);
            holderobj.climaterelat = (RelativeLayout) convertView.findViewById(R.id.climaterelative);
            holderobj.othernamerel = (RelativeLayout) convertView.findViewById(R.id.othernamerelative);
//label

            holderobj.txt8 = (TextView) convertView.findViewById(R.id.txtds);
            holderobj.txt9 = (TextView) convertView.findViewById(R.id.txtsigm);
            holderobj.txt10 = (TextView) convertView.findViewById(R.id.txtmothcm);

            holderobj.txt11 = (TextView) convertView.findViewById(R.id.textView9);
            holderobj.txt12 = (TextView) convertView.findViewById(R.id.textView10);
            holderobj.txt13 = (TextView) convertView.findViewById(R.id.textView11);
            holderobj.txt14 = (TextView) convertView.findViewById(R.id.textView12);
            holderobj.txt15 = (TextView) convertView.findViewById(R.id.txtnamequote);
            holderobj.txt16 = (TextView) convertView.findViewById(R.id.txtsubquotes);


            holderobj.MtxtQuote.getSettings().setJavaScriptEnabled(true);
            holderobj.MtxtQuote.getSettings().setBuiltInZoomControls(false);
            holderobj.MtxtQuote.setHorizontalScrollBarEnabled(true);
            holderobj.MtxtQuote.setHorizontalFadingEdgeEnabled(false);
            holderobj.MtxtQuote.setHorizontalScrollbarOverlay(false);
            holderobj.MtxtQuote.setVerticalScrollBarEnabled(true);
            holderobj.MtxtQuote.getSettings().setDefaultFontSize(18);
            holderobj.MtxtQuote.getSettings().setJavaScriptEnabled(true);
            holderobj.MtxtQuote.setBackgroundColor(Color.parseColor("#ffffff"));
            holderobj.MtxtQuote.getSettings().setAllowFileAccess(true);
            holderobj.MtxtQuote.setWebChromeClient(new WebChromeClient());
            holderobj.MtxtQuote.getSettings().setAppCacheEnabled(true);
            holderobj.MtxtQuote.getSettings().setDomStorageEnabled(true);
            holderobj.MtxtQuote.getSettings().setPluginState(WebSettings.PluginState.ON);
            //prevent to load cache
            holderobj.MtxtQuote.getSettings().setAppCacheEnabled(false);
            holderobj.MtxtQuote.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);


            holderobj.txtvarientquote.getSettings().setJavaScriptEnabled(true);
            holderobj.txtvarientquote.getSettings().setBuiltInZoomControls(false);
            holderobj.txtvarientquote.setHorizontalScrollBarEnabled(true);
            holderobj.txtvarientquote.setHorizontalFadingEdgeEnabled(false);
            holderobj.txtvarientquote.setHorizontalScrollbarOverlay(false);
            holderobj.txtvarientquote.setVerticalScrollBarEnabled(true);
            holderobj.txtvarientquote.getSettings().setDefaultFontSize(18);
            holderobj.txtvarientquote.getSettings().setJavaScriptEnabled(true);
            holderobj.txtvarientquote.setBackgroundColor(Color.parseColor("#ffffff"));
            holderobj.txtvarientquote.getSettings().setAllowFileAccess(true);
            holderobj.txtvarientquote.setWebChromeClient(new WebChromeClient());
            holderobj.txtvarientquote.getSettings().setAppCacheEnabled(true);
            holderobj.txtvarientquote.getSettings().setDomStorageEnabled(true);
            holderobj.txtvarientquote.getSettings().setPluginState(WebSettings.PluginState.ON);
            //prevent to load cache
            holderobj.txtvarientquote.getSettings().setAppCacheEnabled(false);
            holderobj.txtvarientquote.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);


            try {
                if (Typefaces.get(activity, "MagmaIIW04_SemiboldItalic") != null) {
                    holderobj.MtxtSubName.setTypeface(Typefaces.get(activity, "MagmaIIW04_SemiboldItalic"));
                }


                if (Typefaces.get(activity, "megmaIIW04_semibold") != null) {
                    holderobj.MtxtName.setTypeface(Typefaces.get(activity, "megmaIIW04_semibold"));
                    holderobj.txtWikipediaC.setTypeface(Typefaces.get(activity, "megmaIIW04_semibold"));
                    holderobj.txtwikiC.setTypeface(Typefaces.get(activity, "megmaIIW04_semibold"));

                }

                if (Typefaces.get(activity, "magmaIIW04_thin") != null) {
                    holderobj.txtsignificance.setTypeface(Typefaces.get(activity, "magmaIIW04_thin"));
                    holderobj.txtmothercommetn.setTypeface(Typefaces.get(activity, "magmaIIW04_thin"));
                    holderobj.MSignifiMo.setTypeface(Typefaces.get(activity, "magmaIIW04_thin"));
                    holderobj.Mmothercomm.setTypeface(Typefaces.get(activity, "magmaIIW04_thin"));
                    // holderobj.MtxtQuote.setTypeface(Typefaces.get(activity, "magmaIIW04_thin"));
                    //holderobj.txtvarientquote.setTypeface(Typefaces.get(activity, "magmaIIW04_thin"));

                }
                if (Typefaces.get(activity, "magmaw04_light") != null) {

                    holderobj.txtdesc.setTypeface(Typefaces.get(activity, "magmaw04_light"));
                    holderobj.txtcolor.setTypeface(Typefaces.get(activity, "magmaw04_light"));
                    holderobj.MTxtDesc.setTypeface(Typefaces.get(activity, "magmaw04_light"));


                    holderobj.varient.setTypeface(Typefaces.get(activity, "magmaw04_light"));
                    holderobj.txt9.setTypeface(Typefaces.get(activity, "magmaw04_light"));
                    holderobj.txt10.setTypeface(Typefaces.get(activity, "magmaw04_light"));
                    holderobj.txt8.setTypeface(Typefaces.get(activity, "magmaw04_light"));
                    holderobj.txt13.setTypeface(Typefaces.get(activity, "magmaw04_light"));
                    holderobj.txt14.setTypeface(Typefaces.get(activity, "magmaw04_light"));
                    holderobj.txt11.setTypeface(Typefaces.get(activity, "magmaw04_light"));
                    holderobj.txt12.setTypeface(Typefaces.get(activity, "magmaw04_light"));
                    holderobj.txt15.setTypeface(Typefaces.get(activity, "magmaw04_light"));
                    holderobj.txt16.setTypeface(Typefaces.get(activity, "magmaw04_light"));

                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            convertView.setTag(holderobj);
        } else {
            holderobj = (Holder) convertView.getTag();
        }
        //UI here


        if (position == 0) {
            // Chcek for empty status message

            holderobj.firstItem.setVisibility(View.VISIBLE);
            holderobj.varient.setVisibility(View.VISIBLE);
            holderobj.MainimgLinear.setVisibility(View.VISIBLE);

            if (desc.getImage_url() != null && !desc.getImage_url().equals("") && !desc.getImage_url().equals("null")) {

                imageLoaders.DisplayImageofwall("https://flowers-assets.s3-ap-southeast-1.amazonaws.com/img/" + desc.getImage_url(), holderobj.MainImg, 10, true, holderobj.Mainimgprog);


//                holderobj.MainImg.setImageUrl("https://flowers-assets.s3-ap-southeast-1.amazonaws.com/img/" + desc.getImage_url(), imageLoader);
//                holderobj.MainImg.setVisibility(View.VISIBLE);
//                holderobj.MainImg
//                        .setResponseObserver(new FeedImageView.ResponseObserver() {
//                            @Override
//                            public void onError() {
//                            }
//
//                            @Override
//                            public void onSuccess() {
//                            }
//                        });
            } else {
                holderobj.feedImageView.setVisibility(View.GONE);
            }

            holderobj.MainImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (desc.getImage_url() != null && !desc.getImage_url().equals("") && !desc.getImage_url().equals("null")) {
                            Intent i = new Intent(view.getContext(), FullScreenImage.class);
                            i.putExtra("FLOWER_NAME", desc.getName());
                            i.putExtra("index", position);
                            i.putStringArrayListExtra("ColorArray", ColorArray);
                            i.putStringArrayListExtra("Array", ImgurlArray);

                            view.getContext().startActivity(i);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            });

//holderobj.MainImg.setOnClickListener(new View.OnClickListener() {
//    @Override
//    public void onClick(View view) {
//        ShowDialog();
//    }
//});

            holderobj.DescMainFeed.setVisibility(View.VISIBLE);

            if (desc.getName().isEmpty() || desc.getName().equals("") || desc.getName().equals("null")) {
                holderobj.MtxtName.setVisibility(View.GONE);
            } else {
                holderobj.MtxtName.setText(desc.getName());
            }

            if (desc.getBot_name().isEmpty() || desc.getBot_name().equals("") || desc.getBot_name().equals("null")) {
                holderobj.MtxtSubName.setVisibility(View.GONE);
            } else {
                holderobj.MtxtSubName.setText("(" + desc.getBot_name() + ")");
            }


            if (desc.getDescription().isEmpty() || desc.getDescription().equals("") || desc.getDescription().equals("null")) {
                holderobj.MTxtDesc.setVisibility(View.GONE);
                holderobj.txt8.setVisibility(View.GONE);
                //  holderobj.DescMainFeed.setVisibility(View.GONE);
            } else {
                //  holderobj.DescMainFeed.setVisibility(View.VISIBLE);
                holderobj.MTxtDesc.setVisibility(View.VISIBLE);
                holderobj.txt8.setVisibility(View.VISIBLE);
                holderobj.MTxtDesc.setText(desc.getDescription());
            }

            if (desc.getSignificance().isEmpty() || desc.getSignificance().equals("") || desc.getSignificance().equals("null")) {
                holderobj.MSignifiMo.setVisibility(View.GONE);
                holderobj.txt9.setVisibility(View.GONE);
                // holderobj.DescMainFeed.setVisibility(View.GONE);
            } else {
                // holderobj.DescMainFeed.setVisibility(View.VISIBLE);
                holderobj.MSignifiMo.setVisibility(View.VISIBLE);
                holderobj.txt9.setVisibility(View.VISIBLE);
                holderobj.MSignifiMo.setText(desc.getSignificance());
            }

            if (desc.getComment().isEmpty() || desc.getComment().equals("") || desc.getComment().equals("null")) {
                holderobj.Mmothercomm.setVisibility(View.GONE);
                holderobj.txt10.setVisibility(View.GONE);
                // holderobj.DescMainFeed.setVisibility(View.GONE);
            } else {
                // holderobj.DescMainFeed.setVisibility(View.VISIBLE);
                holderobj.Mmothercomm.setVisibility(View.VISIBLE);
                holderobj.txt10.setVisibility(View.VISIBLE);
                holderobj.Mmothercomm.setText(desc.getComment());
            }


            if (desc.getQuote().isEmpty() || desc.getQuote().equals("") || desc.getQuote().equals("null")) {
                holderobj.txt15.setVisibility(View.GONE);
                holderobj.MtxtQuote.setVisibility(View.GONE);
                //holderobj.DescMainFeed.setVisibility(View.GONE);
            } else {

                String script = "<html>"
                        + "<head>"
                        + "<style type=\"text/css\">"
                        + "@font-face {font-family: \"Magma II\";src: url(\"file:///android_asset/fonts/magmaw04_light.ttf\");}"
                        + "p {font-family:'Magma II'}"
                        + "body {"
                        + "color: rgb(69,69,69);"
                        + "}"
                        + "</style>"
                        + "</head>"
                        + "<body>"
                        + desc.getQuote()
                        + "</body>"
                        + "</html>";


                holderobj.txt15.setVisibility(View.VISIBLE);
                holderobj.MtxtQuote.setVisibility(View.VISIBLE);
                try {
                    holderobj.MtxtQuote.loadDataWithBaseURL("http://foo", script, "text/html", "utf-8", null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // holderobj.MtxtQuote.setText(Html.fromHtml(desc.getQuote()));
            }

            if ((desc.getDescription().isEmpty() || desc.getDescription().equals("") || desc.getDescription().equals("null")) &&
                    (desc.getSignificance().isEmpty() || desc.getSignificance().equals("") || desc.getSignificance().equals("null")) &&
                    (desc.getComment().isEmpty() || desc.getComment().equals("") || desc.getComment().equals("null")) &&
                    (desc.getQuote().isEmpty() || desc.getQuote().equals("") || desc.getQuote().equals("null"))) {
                holderobj.DescMainFeed.setVisibility(View.GONE);
            } else {
                holderobj.DescMainFeed.setVisibility(View.VISIBLE);
            }

//            if (feedItems.size() == 1) {
//
//               // holderobj.DescMainFeed.setVisibility(View.VISIBLE);
//
//
//                if (desc.getDescription().isEmpty() || desc.getDescription().equals("") || desc.getDescription().equals("null")) {
//                    holderobj.MTxtDesc.setVisibility(View.GONE);
//                    holderobj.txt8.setVisibility(View.GONE);
//                } else {
//                    holderobj.MTxtDesc.setVisibility(View.VISIBLE);
//                    holderobj.txt8.setVisibility(View.VISIBLE);
//                    holderobj.MTxtDesc.setText(desc.getDescription());
//                }
//
//                if (desc.getSignificance().isEmpty() || desc.getSignificance().equals("") || desc.getSignificance().equals("null")) {
//                    holderobj.MSignifiMo.setVisibility(View.GONE);
//                    holderobj.txt9.setVisibility(View.GONE);
//                } else {
//                    holderobj.MSignifiMo.setVisibility(View.VISIBLE);
//                    holderobj.txt9.setVisibility(View.VISIBLE);
//                    holderobj.MSignifiMo.setText(desc.getSignificance());
//                }
//
//                if (desc.getComment().isEmpty() || desc.getComment().equals("") || desc.getComment().equals("null")) {
//                    holderobj.Mmothercomm.setVisibility(View.GONE);
//                    holderobj.txt10.setVisibility(View.GONE);
//                } else {
//                    holderobj.Mmothercomm.setVisibility(View.VISIBLE);
//                    holderobj.txt10.setVisibility(View.VISIBLE);
//                    holderobj.Mmothercomm.setText(desc.getComment());
//                }
//
//                if (desc.getQuote().isEmpty() || desc.getQuote().equals("") || desc.getQuote().equals("null")) {
//                    holderobj.txt15.setVisibility(View.INVISIBLE);
//                    holderobj.MtxtQuote.setVisibility(View.INVISIBLE);
//                } else {
//                    holderobj.MtxtQuote.setText(Html.fromHtml(desc.getQuote().toString()));
//                }
//
//
//            } else {
//               // holderobj.DescMainFeed.setVisibility(View.GONE);
//            }


        } else {
            holderobj.firstItem.setVisibility(View.GONE);
            holderobj.varient.setVisibility(View.INVISIBLE);
            holderobj.MainimgLinear.setVisibility(View.GONE);
        }


        if (feedItems.size() > 1) {


            //feed content
            if (record.getDescription().isEmpty() || record.getDescription().equals("") || record.getDescription().equals("null")) {
                holderobj.txtdesc.setVisibility(View.GONE);
            } else {
                // status is empty, remove from view
                holderobj.txtdesc.setText(record.getDescription());
                holderobj.txtdesc.setVisibility(View.VISIBLE);

            }

            if (record.getColour().isEmpty() || record.getColour().equals("") || record.getColour().equals("null")) {
                holderobj.txtcolor.setVisibility(View.GONE);
            } else {
                // status is empty, remove from view
                holderobj.txtcolor.setText(record.getColour());
                holderobj.txtcolor.setVisibility(View.VISIBLE);

            }

            if (record.getSignificance().isEmpty() || record.getSignificance().equals("") || record.getSignificance().equals("null")) {
                holderobj.txtsignificance.setVisibility(View.GONE);
            } else {
                // status is empty, remove from view
                holderobj.txtsignificance.setText(record.getSignificance());
                holderobj.txtsignificance.setVisibility(View.VISIBLE);

            }


            if (record.getComment().isEmpty() || record.getComment().equals("") || record.getComment().equals("null")) {
                holderobj.txtmothercommetn.setVisibility(View.GONE);
            } else {
                holderobj.txtmothercommetn.setText(record.getComment());
                holderobj.txtmothercommetn.setVisibility(View.VISIBLE);
                // status is empty, remove from view

            }

            if (record.getQuote().isEmpty() || record.getQuote().equals("") || record.getQuote().equals("null")) {
                holderobj.txt16.setVisibility(View.GONE);
                holderobj.txtvarientquote.setVisibility(View.GONE);
            } else {

                String script = "<html>"
                        + "<head>"
                        + "<style type=\"text/css\">"
                        + "@font-face {font-family: \"Magma II\";src: url(\"file:///android_asset/fonts/magmaw04_light.ttf\");}"
                        + "p {font-family:'Magma II'}"
                        + "body {"
                        + "color: rgb(69,69,69);"
                        + "}"
                        + "</style>"
                        + "</head>"
                        + "<body>"
                        + record.getQuote()
                        + "</body>"
                        + "</html>";
//99
                holderobj.txt16.setVisibility(View.VISIBLE);
                holderobj.txtvarientquote.setVisibility(View.VISIBLE);
                try {
                    holderobj.txtvarientquote.loadDataWithBaseURL("http://foo", script, "text/html", "utf-8", null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //holderobj.txtvarientquote.setText(Html.fromHtml(record.getQuote().toString()));
            }


            if (record.getImage_url() != null && !record.getImage_url().equals("")) {

                imageLoaders.DisplayImageofwall("https://flowers-assets.s3-ap-southeast-1.amazonaws.com/img/" + record.getImage_url(), holderobj.feedImageView, 200, true, holderobj.ImgLoadingProg);
//
//                holderobj.feedImageView.setImageUrl("https://flowers-assets.s3-ap-southeast-1.amazonaws.com/img/" + record.getImage_url(), imageLoader);
//                holderobj.feedImageView.setVisibility(View.VISIBLE);
//                holderobj.feedImageView
//                        .setResponseObserver(new FeedImageView.ResponseObserver() {
//                            @Override
//                            public void onError() {
//                            }
//
//                            @Override
//                            public void onSuccess() {
//                            }
//                        });
            } else {
                holderobj.feedImageView.setVisibility(View.GONE);
            }

            holderobj.feedImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Flower_Desc descs = feedItems.get(position);
                    try {
                        if (descs.getImage_url() != null && !descs.getImage_url().equals("") && !descs.getImage_url().equals("null")) {
                            Intent i = new Intent(view.getContext(), FullScreenImage.class);
                            i.putExtra("FLOWER_NAME", desc.getName());
                            i.putExtra("index", position + 1);
                            i.putStringArrayListExtra("ColorArray", ColorArray);
                            i.putStringArrayListExtra("Array", ImgurlArray);
                            view.getContext().startActivity(i);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            });


        } else {
            holderobj.varient.setVisibility(View.INVISIBLE);
            holderobj.SecondTypeFeedLine.setVisibility(View.GONE);
        }


//        holderobj.txtwikiC.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Flower_Desc record = feedItems.get(position);
//                String SourceUrl = record.getUrl().toString();
//                if (!SourceUrl.isEmpty() && !SourceUrl.equals("") && !SourceUrl.equals("null")) {
//                    Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(SourceUrl));
//                    view.getContext().startActivity(intent);
//                }
//
//
//            }
//        });

//        holderobj.txtWikipediaC.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String SourceUrl = desc.getUrl().toString();
//                if (!SourceUrl.isEmpty() && !SourceUrl.equals("") && !SourceUrl.equals("null")) {
//                    Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(SourceUrl));
//                    view.getContext().startActivity(intent);
//                }
//
//
//            }
//        });
        return convertView;


    }

}
