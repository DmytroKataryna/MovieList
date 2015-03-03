package kat.android.com.movielist.rest.pojo.userdatails.post;

import java.io.Serializable;

//Http POST Body
public class Rating implements Serializable {

    private double value;

    public Rating(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
