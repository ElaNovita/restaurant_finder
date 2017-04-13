package com.example.novita.ela.restaurant.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.novita.ela.restaurant.Model.CafeModel;
import com.example.novita.ela.restaurant.R;
import com.example.novita.ela.restaurant.helper.RetrofitBuilder;

import java.util.List;

/**
 * Created by elaa on 11/04/17.
 */

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.ViewHolder> {

    List<CafeModel> models;
    Context context;

    public BookmarkAdapter(List<CafeModel> models, Context context) {
        this.models = models;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cafe_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Glide.with(context).load(RetrofitBuilder.BaseUrl + "img/" + models.get(position).getGambar()).into(holder.cafeImg);
        holder.cafeName.setText(models.get(position).getNama());
        holder.cafeAddress.setText(models.get(position).getLokasi());
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView cafeImg;
        TextView cafeName, cafeAddress;

        public ViewHolder(View itemView) {
            super(itemView);

            cafeImg = (ImageView) itemView.findViewById(R.id.cafe_img);
            cafeName = (TextView) itemView.findViewById(R.id.cafe_name);
            cafeAddress = (TextView) itemView.findViewById(R.id.address);
        }
    }

}

