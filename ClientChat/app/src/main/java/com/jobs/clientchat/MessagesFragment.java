package com.jobs.clientchat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MessagesFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_messages, container, false);
        view.findViewById(R.id.chat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Memory.viewPager != null) {
                    Memory.viewPager.setScrollDurationFactor(2);
                    Memory.viewPager.setCurrentItem(1);
                    Memory.viewPager.setScrollDurationFactor(0.5f);
                }
            }
        });
        return view;
    }
}
