package com.glwz.bookassociation.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.glwz.bookassociation.ui.utils.MediaPlayControl;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (MediaPlayControl.getInstance().back_to_menu) {
            MediaPlayControl.getInstance().back_to_menu = false;
            MediaPlayControl.getInstance().continuePlay();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (MediaPlayControl.getInstance().isPlaying()) {
            MediaPlayControl.getInstance().back_to_menu = true;
            MediaPlayControl.getInstance().pause();
        }

    }
}
