package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import in.flowersong.R;
import model.NavDrawerItem;
import util.Typefaces;

public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.MyViewHolder> {
    List<NavDrawerItem> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;
    public NavigationDrawerAdapter(Context context, List<NavDrawerItem> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.nav_drawer_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NavDrawerItem current = data.get(position);
//        if (position == 0|| position == 4 || position == 8) {
//
//            holder.title.setBackgroundColor(Color.parseColor("#bdbdbd"));
//            holder.title.setTextColor(Color.parseColor("#777D77"));
//            // holder.title.setTypeface(null, Typeface.BOLD);
//            //holder.title.setGravity(Gravity.CENTER);
//
//            try {
//                if (Typefaces.get(context, "CharlotteSans_nn") != null) {
//                    holder.title.setTypeface(Typefaces.get(context, "CharlotteSans_nn"));
//                }
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }


        holder.title.setText(current.getTitle());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);

            try {
                if (Typefaces.get(context, "magmaw04_light") != null) {
                    title.setTypeface(Typefaces.get(context, "magmaw04_light"));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
