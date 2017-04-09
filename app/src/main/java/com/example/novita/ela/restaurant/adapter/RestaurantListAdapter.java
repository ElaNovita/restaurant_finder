package com.example.novita.ela.restaurant.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.novita.ela.restaurant.Model.CafeModel;
import com.example.novita.ela.restaurant.R;
import com.example.novita.ela.restaurant.helper.OnItemClickListener;
import com.example.novita.ela.restaurant.helper.RetrofitBuilder;

import java.util.List;

/**
 * Created by elaa on 02/04/17.
 */

public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.ViewHolder> {

    List<CafeModel> models;
    Context context;
    String TAG = "respon";
    public  static OnItemClickListener listener;

//    private void setOnItemClickListener(OnItemClickListener listener) {
//        this.listener = listener;
//    }


    public RestaurantListAdapter(List<CafeModel> models, Context context, OnItemClickListener listener) {
        this.models = models;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rest_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.name.setText(models.get(position).getNama());
        holder.address.setText(models.get(position).getLokasi());
//        holder.ratingBar.setRating(Float.parseFloat(models.get(position).getRestaurant().getUser_rating().getAggregate_rating()));
        Glide.with(context).load(RetrofitBuilder.BaseUrl + "img/" + models.get(position).getGambar()).into(holder.res_img);
        Log.d(TAG, "onBindViewHolder: " + RetrofitBuilder.BaseUrl + "img/" + models.get(position).getGambar());
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView res_img;
        TextView name, address;
        RatingBar ratingBar;

        public ViewHolder(View itemView) {
            super(itemView);

            res_img = (ImageView) itemView.findViewById(R.id.res_img);
            name = (TextView) itemView.findViewById(R.id.res_name);
            address = (TextView) itemView.findViewById(R.id.res_address);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(v, getLayoutPosition(), false);
                }
            });
        }
    }

}
