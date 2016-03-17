package com.example.mikogarcia.findit.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Miko Garcia on 3/6/2016.
 */
public class Feature implements Parcelable{

    public static final String TABLE_NAME = "features";
    public static final String COLUMN_ID = "feat_id";
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

    protected Feature(Parcel in) {
        id = in.readInt();
        feat = in.readString();
    }

    public static final Creator<Feature> CREATOR = new Creator<Feature>() {
        @Override
        public Feature createFromParcel(Parcel in) {
            return new Feature(in);
        }

        @Override
        public Feature[] newArray(int size) {
            return new Feature[size];
        }
    };

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

    public String toJSONString() throws JSONException {
        JSONObject json = new JSONObject();

        json.put(COLUMN_ID, id);
        json.put(COLUMN_FEATURE, feat);

        return json.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(getId());
        dest.writeString(getFeat());
    }
}
