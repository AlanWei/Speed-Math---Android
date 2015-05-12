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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.util.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alanwei on 25/04/15.
 */


public class QuestionListAdapter extends ArrayAdapter<ListItem>{

    private final Context context;
    private final ListItem[] values;
    private int count = 0;
    public int penaltyTime = 0;
    public boolean finish = false;
    private static LayoutInflater inflater=null;
    private List<Float> userSelect = new ArrayList<Float>();

    public Integer rightAnswer = 0;
    public Integer wrongAnswer = 0;

    public boolean[] corrects = new boolean[20];


    public QuestionListAdapter(Context context, ListItem[] values) {
        super(context, R.layout.question_list_layout, values);
        this.context = context;
        this.values = values;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (int i=0; i<20;i++){
            userSelect.add(0.3f);
        }
    }

    public static class ViewHolder{
        public TextView questionItem;
        public Button rightIcon;
        public Button wrongIcon;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        ViewHolder vh = new ViewHolder();

        if (vi == null){

            vi = inflater.inflate(R.layout.question_list_layout, parent, false);
            vh = new ViewHolder();

            vh.questionItem = (TextView) vi.findViewById(R.id.question_list_item);
            vh.rightIcon = (Button) vi.findViewById(R.id.right_icon);
            vh.wrongIcon = (Button) vi.findViewById(R.id.wrong_icon);

            vh.rightIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position = (Integer) v.getTag();
                    values[position].b1.setAlpha(1f);
                    values[position].b1.setEnabled(false);
                    values[position].b2.setEnabled(false);
                    v.setAlpha(values[position].b1.getAlpha());
                    v.setEnabled(values[position].b1.isEnabled());

                    LinearLayout ly = (LinearLayout) v.getParent();
                    Button b = (Button) ly.findViewById(R.id.wrong_icon);
                    b.setEnabled(values[position].b2.isEnabled());


                    count += 1;

                    if (values[position].right == false){
                        penaltyTime += 3;
                        wrongAnswer += 1;
                        corrects[position] = false;
                    }
                    else {
                        rightAnswer += 1;
                        corrects[position] = true;
                    }

                    Log.i("row", Boolean.toString(values[position].right));

                    if (count == 20){
                        finish = true;
                    }
                }
            });

            vh.wrongIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position = (Integer) v.getTag();

                    values[position].b2.setAlpha(1f);
                    values[position].b1.setEnabled(false);
                    values[position].b2.setEnabled(false);
                    v.setAlpha(values[position].b2.getAlpha());
                    v.setEnabled(values[position].b2.isEnabled());

                    LinearLayout ly = (LinearLayout) v.getParent();
                    Button b = (Button) ly.findViewById(R.id.right_icon);
                    b.setEnabled(values[position].b1.isEnabled());

                    count += 1;

                    if (values[position].right == true){
                        penaltyTime += 3;
                        wrongAnswer += 1;
                        corrects[position] = false;
                    }
                    else
                    {
                        rightAnswer += 1;
                        corrects[position] = true;
                    }

                    if (count == 20){
                        finish = true;
                    }
                    Log.i("row", Boolean.toString(values[position].right));
                }
            });
            vi.setTag(vh);
        }
        else{
            vh = (ViewHolder) vi.getTag();
        }


        vh.rightIcon.setTag(position);
        vh.wrongIcon.setTag(position);

        vh.questionItem.setText(values[position].s);
        vh.rightIcon.setAlpha(values[position].b1.getAlpha());
        vh.wrongIcon.setAlpha(values[position].b2.getAlpha());
        vh.rightIcon.setEnabled(values[position].b1.isEnabled());
        vh.wrongIcon.setEnabled(values[position].b2.isEnabled());

        return vi;
    }
}
