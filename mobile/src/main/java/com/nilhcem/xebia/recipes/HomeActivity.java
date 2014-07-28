package com.nilhcem.xebia.recipes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.nilhcem.xebia.recipes.RecipeActivity.EXTRA_RECIPE_NAME;

public class HomeActivity extends Activity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(new CalligraphyContextWrapper(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
    }

    public void onRecipeClicked(View view) {
        Intent intent = new Intent(this, RecipeActivity.class);
        intent.putExtra(EXTRA_RECIPE_NAME, (String) view.getTag());
        startActivity(intent);
    }
}
