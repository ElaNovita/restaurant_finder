package com.example.novita.ela.restaurant;

import android.content.Intent;
import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.novita.ela.restaurant.Model.CafeModel;
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
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailRestActivity extends AppCompatActivity {

    RecyclerView menuRecyclerView;
    MenuAdapter menuAdapter;
    RecyclerView galleryRecyclerView;
    GalleryAdapter galleryAdapter;
    String TAG = "respon" ;
    ArrayList<Image> images = new ArrayList<>();
    ArrayList<Image> menuImages = new ArrayList<>();
    Button haveBeen, bookmark, map;
    RatingBar ratingBar;
    TextView ratingTxt, name, location, openTime, galleryImageCount, hp, address, bookmarkCount, beenThereCount;
    ImageView cafeImg;
    Double latitude, longitude;
    LinearLayout galleryWrapper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_rest);

        haveBeen = (Button) findViewById(R.id.haveBeen);
        bookmark = (Button) findViewById(R.id.bookmark);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingTxt = (TextView) findViewById(R.id.rating_Txt);
        cafeImg = (ImageView) findViewById(R.id.cafe_img);
        name = (TextView) findViewById(R.id.cafe_name);
        location = (TextView) findViewById(R.id.cafe_location);
        openTime = (TextView) findViewById(R.id.open_time);
        galleryImageCount = (TextView) findViewById(R.id.gallery_image_count);
        hp = (TextView) findViewById(R.id.hp);
        address = (TextView) findViewById(R.id.address);
        bookmarkCount = (TextView) findViewById(R.id.bookmark_count);
        beenThereCount = (TextView) findViewById(R.id.been_there_count);
        map = (Button) findViewById(R.id.map);
        galleryWrapper = (LinearLayout) findViewById(R.id.img_wrapper);

        int cafe_id = getIntent().getIntExtra("cafe_id", 0);

        haveBeen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ratingBar.setVisibility(View.VISIBLE);
                ratingTxt.setVisibility(View.VISIBLE);
            }
        });

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Log.d(TAG, "onRatingChanged: " + rating + " " + ratingBar.getRating());
            }
        });

        menuRecyclerView = (RecyclerView) findViewById(R.id.menuRv);
        menuRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        galleryRecyclerView = (RecyclerView) findViewById(R.id.galleryRv);
        galleryRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        reqJson(cafe_id);
        reqMenu(cafe_id);
        reqGallery(cafe_id);

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

    private void reqJson(int cafe_id) {
        MyInterface service = new RetrofitBuilder(getApplicationContext()).getRetrofit().create(MyInterface.class);
        Call<CafeModel> call = service.getCafeDetail(cafe_id);
        call.enqueue(new Callback<CafeModel>() {
            @Override
            public void onResponse(Call<CafeModel> call, Response<CafeModel> response) {
                CafeModel model = response.body();

                name.setText(model.getNama());
                location.setText(model.getLokasi());
                openTime.setText(model.getJam());
                hp.setText(model.getTlp());
                address.setText(model.getAlamat());
                bookmarkCount.setText(Integer.toString(model.getBookmark()) + " Bookmarks");
                beenThereCount.setText(Integer.toString(model.getHave_here()) + " Been There");
                latitude = model.getLat();
                longitude = model.getLng();

                Glide.with(getApplicationContext())
                        .load(RetrofitBuilder.BaseUrl + "img/" + model.getGambar())
                        .into(cafeImg);

                map.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String uri = String.format(Locale.ENGLISH, "geo:%f, %f", latitude, longitude);
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onFailure(Call<CafeModel> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
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
                if (models.size() <= 3) {
                    galleryWrapper.setVisibility(View.GONE);
                } else {
                    galleryImageCount.setText(Integer.toHexString(models.size() - 3) + "+ Images");
                }


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
