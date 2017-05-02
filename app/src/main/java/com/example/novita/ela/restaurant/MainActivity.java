package com.example.novita.ela.restaurant;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.novita.ela.restaurant.Model.CafeModel;
import com.example.novita.ela.restaurant.Model.UserModel;
import com.example.novita.ela.restaurant.adapter.RestaurantListAdapter;
import com.example.novita.ela.restaurant.helper.MyInterface;
import com.example.novita.ela.restaurant.helper.MySharedPreference;
import com.example.novita.ela.restaurant.helper.OnItemClickListener;
import com.example.novita.ela.restaurant.helper.RetrofitBuilder;
import com.facebook.AccessToken;
import com.facebook.FacebookRequestError;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemSelectedListener {
    String TAG = "respon";
    RecyclerView recyclerView;
    RestaurantListAdapter adapter;
    View header, loginHeader, header2;
    NavigationView navigationView;
    DrawerLayout drawer;
    MySharedPreference sf;
    boolean login;
    Spinner sortingSpinner;
    SharedPreferences prefs = null;
    int count = 0;
    String fid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fid = getIntent().getStringExtra("id");

        recyclerView = (RecyclerView) findViewById(R.id.resRv);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        sf = new MySharedPreference(getApplicationContext());
        login = sf.getStatus();

        sortingSpinner = (Spinner) findViewById(R.id.spinner);
        sortingSpinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> sortingAdapter = ArrayAdapter.createFromResource(this, R.array.sorting, android.R.layout.simple_spinner_item);
        sortingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortingSpinner.setAdapter(sortingAdapter);

//        cekRating();

        reqJson();
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (login) {
            getUserDetail();
        } else {
            Log.d(TAG, "onCreate: " + sf.getStatus());

            loginHeader = LayoutInflater.from(getApplicationContext()).inflate(R.layout.before_login, null, false);
            navigationView.addHeaderView(loginHeader);
            header2 = navigationView.getHeaderView(0);

            Button login = (Button) header2.findViewById(R.id.login);
            CircleImageView userImg = (CircleImageView) header2.findViewById(R.id.userImg);
            Glide.with(getApplicationContext()).load(RetrofitBuilder.BaseUrl + "img/person.png").into(userImg);
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), StartActivity.class);
                    intent.putExtra("status", "second");
                    startActivity(intent);
                }
            });
        }

    }


    private void reqJson() {
        MyInterface service = new RetrofitBuilder(getApplicationContext()).getRetrofit().create(MyInterface.class);
        Call<List<CafeModel>> call = service.getCafeList();
        call.enqueue(new Callback<List<CafeModel>>() {
            @Override
            public void onResponse(Call<List<CafeModel>> call, final Response<List<CafeModel>> response) {
                adapter = new RestaurantListAdapter(response.body(), getApplicationContext(),  new OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position, boolean isLongClick) {
                        List<CafeModel> models = response.body();

                        int cafe_id = models.get(position).getId();

                        Intent intent = new Intent(getApplicationContext(), DetailRestActivity.class);
                        intent.putExtra("cafe_id", cafe_id);
                        startActivity(intent);
                    }
                });

                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<List<CafeModel>> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.marked) {
            goToActivity(MarkedActivity.class);
        } else if (id == R.id.bookmark) {
            goToActivity(BookmarkActivity.class);
        } else if (id == R.id.setting) {
            goToActivity(EditActivity.class);
        } else if (id == R.id.logout) {
            sf.setStatus(false);
            logoutFb();
            goToActivity(MainActivity.class);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getUserDetail() {
        MyInterface service = new RetrofitBuilder(getApplicationContext()).getRetrofit().create(MyInterface.class);
        Call<UserModel> call = service.userDetail(sf.getId());
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                loginHeader = LayoutInflater.from(getApplicationContext()).inflate(R.layout.nav_header_main, null, false);
                navigationView.addHeaderView(loginHeader);
                header = navigationView.getHeaderView(0);
                UserModel model = response.body();
                navigationView.inflateMenu(R.menu.activity_main_drawer);

                TextView username = (TextView) header.findViewById(R.id.username);
                CircleImageView userImg = (CircleImageView) header.findViewById(R.id.user_img);
                username.setText(model.getUsername());
                String imgUrl = model.getPelanggan().getGambar();
                if (imgUrl != null) {
                    Glide.with(getApplicationContext()).load(RetrofitBuilder.BaseUrl + "img/" + imgUrl)
                            .into(userImg);
                }

            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {

            }
        });
    }

    private void reqCafeLikes() {
        MyInterface service = new RetrofitBuilder(getApplicationContext()).getRetrofit().create(MyInterface.class);
        Call<List<CafeModel>> call = service.getCafeListLikes();
        call.enqueue(new Callback<List<CafeModel>>() {
            @Override
            public void onResponse(Call<List<CafeModel>> call, final Response<List<CafeModel>> response) {
                adapter = new RestaurantListAdapter(response.body(), getApplicationContext(), new OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position, boolean isLongClick) {
                        List<CafeModel> models = response.body();

                        int cafe_id = models.get(position).getId();

                        Intent intent = new Intent(getApplicationContext(), DetailRestActivity.class);
                        intent.putExtra("cafe_id", cafe_id);
                        startActivity(intent);
                    }
                });

                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<List<CafeModel>> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    private void reqCafeAlpha() {
        MyInterface service = new RetrofitBuilder(getApplicationContext()).getRetrofit().create(MyInterface.class);
        Call<List<CafeModel>> call = service.getCafeListAlpha();
        call.enqueue(new Callback<List<CafeModel>>() {
            @Override
            public void onResponse(Call<List<CafeModel>> call, final Response<List<CafeModel>> response) {
                adapter = new RestaurantListAdapter(response.body(), getApplicationContext(), new OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position, boolean isLongClick) {
                        List<CafeModel> models = response.body();

                        int cafe_id = models.get(position).getId();

                        Intent intent = new Intent(getApplicationContext(), DetailRestActivity.class);
                        intent.putExtra("cafe_id", cafe_id);
                        startActivity(intent);
                    }
                });

                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<List<CafeModel>> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    private void goToActivity(Class activity) {
        Intent intent = new Intent(getApplicationContext(), activity);
        startActivity(intent);
    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        Log.d(TAG, "onItemSelected: " + item);


        if (item.matches("Likes")) {
            reqCafeLikes();
        } else if (item.matches("Alphabet")) {
            reqCafeAlpha();
        } else {
            reqJson();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //
    }

    private void logoutFb() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null) {
            LoginManager.getInstance().logOut();
        }

    }

}
