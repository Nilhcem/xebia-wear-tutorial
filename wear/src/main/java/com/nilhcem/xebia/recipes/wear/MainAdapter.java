package com.nilhcem.xebia.recipes.wear;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.wearable.view.CardFragment;
import android.support.wearable.view.FragmentGridPagerAdapter;
import android.support.wearable.view.ImageReference;

import com.nilhcem.xebia.recipes.R;

import java.io.File;
import java.util.Collections;
import java.util.List;

import static com.nilhcem.xebia.recipes.wear.MainActivity.EXTRA_RECIPE_INGREDIENTS;
import static com.nilhcem.xebia.recipes.wear.MainActivity.EXTRA_RECIPE_PHOTO;
import static com.nilhcem.xebia.recipes.wear.MainActivity.EXTRA_RECIPE_STEPS;
import static com.nilhcem.xebia.recipes.wear.MainActivity.EXTRA_RECIPE_TITLE;

public class MainAdapter extends FragmentGridPagerAdapter {

    private String mTitle;
    private String mIngredients;
    private String mPhotoPath;
    private List<String> mSteps = Collections.emptyList();
    private Context mContext;
    private Bitmap mBackground;

    public MainAdapter(FragmentManager fm, Context context, Intent intent) {
        super(fm);
        mContext = context;
        setData(intent);
    }

    @Override
    public Fragment getFragment(int row, int col) {
        if (col == 0) {
            return CardFragment.create(mTitle, mIngredients, R.drawable.ic_launcher);
        } else {
            return CardFragment.create(mContext.getString(R.string.step_title, col), mSteps.get(col - 1), 0);
        }
    }

    @Override
    public int getRowCount() {
        return 1;
    }

    @Override
    public int getColumnCount(int row) {
        return mSteps.size() + 1;
    }

    @Override
    public ImageReference getBackground(int row, int column) {
        if (mBackground != null) {
            return ImageReference.forBitmap(mBackground);
        }
        return super.getBackground(row, column);
    }

    private void setData(Intent intent) {
        if (intent == null) {
            return;
        }

        if (intent.hasExtra(EXTRA_RECIPE_TITLE)) {
            mTitle = intent.getStringExtra(EXTRA_RECIPE_TITLE);
        }
        if (intent.hasExtra(EXTRA_RECIPE_INGREDIENTS)) {
            mIngredients = intent.getStringExtra(EXTRA_RECIPE_INGREDIENTS);
        }
        if (intent.hasExtra(EXTRA_RECIPE_STEPS)) {
            mSteps = intent.getStringArrayListExtra(EXTRA_RECIPE_STEPS);
        }
        if (intent.hasExtra(EXTRA_RECIPE_PHOTO)) {
            mPhotoPath = intent.getStringExtra(EXTRA_RECIPE_PHOTO);
            File filePath = mContext.getFileStreamPath(mPhotoPath);
            mBackground = BitmapFactory.decodeFile(filePath.toString());
        }
    }
}
