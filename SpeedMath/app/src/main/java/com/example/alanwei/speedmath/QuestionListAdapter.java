package com.example.alanwei.speedmath;

import android.graphics.Color;
import android.widget.ArrayAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.*;

/**
 * Created by alanwei on 25/04/15.
 */
public class QuestionListAdapter extends ArrayAdapter<String>{

    private final Context context;
    private final String[] values;
    private final Boolean[] rights;
    private int count = 0;
    public int penaltyTime = 0;
    public boolean finish = false;

    public QuestionListAdapter(Context context, String[] values, Boolean[] rights) {
        super(context, R.layout.question_list_layout, values);
        this.context = context;
        this.values = values;
        this.rights = rights;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.question_list_layout, parent, false);
        TextView questionItem = (TextView) rowView.findViewById(R.id.question_list_item);
        final Button rightIcon = (Button) rowView.findViewById(R.id.right_icon);
        final Button wrongIcon = (Button) rowView.findViewById(R.id.wrong_icon);

        rightIcon.setTag(position);
        wrongIcon.setTag(position);
        questionItem.setText(values[position]);

        rightIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rightIcon.setAlpha(1f);
                rightIcon.setEnabled(false);
                wrongIcon.setEnabled(false);
                count += 1;

                if (rights[position] == false){
                    penaltyTime += 3;
                }

                if (count == 20){
                    finish = true;
                }
            }
        });

        wrongIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wrongIcon.setAlpha(1f);
                rightIcon.setEnabled(false);
                wrongIcon.setEnabled(false);
                count += 1;

                if (rights[position] == true){
                    penaltyTime += 3;
                }

                if (count == 20){
                    finish = true;
                }
            }
        });

        return rowView;
    }


}
