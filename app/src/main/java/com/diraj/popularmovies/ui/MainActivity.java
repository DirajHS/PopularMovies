package com.diraj.popularmovies.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.diraj.popularmovies.AnimationHandling;
import com.diraj.popularmovies.R;

public class MainActivity extends AppCompatActivity {

    private boolean mTwoPane;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction().add(R.id.homeFragment, new HomeFragment()).commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AnimationHandling.animateScreen(this, AnimationHandling.ANIM_TYPE.CLOSE);
    }
}
