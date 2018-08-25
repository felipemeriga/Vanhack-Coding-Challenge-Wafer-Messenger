package com.felipe.vanhackchallenge.core.Adapter;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.felipe.vanhackchallenge.core.Domain.Wafer;
import com.felipe.vanhackchallenge.R;

import java.util.List;

public class WaferListAdapter extends RecyclerView.Adapter<WaferListAdapter.MyViewHolder> {
    private Context context;
    private List<Wafer> waferList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView name, language, currency;
        public RelativeLayout viewBackground, viewForeground;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            language = view.findViewById(R.id.language);
            currency = view.findViewById(R.id.currency);
            viewBackground = view.findViewById(R.id.view_background);
            viewForeground = view.findViewById(R.id.view_foreground);
        }
    }


    public WaferListAdapter(Context context, List<Wafer> waferList) {
        this.context = context;
        this.waferList = waferList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //When ViewHolder is created the wafer_list_item.xml layout will be inflated to the current UI
        View waferView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.wafer_list_item, parent, false);

        return new MyViewHolder(waferView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        //Which wafer of the waferList will be bound setting the TextView from its current POJO attribute
        final Wafer wafer = waferList.get(position);
        holder.name.setText(wafer.getName());
        holder.language.setText(wafer.getLanguage());
        holder.currency.setText(wafer.getCurrency());
    }

    @Override
    public int getItemCount() {
        return waferList.size();
    }

    //This method is called by onSwipe in WaferListActivity that implements this method from RecyclerItemTouchHelperListener interface
    //This method is called to delete items from RecyclerView list
    public void removeWafer(int position) {
        waferList.remove(position);
        // notify the wafer removed by position
        // to perform recycler view delete animations
        notifyItemRemoved(position);
    }

    //PLUS-This method is called to restore an item that have been deleted that is bound to onSwipe method on WaferListActivity
    public void restoreWafer(Wafer wafer, int position) {
        waferList.add(position,wafer);
        // notify wafer added by position
        notifyItemInserted(position);
    }
}
