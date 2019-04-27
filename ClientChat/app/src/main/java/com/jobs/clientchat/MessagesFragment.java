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
        Memory.messagesViewPager = view.findViewById(R.id.messages_viewpager);
        Memory.messagesViewPager.setOffscreenPageLimit(2);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
        adapter.addFragment(new MessagesSettingsFragment());
        adapter.addFragment(new MessagesUsersFragment());
        Memory.messagesViewPager.setAdapter(adapter);
        Memory.messagesViewPager.setCurrentItem(1);
        return view;
    }
}
