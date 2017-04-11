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

import com.example.novita.ela.restaurant.Model.RegisterModel;
import com.example.novita.ela.restaurant.helper.MyInterface;
import com.example.novita.ela.restaurant.helper.RetrofitBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    EditText username, password, email;
    Button signup;
    TextView toLogin;
    String _u, _p, _e, _g = "person.png";
    final String TAG = "respon";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        email = (EditText) findViewById(R.id.email);
        signup = (Button) findViewById(R.id.signup);
        toLogin = (TextView) findViewById(R.id.to_login);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _u = username.getText().toString();
                _p = password.getText().toString();
                _e = email.getText().toString();
                reqJson();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void reqJson() {
        MyInterface service = new RetrofitBuilder(getApplicationContext()).getRetrofit().create(MyInterface.class);
        Call<RegisterModel> call = service.register(_u, _p, _e, _g);
        call.enqueue(new Callback<RegisterModel>() {
            @Override
            public void onResponse(Call<RegisterModel> call, Response<RegisterModel> response) {
                Toast.makeText(getApplicationContext(), "Sign Up Success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<RegisterModel> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                Toast.makeText(getApplicationContext(), "Sign Up Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
