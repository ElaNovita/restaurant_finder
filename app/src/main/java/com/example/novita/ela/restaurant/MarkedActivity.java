package com.example.novita.ela.restaurant;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.example.novita.ela.restaurant.Model.CafeModel;
import com.example.novita.ela.restaurant.adapter.BookmarkAdapter;
import com.example.novita.ela.restaurant.helper.MyInterface;
import com.example.novita.ela.restaurant.helper.MySharedPreference;
import com.example.novita.ela.restaurant.helper.RetrofitBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MarkedActivity extends AppCompatActivity {

    TextView empty;
    RecyclerView recyclerView;
    BookmarkAdapter adapter;
    MySharedPreference sf;
    String TAG = "respon";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);
        sf = new MySharedPreference(getApplicationContext());

        empty = (TextView) findViewById(R.id.empty);
        recyclerView = (RecyclerView) findViewById(R.id.bookmarkRv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        reqJson(sf.getId());
    }

    private void reqJson(int user_id) {
        MyInterface service = new RetrofitBuilder(getApplicationContext()).getRetrofit().create(MyInterface.class);
        Call<List<CafeModel>> call = service.getMarkList(user_id);
        call.enqueue(new Callback<List<CafeModel>>() {
            @Override
            public void onResponse(Call<List<CafeModel>> call, Response<List<CafeModel>> response) {
                Log.d(TAG, "onResponse: " + response.code());
                List<CafeModel> models = response.body();
                adapter = new BookmarkAdapter(models, getApplicationContext());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<CafeModel>> call, Throwable t) {

            }
        });
    }
}
