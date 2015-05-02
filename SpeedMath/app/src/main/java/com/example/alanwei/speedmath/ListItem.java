package com.example.alanwei.speedmath;

import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alanwei on 28/04/15.
 */
public class ListItem {

    public String s;
    public Button b1;
    public Button b2;
    public Boolean right;

    public ListItem(String s, Button b1, Button b2, Boolean right){
        this.s = s;
        this.b1 = b1;
        this.b2 = b2;
        this.right = right;
    }
}
