package kat.android.com.movielist.rest.pojo.userdatails;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

//Using GSON to convert json to java object
public class Token implements Serializable {

    @SerializedName("success")
    private boolean success;

    @SerializedName("request_token")
    private String request_token;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getRequest_token() {
        return request_token;
    }

    public void setRequest_token(String request_token) {
        this.request_token = request_token;
    }
}
