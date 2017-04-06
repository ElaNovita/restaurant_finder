package com.example.novita.ela.restaurant.Model;

/**
 * Created by elaa on 03/04/17.
 */

public class UserModel {
    int id;
    String username, password, status, reg_id;
    PelangganModel pelanggan;


    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getStatus() {
        return status;
    }

    public PelangganModel getPelanggan() {
        return pelanggan;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPelanggan(PelangganModel pelanggan) {
        this.pelanggan = pelanggan;
    }

    public String getReg_id() {
        return reg_id;
    }

    public void setReg_id(String reg_id) {
        this.reg_id = reg_id;
    }
}
