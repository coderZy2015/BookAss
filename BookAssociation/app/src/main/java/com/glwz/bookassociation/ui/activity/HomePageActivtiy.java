package com.glwz.bookassociation.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.glwz.bookassociation.R;
import com.glwz.bookassociation.ui.fragment.MainFragment;
import com.glwz.bookassociation.ui.fragment.MainPageFragment;
import com.glwz.bookassociation.ui.utils.MediaPlayControl;

import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

/**
 * 书本的目录页
 * Created by zy on 2018/4/23.
 */

public class HomePageActivtiy extends SupportActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        if (findFragment(MainPageFragment.class) == null) {
            loadRootFragment(R.id.fl_container, MainFragment.newInstance());  // 加载根Fragment
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (MediaPlayControl.getInstance().back_to_menu){
            MediaPlayControl.getInstance().back_to_menu = false;
            MediaPlayControl.getInstance().continuePlay();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (MediaPlayControl.getInstance().isPlaying()){
            MediaPlayControl.getInstance().back_to_menu = true;
            MediaPlayControl.getInstance().pause();
        }

    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        return new DefaultHorizontalAnimator();
    }

}
