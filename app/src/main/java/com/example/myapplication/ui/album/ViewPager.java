package com.example.myapplication.ui.album;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPager extends FragmentStateAdapter {

    public ViewPager(FragmentActivity fa) {
        super(fa);
    }

    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new TrackFragment();
            case 1:
                return new DetailFragment();
            case 2:
                return new VideoFragment();
            default:
                return new TrackFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3; // 탭의 개수
    }
}


