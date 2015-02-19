package kr.nogcha.sejongbus.drawer;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import kr.nogcha.sejongbus.R;

/**
 * Created by FullOfOrange on 2015. 2. 17..
 */
public class tabAdapter extends RecyclerView.Adapter<tabAdapter.DrawerViewHolder> {

    private List<drawerlistitem> items;

    public DrawerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.drawer_item, viewGroup, false);
        return new DrawerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DrawerViewHolder holder, int position) {
        drawerlistitem name = items.get(position);
        holder.menutext.setText(name.menuname);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public final static class DrawerViewHolder extends RecyclerView.ViewHolder {
        TextView menutext;

        public DrawerViewHolder(View itemView) {
            super(itemView);
            menutext = (TextView) itemView.findViewById(R.id.main_drawer_text);

        }
    }

}