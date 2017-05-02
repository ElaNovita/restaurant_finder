package com.example.novita.ela.restaurant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.novita.ela.restaurant.Model.UserModel;
import com.example.novita.ela.restaurant.helper.MyInterface;
import com.example.novita.ela.restaurant.helper.MySharedPreference;
import com.example.novita.ela.restaurant.helper.RetrofitBuilder;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StartActivity extends AppCompatActivity {
    LoginButton loginButton;
    private CallbackManager callbackManager;
    String TAG = "dayat";
    Button login, signup;
    TextView skip;
    MySharedPreference sf ;
    String fbUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_start);
        sf = new MySharedPreference(getApplicationContext());

        String status = getIntent().getStringExtra("status");



        loginButton = (LoginButton) findViewById(R.id.fb_login);
        loginButton.setText("Continue with facebook");
        login = (Button) findViewById(R.id.login);
        signup = (Button) findViewById(R.id.signup);
        skip = (TextView) findViewById(R.id.skip);

        if (status.equals("first")) {
            skip.setVisibility(View.VISIBLE);
        } else {
            skip.setVisibility(View.GONE);
        }

        loginButton.setReadPermissions("email");

        callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "onSuccess: yasss" );
                Log.d(TAG, "onSuccess: " + loginResult.getAccessToken().getUserId());
                fbUserId = loginResult.getAccessToken().getUserId();
                sf.setFbUserId(fbUserId);
                new GraphRequest(
                        AccessToken.getCurrentAccessToken(), "/" + loginResult.getAccessToken().getUserId(), null,
                        HttpMethod.GET, new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        Log.d(TAG, "onCompleted: " + response.toString());
                    }
                }
                ).executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Log.e(TAG, "onError: ", error);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);

            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);

            }
        });
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        JSONObject a = response.getJSONObject();
//                        Log.d(TAG, "onCompleted: json " + a);
                        Log.d(TAG, "onCompleted: " + AccessToken.getCurrentAccessToken().getToken());



                        if (AccessToken.getCurrentAccessToken().getDeclinedPermissions().isEmpty()) {
                            try {
                                String name = a.getString("name");
                                String mail = a.getString("email");
                                String userImg = a.getJSONObject("picture").getJSONObject("data").getString("url");
                                Log.d(TAG, "onCompleted: " + mail + " " + name + " " + userImg);
                                reqJson(name, mail, userImg);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,picture");
        request.setParameters(parameters);
        request.executeAsync();

    }

    private void reqJson(String name, String mail, String url) {
        MyInterface service = new RetrofitBuilder(getApplicationContext()).getRetrofit().create(MyInterface.class);
        Call<UserModel> call = service.regFb(name, mail, url);
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {

                UserModel model = response.body();

                sf.setStatus(true);
                sf.setUsername(model.getUsername());
                sf.setEmail(model.getPelanggan().getEmail());
                sf.setPelangganId(model.getId());
                sf.setId(response.body().getId());
                sf.set_Image(model.getPelanggan().getGambar());



                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("id", fbUserId);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

}
