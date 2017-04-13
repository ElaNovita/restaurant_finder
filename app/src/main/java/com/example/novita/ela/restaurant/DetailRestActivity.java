package com.example.novita.ela.restaurant;

import android.app.Activity;
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
import com.example.novita.ela.restaurant.Model.BookmarkModel;
import com.example.novita.ela.restaurant.Model.CafeModel;
import com.example.novita.ela.restaurant.Model.GalleryModel;
import com.example.novita.ela.restaurant.Model.LikeModel;
import com.example.novita.ela.restaurant.Model.MarkModel;
import com.example.novita.ela.restaurant.Model.MenuModel;
import com.example.novita.ela.restaurant.Model.RatingModel;
import com.example.novita.ela.restaurant.adapter.GalleryAdapter;
import com.example.novita.ela.restaurant.adapter.MenuAdapter;
import com.example.novita.ela.restaurant.helper.Image;
import com.example.novita.ela.restaurant.helper.MyInterface;
import com.example.novita.ela.restaurant.helper.MySharedPreference;
import com.example.novita.ela.restaurant.helper.OnItemClickListener;
import com.example.novita.ela.restaurant.helper.RealPathUtil;
import com.example.novita.ela.restaurant.helper.RetrofitBuilder;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
    Button haveBeen, bookmark, map, addPhoto;
    RatingBar ratingBar;
    TextView ratingTxt, name, location, openTime, galleryImageCount, hp, address, bookmarkCount, beenThereCount;
    ImageView cafeImg;
    Double latitude, longitude;
    LinearLayout galleryWrapper;
    Float ratingValue = 0f;
    String s, bookmarkStatus = "tdk ada", markedStatus = "tdk ada";
    Button ok;
    Boolean login;
    private static final int PICK_IMAGE_REQUEST = 1;
    MySharedPreference sf;
    int _beenThere, cafe_id, user_id;
    MultipartBody.Part requestFileBody;
    RequestBody _id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_rest);
        sf = new MySharedPreference(getApplicationContext());
        login = sf.getStatus();

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
        ok = (Button) findViewById(R.id.ok);
        addPhoto = (Button) findViewById(R.id.addPhoto);

        cafe_id = getIntent().getIntExtra("cafe_id", 0);
        _id = RequestBody.create(MediaType.parse("multipart/form-data"), Integer.toString(cafe_id));
        if (login) {
            user_id = sf.getId();
        }

        cekBookmark();
        cekMarked();

        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (login) {
                    Log.d(TAG, "onClick: " + bookmarkStatus);
                    setBookmark();
                    if (bookmarkStatus.matches("tdk ada")) {
                        Toast.makeText(getApplicationContext(), "Added to bookmark", Toast.LENGTH_SHORT).show();
                        bookmarkStatus = "ada";
                        bookmark.setBackgroundResource(R.drawable.marked_button);
                        bookmark.setText("Bookmarked");
                    } else {
                        Toast.makeText(getApplicationContext(), "Already in bookmark list", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Intent intent = new Intent(getApplicationContext(), StartActivity.class);
                    intent.putExtra("status", "second");
                    startActivity(intent);
                }
            }
        });

        haveBeen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (login) {
                    reqMark(cafe_id, user_id);

                    if (markedStatus.matches("tdk ada")) {
                        Toast.makeText(getApplicationContext(), "Thank You!", Toast.LENGTH_SHORT).show();
                        markedStatus = "ada";
                        haveBeen.setBackgroundResource(R.drawable.marked_button);
                        haveBeen.setText("Visited Place");
                    } else {
                        Toast.makeText(getApplicationContext(), "Already in visited place", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Intent intent = new Intent(getApplicationContext(), StartActivity.class);
                    intent.putExtra("status", "second");
                    startActivity(intent);
                }
            }
        });

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ratingValue = rating;
                if (login) {
                    ok.setVisibility(View.VISIBLE);
                } else {
                    Intent intent = new Intent(getApplicationContext(), StartActivity.class);
                    intent.putExtra("status", "second");
                    startActivity(intent);
                }

            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Float f = new Float(ratingBar.getRating());
                s = Float.toString(f);
                reqRating(cafe_id, Double.valueOf(s));
                Log.d(TAG, "onClick: " + ratingBar.getRating());

            }
        });

        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (login) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
                } else {
                    Intent intent = new Intent(getApplicationContext(), StartActivity.class);
                    intent.putExtra("status", "second");
                    startActivity(intent);
                }

            }
        });


        menuRecyclerView = (RecyclerView) findViewById(R.id.menuRv);
        menuRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        galleryRecyclerView = (RecyclerView) findViewById(R.id.galleryRv);
        galleryRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        reqJson(cafe_id);
        reqMenu(cafe_id);
        reqGallery(cafe_id);
        cekRating();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null
                && data.getData() != null) {

            String realPath = RealPathUtil.getPath(this, data.getData());


            File file = new File(realPath);

            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
            requestFileBody = MultipartBody.Part.createFormData("gambar", file.getName(), requestFile);

            uploadPhoto(requestFileBody);

            Log.d("respon", "onActivityResult: " + realPath);
        }
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
                _beenThere = model.getHave_here();

                name.setText(model.getNama());
                location.setText(model.getLokasi());
                openTime.setText(model.getJam());
                hp.setText(model.getTlp());
                address.setText(model.getAlamat());
                bookmarkCount.setText(Integer.toString(model.getBookmark()) + " Bookmarks");
                beenThereCount.setText(Integer.toString(_beenThere) + " Been There");
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

    private void reqRating(int cafe_id, Double rating) {
        MyInterface service = new RetrofitBuilder(getApplicationContext()).getRetrofit().create(MyInterface.class);
        Call<CafeModel> call = service.rating(cafe_id, user_id, rating);
        call.enqueue(new Callback<CafeModel>() {
            @Override
            public void onResponse(Call<CafeModel> call, Response<CafeModel> response) {
                Log.d(TAG, "onResponse: " + response.code());
                Log.d(TAG, "onResponse: " + response.body().getRating());
                Toast.makeText(getApplicationContext(), "Sukses Memberi Rating", Toast.LENGTH_SHORT).show();
                ok.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<CafeModel> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }


    private void reqMark(final int cafe_id, final int user_id) {
        MyInterface service = new RetrofitBuilder(getApplicationContext()).getRetrofit().create(MyInterface.class);
        Call<CafeModel> call = service.mark(cafe_id, user_id);
        call.enqueue(new Callback<CafeModel>() {
            @Override
            public void onResponse(Call<CafeModel> call, Response<CafeModel> response) {
                CafeModel model = response.body();
                if (model.isStatus()) {
                    Toast.makeText(getApplicationContext(), "Thank You!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "You have been here before", Toast.LENGTH_SHORT).show();
                    beenThereCount.setText(Integer.toString(_beenThere) + " Been There");
                }
            }

            @Override
            public void onFailure(Call<CafeModel> call, Throwable t) {

            }
        });
    }

    private void uploadPhoto(MultipartBody.Part req) {
        MyInterface service = new RetrofitBuilder(getApplicationContext()).getRetrofit().create(MyInterface.class);
        Call<GalleryModel> call = service.uploadPhoto(_id, req);
        call.enqueue(new Callback<GalleryModel>() {
            @Override
            public void onResponse(Call<GalleryModel> call, Response<GalleryModel> response) {
                Log.d(TAG, "onResponse: " + response.code());
                Log.d(TAG, "onResponse: " + response.body().getGambar());
                if (response.code() == 200) {
                    finish();
                    Intent intent = new Intent(getApplicationContext(), DetailRestActivity.class);
                    intent.putExtra("cafe_id", cafe_id);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<GalleryModel> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    private void setBookmark() {
        MyInterface service = new RetrofitBuilder(getApplicationContext()).getRetrofit().create(MyInterface.class);
        Call<BookmarkModel> call = service.setBookmark(cafe_id, user_id);
        call.enqueue(new Callback<BookmarkModel>() {
            @Override
            public void onResponse(Call<BookmarkModel> call, Response<BookmarkModel> response) {
                Log.d(TAG, "onResponse: set " + response.code());
                Log.d(TAG, "onResponse: set " + response.body().isStatus());
                Log.d(TAG, "onResponse: set" + response.body().getCafe_id());
                Log.d(TAG, "onResponse: set" + response.body().getUser_id());
                if (response.body().isStatus()) {
                    bookmarkStatus = "ada";
                    Log.d(TAG, "onResponse: set " + response.body().isStatus());

                } else {
                    bookmarkStatus = "tdk ada";
                }
            }

            @Override
            public void onFailure(Call<BookmarkModel> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    private void cekBookmark() {
        MyInterface service = new RetrofitBuilder(getApplicationContext()).getRetrofit().create(MyInterface.class);
        Call<BookmarkModel> call = service.cekBookmark(cafe_id, user_id);
        call.enqueue(new Callback<BookmarkModel>() {
            @Override
            public void onResponse(Call<BookmarkModel> call, Response<BookmarkModel> response) {
                Log.d(TAG, "onResponse: cek " + response.body().isStatus());
                BookmarkModel model = response.body();
                if (model.isStatus()) {
                    bookmark.setBackgroundResource(R.drawable.marked_button);
                    bookmark.setText("Bookmarked");
                    bookmarkStatus = "ada";
                } else {
                    bookmarkStatus = "tdk ada";
                }
            }

            @Override
            public void onFailure(Call<BookmarkModel> call, Throwable t) {

            }
        });
    }

    private void cekMarked() {
        MyInterface service = new RetrofitBuilder(getApplicationContext()).getRetrofit().create(MyInterface.class);
        Call<BookmarkModel> call = service.cekMarked(cafe_id, user_id);
        call.enqueue(new Callback<BookmarkModel>() {
            @Override
            public void onResponse(Call<BookmarkModel> call, Response<BookmarkModel> response) {
                Log.d(TAG, "onResponse: cek " + response.body().isStatus());
                BookmarkModel model = response.body();
                if (model.isStatus()) {
                    haveBeen.setBackgroundResource(R.drawable.marked_button);
                    haveBeen.setText("Visited Place");
                    markedStatus = "ada";
                } else {
                    markedStatus = "tdk ada";
                }
            }

            @Override
            public void onFailure(Call<BookmarkModel> call, Throwable t) {

            }
        });
    }

    private void cekRating() {
        MyInterface service = new RetrofitBuilder(getApplicationContext()).getRetrofit().create(MyInterface.class);
        Call<RatingModel> call = service.setRating(user_id, cafe_id);
        call.enqueue(new Callback<RatingModel>() {
            @Override
            public void onResponse(Call<RatingModel> call, Response<RatingModel> response) {
                Log.d(TAG, "onResponse: " + response.body().isStatus());
                RatingModel model = response.body();
                if (model.isStatus()) {
                    double d = model.getValue();
                    float f = (float) d;
                    ratingBar.setRating(f);
                }
            }

            @Override
            public void onFailure(Call<RatingModel> call, Throwable t) {

            }
        });
    }
}

