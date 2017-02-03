package com.vonderland.diarydemo.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.vonderland.diarydemo.R;
import com.vonderland.diarydemo.homepage.DiaryFragment;
import com.vonderland.diarydemo.homepage.MomentFragment;

/**
 * Created by Vonderland on 2017/2/1.
 */

public class HomePagerAdapter extends FragmentPagerAdapter {
    private String[] titles;
    private DiaryFragment diaryFragment;
    private MomentFragment momentFragment;

    public HomePagerAdapter(FragmentManager fragmentManager,
                            Context context, DiaryFragment diaryFragment, MomentFragment momentFragment) {
        super(fragmentManager);
        this.diaryFragment = diaryFragment;
        this.momentFragment = momentFragment;

        this.titles = new String[]{
                context.getResources().getString(R.string.diary),
                context.getResources().getString(R.string.moment)
        };
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return diaryFragment;
        } else {
            return momentFragment;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
