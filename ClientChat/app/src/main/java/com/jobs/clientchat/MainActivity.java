package com.jobs.clientchat;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {



    MessagesFragment messagesFragment;
    AccordFragment accordFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Memory.viewPager = findViewById(R.id.viewpager);
        Memory.viewPager.setScrollDurationFactor(0.5f);
        //viewpager.setCurrentItem(number);
        Memory.viewPager.setOffscreenPageLimit(2);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        messagesFragment = new MessagesFragment();
        accordFragment = new AccordFragment();
        adapter.addFragment(messagesFragment);
        adapter.addFragment(accordFragment);
        Memory.viewPager.setAdapter(adapter);
    }
}

class ViewPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();

    ViewPagerAdapter(FragmentManager manager) {
        super(manager);
    }
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    void addFragment(Fragment fragment) {
        mFragmentList.add(fragment);
    }

}