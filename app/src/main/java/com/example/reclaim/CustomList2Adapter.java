package com.example.reclaim;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
//REFERENCE:https://stackoverflow.com/questions/20880841/how-to-add-imageview-array-to-arrayadapter-for-a-listview
//https://ibytecode.com/blog/custom-listview-with-image-and-text-using-with-arrayadapter/
//https://www.youtube.com/watch?v=oQtNbk_ounk
public class CustomList2Adapter extends ArrayAdapter<String> {
    public CustomList2Adapter(Context context, List<String> foods) {
        super(context, 0, foods);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Checks if an existing view is being used
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_list_2, parent, false);
        }
        TextView textViewCalorie = convertView.findViewById(R.id.textViewCalorieItem);
        textViewCalorie.setText(getItem(position));

        return convertView;
    }
}
//reference complete
