package com.example.novita.ela.restaurant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.novita.ela.restaurant.Model.UserModel;
import com.example.novita.ela.restaurant.helper.MyInterface;
import com.example.novita.ela.restaurant.helper.MySharedPreference;
import com.example.novita.ela.restaurant.helper.RetrofitBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    Button login;
    TextView toSignUp;
    EditText username, password;
    String _username, _password;
    String TAG = "respon";
    MySharedPreference sf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = (Button) findViewById(R.id.login);
        toSignUp = (TextView) findViewById(R.id.to_sign_up);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        sf = new MySharedPreference(getApplicationContext());

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _username = username.getText().toString();
                _password = password.getText().toString();
                reqLogin();
            }
        });

        toSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void reqLogin() {
        MyInterface service = new RetrofitBuilder(getApplicationContext()).getRetrofit().create(MyInterface.class);
        Call<UserModel> call = service.login(_username, _password);
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {

                UserModel model = response.body();
                String status = model.getStatus();

                if (status.equals("success")) {
                    sf.setStatus(true);
                    sf.setUsername(_username);
                    sf.set_name(model.getPelanggan().getNama());
                    sf.setAlamat(model.getPelanggan().getAlamat());
                    sf.setHp(model.getPelanggan().getHp());
                    sf.setPelangganId(model.getId());
                    sf.setId(response.body().getId());


                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Login Gagal", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {

            }
        });
    }


}
