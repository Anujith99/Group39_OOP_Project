package com.example.food_app;

import android.content.Context;
import android.widget.ArrayAdapter;

public class SizeAdapter {
    private int selected;
    private ArrayAdapter<CharSequence> adapter;

    public SizeAdapter(Context parent) {
        adapter = ArrayAdapter.createFromResource(parent, R.array.sizes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    public ArrayAdapter<CharSequence> getAdapter() {
        return adapter;
    }

    public String getText() {
        return (String) adapter.getItem(selected);
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

}