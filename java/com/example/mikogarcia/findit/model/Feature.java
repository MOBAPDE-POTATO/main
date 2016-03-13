package com.example.mikogarcia.findit.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Miko Garcia on 3/6/2016.
 */
public class Feature {

    public static final String TABLE_NAME = "features";
    public static final String COLUMN_ID = "feature_id";
    public static final String COLUMN_FEATURE = "feature";

    private int id;
    private String feat;

    public Feature(int id, String feat) {
        this.id = id;
        this.feat = feat;
    }

    public Feature(JSONObject json) throws JSONException {
        this.id = json.getInt(COLUMN_ID);
        this.feat = json.getString(COLUMN_FEATURE);
    }

    public Feature(String feat) {
        this.feat = feat;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFeat() {
        return feat;
    }

    public void setFeat(String feat) {
        this.feat = feat;
    }
}
