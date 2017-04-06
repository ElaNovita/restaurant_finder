package com.example.novita.ela.restaurant.Model;

/**
 * Created by elaa on 03/04/17.
 */

public class MenuModel {
    int id, cafe_id;
    String nama, harga, gambar;

    public MenuModel(int id, int cafe_id, String nama, String harga, String gambar) {
        this.id = id;
        this.cafe_id = cafe_id;
        this.nama = nama;
        this.harga = harga;
        this.gambar = gambar;
    }

    public int getId() {
        return id;
    }

    public int getCafe_id() {
        return cafe_id;
    }

    public String getNama() {
        return nama;
    }

    public String getHarga() {
        return harga;
    }

    public String getGambar() {
        return gambar;
    }
}
