package com.example.mikogarcia.findit.model;

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

    public Feature(String feat) {
        this.feat = feat;
    }
}
