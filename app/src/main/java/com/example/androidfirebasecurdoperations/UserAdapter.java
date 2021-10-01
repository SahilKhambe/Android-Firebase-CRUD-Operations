package com.example.androidfirebasecurdoperations;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class UserAdapter extends ArrayAdapter<User> {

    Activity context;
    List<User> list;

    public UserAdapter(Activity context, List<User> list) {
        super(context, R.layout.activity_user, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = context.getLayoutInflater();
        convertView = layoutInflater.inflate(R.layout.user_list_layout, null, true);

        TextView name_ = (TextView) convertView.findViewById(R.id.txt_name);
        TextView email_ = (TextView) convertView.findViewById(R.id.txt_email);
        TextView contact_ = (TextView) convertView.findViewById(R.id.txt_contact);
        TextView city_ = (TextView) convertView.findViewById(R.id.txt_city);
        TextView lang_ = (TextView) convertView.findViewById(R.id.txt_lang);

        User user = list.get(position);

        name_.setText(user.getName());
        email_.setText(user.getEmail());
        contact_.setText(user.getContact());
        city_.setText(user.getCity());
        lang_.setText(user.getLanguage());

        return convertView;

    }
}