package kat.android.com.movielist.rest.pojo.userdatails.accountstate;

import com.google.gson.annotations.SerializedName;

//Using GSON to convert json to java object
public class Rated {

    @SerializedName("value")
    private int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
