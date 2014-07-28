package com.nilhcem.xebia.recipes.wear;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.GridViewPager;

import com.nilhcem.xebia.recipes.R;

public class MainActivity extends Activity {

    public static final String EXTRA_RECIPE_TITLE = "mTitle";
    public static final String EXTRA_RECIPE_INGREDIENTS = "mIngredients";
    public static final String EXTRA_RECIPE_STEPS = "mSteps";
    public static final String EXTRA_RECIPE_PHOTO = "mPhoto";

    private MainAdapter mAdapter;
    private GridViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() == null || !getIntent().hasExtra(EXTRA_RECIPE_TITLE)) {
            finish();
            return;
        }

        setContentView(R.layout.activity_main);
        mPager = (GridViewPager) findViewById(R.id.fragment_container);
        mAdapter = new MainAdapter(getFragmentManager(), this, getIntent());
        mPager.setAdapter(mAdapter);
    }
}
