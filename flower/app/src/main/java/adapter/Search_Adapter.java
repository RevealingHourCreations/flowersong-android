package adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import SetterGetter.FlowerListDto;
import in.flowersong.Flow_desc;
import in.flowersong.R;
import loadimage.ImageLoader_Feed;
import util.AppController;
import util.Typefaces;
import util.Utils;

/**
 * Created by shailb on 10/11/15.
 */
public class Search_Adapter extends BaseAdapter {
    private ArrayList<FlowerListDto> arr;
    Context context;
    boolean Am;
    private ImageLoader_Feed imageLoader;
    ListView listView;
    JsonArrayRequest jsonObjReq;
    int PageNo = 1;
    boolean Working = false;
    ProgressBar pb;
    com.android.volley.toolbox.ImageLoader imageLoaders = AppController.getInstance().getImageLoader();
    String Word;
    //ImageLoader imageLoaders = AppController.getInstance().getImageLoader();
    public Search_Adapter(Context con, ArrayList<FlowerListDto> arr, ListView listView, ProgressBar pb,String word) {
        this.pb = pb;
        this.listView = listView;
        this.arr = arr;
        this.context = con;
        imageLoader = new ImageLoader_Feed(context);
        this.Word=word;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return arr.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return arr.get(position);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    public class Holder {
        TextView Txtflowername;
        ImageView ImgFlowerThumb;
        ProgressBar pbs;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holderobj = null;
        final LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final FlowerListDto record = arr.get(position);
        if (convertView == null) {
            convertView = (RelativeLayout) vi.inflate(R.layout.search_row, parent, false);
            holderobj = new Holder();
            holderobj.pbs= (ProgressBar) convertView.findViewById(R.id.Imagloadpb);
            holderobj.Txtflowername = (TextView) convertView.findViewById(R.id.TxtViewflowerName);
            holderobj.ImgFlowerThumb = (ImageView) convertView.findViewById(R.id.ImgFlowThmb);
            try {
                if (Typefaces.get(context, "magmaw04_light") != null) {
                    holderobj.Txtflowername.setTypeface(Typefaces.get(context, "magmaw04_light"));
                } else {
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            convertView.setTag(holderobj);
        } else {
            holderobj = (Holder) convertView.getTag();
        }


        try {
            holderobj.Txtflowername.setText("" + record.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //show image
        String ThumbUrl = "https://flowers-assets.s3-ap-southeast-1.amazonaws.com/img/" + record.getThumbnail_url();
        // imageLoader.DisplayImageofwall(ThumbUrl, holderobj.ImgFlowerThumb, 500, true);

        if (record.getImage_url() != null) {

            imageLoader.DisplayImageofwall("https://flowers-assets.s3-ap-southeast-1.amazonaws.com/img/" + record.getThumbnail_url(), holderobj.ImgFlowerThumb, 200, true,holderobj.pbs);


//            holderobj.ImgFlowerThumb.setImageUrl(ThumbUrl, imageLoaders);
//            holderobj.ImgFlowerThumb.setVisibility(View.VISIBLE);
//            holderobj.ImgFlowerThumb
//                    .setResponseObserver(new FeedImageView.ResponseObserver() {
//                        @Override
//                        public void onError() {
//                        }
//
//                        @Override
//                        public void onSuccess() {
//                        }
//                    });
        } else {
        }

        //listview onscroll listner

        holderobj.ImgFlowerThumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FlowerListDto record = arr.get(position);
                if (record.getSlugs() != null && !record.getSlugs().equals("")) {
                    Intent i = new Intent(view.getContext(), Flow_desc.class);
                    i.putExtra("URLNAME", record.getSlugs());
                    view.getContext().startActivity(i);
                }

            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {


                if (arr != null && !arr.isEmpty() && arr.size() >= 5) {
                    if (listView.getLastVisiblePosition() == listView.getAdapter().getCount() - 1
                            && listView.getChildAt(listView.getChildCount() - 1).getBottom() <= listView.getHeight()) {

                        if (!Working) {
                            if (Utils.haveNetworkConnection(context)) {
                                makeJsonArrayRequestAdapter("", true);
                                //System.out.println("END OF THE LIST now ");
                            } else {
                                Toast.makeText(context, context.getResources().getString(R.string.InternetConnection), Toast.LENGTH_SHORT).show();
                            }
                            // Toast.makeText(context, "end of listview", Toast.LENGTH_LONG).show();
                        }
//scroll end reached
//Write your code here
                    }
                }
            }
        });

        return convertView;
    }

    /**
     * Method to make json array request where response starts with [
     */
    private void makeJsonArrayRequestAdapter(String url, final boolean MoreData) {
        try {
            pb.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        }
        if (MoreData) {
            Working = true;
            PageNo++;
            //url = "http://flowersong.in/flowers/page/" + PageNo + ".json";
            url=  "http://flowersong.in/flowers/page/"+PageNo+".json?query=" + Word + "";
            System.out.println("URL===" + url);
        }

        jsonObjReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            ArrayList<FlowerListDto> list = new ArrayList<FlowerListDto>();
                            try {
                                if (response != null) {
                                    list = jsonParsing(response.toString());
                                    if (list != null && !list.isEmpty()) {
                                        ShowMore(list);
                                        Working = false;
                                        //ShowData(list);
                                        //ShowDataSearch(list);
                                    }
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(context,
                                        context.getResources().getString(R.string.Volleyerror), Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(context,
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
                Working = false;
                Toast.makeText(context,
                        context.getResources().getString(R.string.Volleyerror), Toast.LENGTH_SHORT).show();
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

    public void ShowMore(final ArrayList<FlowerListDto> list) {
        ((Activity) context).runOnUiThread(new Runnable() {
            public void run() {
                try {
                    arr.addAll(list);
                    //listView.invalidateViews();
                    listView.requestLayout();
                } catch (Exception e) {
                    e.printStackTrace();
                    // TODO: handle exception
                }
            }
        });

    }

}
