package com.my.vkclient.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Rect implements Parcelable {
    public static final Creator<Rect> CREATOR = new Creator<Rect>() {
        @Override
        public Rect createFromParcel(Parcel in) {
            return new Rect(in);
        }

        @Override
        public Rect[] newArray(int size) {
            return new Rect[size];
        }
    };
    @SerializedName("x")
    private float x;
    @SerializedName("y")
    private float y;
    @SerializedName("x2")
    private float x2;
    @SerializedName("y2")
    private float y2;

    public Rect(float x, float y, float x2, float y2) {
        this.x = x;
        this.y = y;
        this.x2 = x2;
        this.y2 = y2;
    }

    protected Rect(Parcel in) {
        x = in.readFloat();
        y = in.readFloat();
        x2 = in.readFloat();
        y2 = in.readFloat();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(x);
        dest.writeFloat(y);
        dest.writeFloat(x2);
        dest.writeFloat(y2);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getX2() {
        return x2;
    }

    public void setX2(float x2) {
        this.x2 = x2;
    }

    public float getY2() {
        return y2;
    }

    public void setY2(float y2) {
        this.y2 = y2;
    }
}
