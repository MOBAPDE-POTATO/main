package com.example.mikogarcia.findit.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Miko Garcia on 3/6/2016.
 */
public class Account {

    public static final String TABLE_NAME = "accounts";
    public static final String REGISTER_ID = "gcm_id";
    public static final String COLUMN_ID = "acc_id";
    public static final String COLUMN_FNAME = "f_name";
    public static final String COLUMN_LNAME = "l_name";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_ACCOUNT_TYPE = "acc_type";

    public static final int ACCOUNT_TYPE_ADMIN = 2;
    public static final int ACCOUNT_TYPE_USER = 1;

    private int id;
    private String fname;
    private String lname;
    private String email;
    private String password;
    private int type;

    public Account(int type, int id, String fname, String lname, String email, String password) {
        this.type = type;
        this.id = id;
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.password = password;
    }

    public Account(String fname, String lname, String email, String password) {
        this.type = ACCOUNT_TYPE_USER;
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.password = password;
    }

    /**
     * creates an Account Object using the values in the JSONObject
     * @param json : The JSONObject containing the values
     * @throws JSONException : If key is not found or type mismtach
     */
    public Account(JSONObject json) throws JSONException {
        this.id = json.getInt(COLUMN_ID);
        this.fname = json.getString(COLUMN_FNAME);
        this.lname = json.getString(COLUMN_LNAME);
        this.email = json.getString(COLUMN_EMAIL);
        this.password = json.getString(COLUMN_PASSWORD);
        this.type = json.getInt(COLUMN_ACCOUNT_TYPE);
    }

    public String getName() {
        return this.fname + " " + this.lname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
