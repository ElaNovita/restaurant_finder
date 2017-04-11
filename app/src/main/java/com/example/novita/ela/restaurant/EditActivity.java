package com.example.novita.ela.restaurant;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.novita.ela.restaurant.Model.UserModel;
import com.example.novita.ela.restaurant.helper.MyInterface;
import com.example.novita.ela.restaurant.helper.MySharedPreference;
import com.example.novita.ela.restaurant.helper.RealPathUtil;
import com.example.novita.ela.restaurant.helper.RetrofitBuilder;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditActivity extends AppCompatActivity {
    EditText username, email, password;
    Button save, selectImg;
    ImageView userImg;
    TextView imgUrl;
    RequestBody _u, _p, _e, _id;
    private static final int PICK_IMAGE_REQUEST = 1;
    MySharedPreference sf;
    MultipartBody.Part requestFileBody;
    String TAG = "respon";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        sf = new MySharedPreference(getApplicationContext());

        username = (EditText) findViewById(R.id.username);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        save = (Button) findViewById(R.id.save);
        selectImg = (Button) findViewById(R.id.selectImg);
        userImg = (ImageView) findViewById(R.id.userImg);
        imgUrl = (TextView) findViewById(R.id.imgUrl);

        username.setText(sf.getUsername());
        email.setText(sf.getEmail());
        imgUrl.setText(sf.get_Image());

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.getText().toString().matches("")) {
                    Toast.makeText(getApplicationContext(), "Masukkan Password", Toast.LENGTH_SHORT).show();
                } else {
                    _id = RequestBody.create(MediaType.parse("multipart/form-data"), Integer.toString(sf.getId()));
                    _u = RequestBody.create(MediaType.parse("multipart/form-data"), username.getText().toString());
                    _p = RequestBody.create(MediaType.parse("multipart/form-data"), password.getText().toString());
                    _e = RequestBody.create(MediaType.parse("multipart/form-data"), email.getText().toString());

                    reqJson();
                    Intent intent = new Intent(getApplicationContext(), BaseActivity.class);
                    startActivity(intent);
                }
            }
        });

        selectImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null
                && data.getData() != null) {

            String realPath = RealPathUtil.getPath(this, data.getData());

            imgUrl.setText(realPath);

            File file = new File(realPath);

            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
            requestFileBody = MultipartBody.Part.createFormData("gambar", file.getName(), requestFile);


            Log.d("respon", "onActivityResult: " + realPath);
        }
    }

    private void reqJson() {
        MyInterface service = new RetrofitBuilder(getApplicationContext()).getRetrofit().create(MyInterface.class);
        Call<UserModel> call = service.editProfile(_id, _u, _p, _e, requestFileBody);
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                UserModel model = response.body();
                sf.setUsername(model.getUsername());
                sf.setEmail(model.getPelanggan().getEmail());
                sf.set_Image(model.getPelanggan().getGambar());
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }
}
