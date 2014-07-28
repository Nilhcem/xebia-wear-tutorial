package com.nilhcem.xebia.recipes.wear;

import android.app.Activity;
import android.os.Bundle;

import com.nilhcem.xebia.recipes.R;

public class MainActivity extends Activity {

    public static final String EXTRA_RECIPE_TITLE = "mTitle";
    public static final String EXTRA_RECIPE_INGREDIENTS = "mIngredients";
    public static final String EXTRA_RECIPE_STEPS = "mSteps";
    public static final String EXTRA_RECIPE_PHOTO = "mPhoto";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
