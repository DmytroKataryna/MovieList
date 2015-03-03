package kat.android.com.movielist.rest.pojo.userdatails.accountstate;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Rated implements Serializable {

    @SerializedName("value")
    private float value;


    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
