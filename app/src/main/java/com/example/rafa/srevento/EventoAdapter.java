package com.example.rafa.srevento;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Rafa on 17/01/2017.
 */

public class EventoAdapter extends ArrayAdapter<EventoItem>{
    private final Context context;
    private final ArrayList<EventoItem> values;

    public EventoAdapter(Context context, int textViewResourceId, ArrayList<EventoItem> values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.evento_row, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.evento_nombre);
        textView.setText(values.get(position).getNombre());

        return rowView;
    }
}