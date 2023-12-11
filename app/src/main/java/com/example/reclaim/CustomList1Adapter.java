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
public class CustomList1Adapter extends ArrayAdapter<String> {
    public CustomList1Adapter(Context context, List<String> foods) {
        super(context, 0, foods);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_list_item, parent);
        }
        TextView textViewFood = convertView.findViewById(R.id.textViewFoodItem);
        textViewFood.setText(getItem(position));
        return convertView;
    }
}
//reference complete
