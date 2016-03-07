package com.example.mikogarcia.findit.model;

import java.util.Date;

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
    private Feature[] features;

    public Report(int id, String item, String place, Date date, Feature[] features,
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
    }

    public Report(String item, String place, Date date,
                  int reportType, int itemType, Feature[] features) {
        this.itemName = item;
        this.place = place;
        this.date = date;
        this.reportType = reportType;
        this.itemType = itemType;
        this.features = features;
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

    public Feature[] getFeatures() {
        return features;
    }

    public void setFeatures(Feature[] features) {
        this.features = features;
    }
}
