package com.example.app;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class MyAdapter extends FragmentPagerAdapter {
    private Context myContext;
    int totalTabs;

    public MyAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                BlankFragment blankFragment = new BlankFragment();
                return blankFragment;
            case 1:
                chat chat = new chat();
                return chat;
            case 2:
                home home = new home();
                return home;

default:
            return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
