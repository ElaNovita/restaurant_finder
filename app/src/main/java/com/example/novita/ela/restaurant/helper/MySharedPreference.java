package com.example.novita.ela.restaurant.helper;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by elaa on 02/04/17.
 */

public class MySharedPreference extends Application {

    private final String myPref = "myPrefs";
    private SharedPreferences sp;
    private Context _context;
    public SharedPreferences.Editor editor;

    public MySharedPreference(Context context) {
        this._context = context;
        this.sp = _context.getSharedPreferences(myPref, MODE_PRIVATE);
        this.editor = sp.edit();
    }

    public String getToken() {
        return sp.getString("token", null);
    }

    public void setToken(String token) {
        editor.putString("token", token);
        editor.commit();
    }

    public void setUsername(String username) {
        editor.putString("username", username);
        editor.commit();
    }



    public void setStatus(boolean status) {
        editor.putBoolean("status", status);
        editor.commit();
    }

    public String getUserImage() {
        return sp.getString("url", null);
    }

    public void setUserImage(String url) {
        editor.putString("url", url);
        editor.commit();
    }

    public int getId() {
        return sp.getInt("id", 0);
    }

    public void setId(int id) {
        editor.putInt("id", id);
        editor.commit();
    }


    public boolean getStatus() {
        return sp.getBoolean("status", false);
    }

    public String getUsername() {
        return sp.getString("username", "Username");
    }

    public int getPelangganId() {
        return sp.getInt("pelanggan_id", 0);
    }

    public void setPelangganId(int id) {
        editor.putInt("pelanggan_id", id);
        editor.commit();
    }

    public String get_Name() {
        return sp.getString("name", null);
    }

    public void set_name(String name) {
        editor.putString("name", name);
        editor.commit();
    }

    public String getAlamat() {
        return sp.getString("alamat", null);
    }

    public void setAlamat(String alamat) {
        editor.putString("alamat", alamat);
        editor.commit();
    }

    public String getHp() {
        return sp.getString("hp", null);
    }

    public void setHp(String hp) {
        editor.putString("hp", hp);
        editor.commit();
    }

    public String get_Image() {
        return sp.getString("image", null);
    }

    public void set_Image(String image) {
        editor.putString("image", image);
        editor.commit();
    }

    public SharedPreferences getSharedPreferences() {
        return this.sp;
    }

    public void deleteToken() {
        editor.clear();
        editor.commit();
    }

    public void setFirebaseToken(String token) {
        editor.putString("token", token);
        editor.commit();
    }

    public String getFirebaseToken() {
        return sp.getString("token", null);
    }

    public void setEmail(String email) {
        editor.putString("email", email);
        editor.commit();
    }

    public String getEmail() {
        return sp.getString("email", null);
    }
}
