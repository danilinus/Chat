package com.jobs.clientchat;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MessagesUsersFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_messages_users, container, false);
        Memory.CirclePoint = (GradientDrawable) getResources().getDrawable(R.drawable.circle_connection_info);
        return view;
    }
}