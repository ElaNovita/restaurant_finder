package com.example.novita.ela.restaurant;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.novita.ela.restaurant.Model.GalleryModel;
import com.example.novita.ela.restaurant.Model.MenuModel;
import com.example.novita.ela.restaurant.adapter.GalleryAdapter;
import com.example.novita.ela.restaurant.adapter.MenuAdapter;
import com.example.novita.ela.restaurant.helper.Image;
import com.example.novita.ela.restaurant.helper.MyInterface;
import com.example.novita.ela.restaurant.helper.OnItemClickListener;
import com.example.novita.ela.restaurant.helper.RetrofitBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailRestActivity extends AppCompatActivity {

    RecyclerView menuRecyclerView;
    MenuAdapter menuAdapter;
    RecyclerView galleryRecyclerView;
    GalleryAdapter galleryAdapter;
    String TAG = "respon";
    ArrayList<Image> images = new ArrayList<>();
    ArrayList<Image> menuImages = new ArrayList<>();
    Button haveBeen, bookmark;
    RatingBar ratingBar;
    TextView ratingTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_rest);

        haveBeen = (Button) findViewById(R.id.haveBeen);
        bookmark = (Button) findViewById(R.id.bookmark);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingTxt = (TextView) findViewById(R.id.rating_Txt);

        haveBeen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ratingBar.setVisibility(View.VISIBLE);
                ratingTxt.setVisibility(View.VISIBLE);
            }
        });

        menuRecyclerView = (RecyclerView) findViewById(R.id.menuRv);
        menuRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        galleryRecyclerView = (RecyclerView) findViewById(R.id.galleryRv);
        galleryRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        reqMenu(1);
        reqGallery(1);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void reqMenu(int cafe_id) {
        MyInterface service = new RetrofitBuilder(getApplicationContext()).getRetrofit().create(MyInterface.class);
        Call<List<MenuModel>> call = service.getMenuList(cafe_id);
        call.enqueue(new Callback<List<MenuModel>>() {
            @Override
            public void onResponse(Call<List<MenuModel>> call, Response<List<MenuModel>> response) {
                List<MenuModel> models = response.body();

                menuAdapter = new MenuAdapter(models, getApplicationContext());
                for (int i = 0; i < models.size(); i++) {
                    Image image = new Image();
                    image.setImages(models.get(i).getGambar());
                    menuImages.add(image);
                }
                menuAdapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position, boolean isLongClick) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("images", menuImages);
                        bundle.putInt("position", position);

                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
                        newFragment.setArguments(bundle);
                        newFragment.show(ft, "slideshow");
                    }
                });

                menuAdapter.notifyDataSetChanged();
                menuRecyclerView.setAdapter(menuAdapter);
            }

            @Override
            public void onFailure(Call<List<MenuModel>> call, Throwable t) {

            }
        });
    }

    private void reqGallery(int cafe_id) {
        MyInterface service = new RetrofitBuilder(getApplicationContext()).getRetrofit().create(MyInterface.class);
        Call<List<GalleryModel>> call = service.getPhotoList(cafe_id);
        call.enqueue(new Callback<List<GalleryModel>>() {
            @Override
            public void onResponse(Call<List<GalleryModel>> call, Response<List<GalleryModel>> response) {
                List<GalleryModel> models = response.body();

                galleryAdapter = new GalleryAdapter(models, getApplicationContext());

                for (int i = 0; i < models.size(); i++) {
                    Image image = new Image();
                    image.setImages(models.get(i).getGambar());
                    images.add(image);
                }
                galleryAdapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position, boolean isLongClick) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("images", images);
                        bundle.putInt("position", position);

                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
                        newFragment.setArguments(bundle);
                        newFragment.show(ft, "slideshow");
                    }
                });

                galleryAdapter.notifyDataSetChanged();
                galleryRecyclerView.setAdapter(galleryAdapter);
            }

            @Override
            public void onFailure(Call<List<GalleryModel>> call, Throwable t) {

            }
        });
    }
}
