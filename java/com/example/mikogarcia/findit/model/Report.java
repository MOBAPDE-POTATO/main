package com.example.mikogarcia.findit.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Miko Garcia on 3/6/2016.
 */
public class Report {

    public static final String TABLE_NAME = "reports";
    public static final String COLUMN_ID = "report_id";
    public static final String COLUMN_ITEM_NAME = "item_name";
    public static final String COLUMN_REPORT_PLACE = "report_place";
    public static final String COLUMN_REPORT_DATE = "report_date";
    public static final String COLUMN_ITEM_TYPE = "item_type";
    public static final String COLUMN_REPORT_TYPE = "report_type";
    public static final String COLUMN_CLAIMED = "claimed";
    public static final String COLUMN_LOG_DATE = "log_date";
    public static final String COLUMN_FEATURES = Feature.TABLE_NAME + "[]";

    public static final int REPORT_TYPE_LOST = 1;
    public static final int REPORT_TYPE_FOUND = 2;
    public static final int ITEM_TYPE_ID = 1;
    public static final int ITEM_TYPE_MONEY = 2;
    public static final int ITEM_TYPE_GADGET = 3;
    public static final int ITEM_TYPE_OTHER = 4;

    private int id;
    private String itemName;
    private String place;
    private Date date;
    private int itemType;
    private int reportType;
    private boolean claimed;
    private Date logDate;
    private ArrayList<Feature> features;

    private String formalDate;

    public Report(int id, String item, String place, Date date, ArrayList features,
                  int reportType, int itemType, boolean claimed, Date logDate) {
        this.id = id;
        this.itemName = item;
        this.place = place;
        this.date = date;
        this.reportType = reportType;
        this.itemType = itemType;
        this.logDate = logDate;
        this.claimed = claimed;
        this.features = features;

        setReportDateString(date);
    }

    public Report(String item, String place, Date date,
                  int reportType, int itemType, ArrayList features) {
        this.itemName = item;
        this.place = place;
        this.date = date;
        this.reportType = reportType;
        this.itemType = itemType;
        this.features = features;

        setReportDateString(date);
    }

    public Report(JSONObject json) throws JSONException {
        this.id = json.getInt(COLUMN_ID);
        this.itemName = json.getString(COLUMN_ITEM_NAME);
        this.place = json.getString(COLUMN_REPORT_PLACE);
        this.date = Date.valueOf(json.getString(COLUMN_REPORT_DATE));
        this.itemType = json.getInt(COLUMN_ITEM_TYPE);
        this.reportType = json.getInt(COLUMN_REPORT_TYPE);
        this.claimed = json.getInt(COLUMN_CLAIMED) == 1 ? true : false;
        this.logDate = Date.valueOf(json.getString(COLUMN_LOG_DATE));
        this.features = new ArrayList<>();

        try {
            JSONArray array = json.getJSONArray(Feature.TABLE_NAME);

            for (int i = 0; i < array.length(); i++) {
                JSONObject feat = array.getJSONObject(i);

                this.features.add(new Feature(feat));
            }
        }catch (Exception e) {
            // This just means walang features ung report
        }

        setReportDateString(date);
    }

    public String getReportDateString() {
        return formalDate;
    }

    public void setReportDateString(Date date) {
        final String[] months = new String[]{"Jan.", "Feb.", "Mar.", "Apr.", "May",
                "Jun.", "Jul.", "Aug.", "Sept.", "Oct.",
                "Nov.", "Dec."};

        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);

        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int year = calendar.get(Calendar.YEAR);

        this.formalDate = months[month] + " " + day + ", " + year;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public int getReportType() {
        return reportType;
    }

    public void setReportType(int reportType) {
        this.reportType = reportType;
    }

    public boolean isClaimed() {
        return claimed;
    }

    public void setClaimed(boolean claimed) {
        this.claimed = claimed;
    }

    public Date getLogDate() {
        return logDate;
    }

    public void setLogDate(Date logDate) {
        this.logDate = logDate;
    }

    public List<Feature> getFeatures() {
        return features;
    }

    public void addFeature(Feature feature) {
        this.features.add(feature);
    }

    public void deleteFeature(Feature feature) { this.features.remove(feature); }
}
