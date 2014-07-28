package com.nilhcem.xebia.recipes;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.Wearable;
import com.nilhcem.xebia.recipes.common.BitmapUtils;
import com.nilhcem.xebia.recipes.common.Constants;
import com.nilhcem.xebia.recipes.core.Recipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class RecipeActivity extends Activity {

    public static final String EXTRA_RECIPE_NAME = "mRecipe";

    private static final String TAG = RecipeActivity.class.getSimpleName();

    @InjectView(R.id.recipe_scroll) ScrollView mScrollView;
    @InjectView(R.id.recipe_picture) ImageView mPicture;
    @InjectView(R.id.recipe_ingredients) TextView mIngredients;
    @InjectView(R.id.recipe_steps) ViewGroup mSteps;
    @InjectView(R.id.recipe_credits) TextView mCredits;

    private Recipe mRecipe;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe);
        ButterKnife.inject(this);
        bindRecipeData();
        initGoogleApiClient();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    /**
     * Ignore this method for the tutorial.
     * It simply binds recipe data to layout views.
     */
    private void bindRecipeData() {
        // Get recipe from intent data
        mRecipe = Recipe.valueOf(getIntent().getStringExtra(EXTRA_RECIPE_NAME));

        // Bind data
        getActionBar().setTitle(mRecipe.nameRes);
        mPicture.setImageResource(mRecipe.drawableRes);
        mIngredients.setText(mRecipe.ingredientsRes);
        mCredits.setText(mRecipe.creditsRes);
        mCredits.setMovementMethod(LinkMovementMethod.getInstance());

        // Bind steps data
        mSteps.removeAllViews();
        int padding = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
        String[] steps = getResources().getStringArray(R.array.recipe_steps);
        for (int i = 0; i < steps.length; i++) {
            String stepNb = String.format(Locale.US, "%d. ", i + 1);
            SpannableString spannable = new SpannableString(stepNb + steps[i]);
            spannable.setSpan(new TextAppearanceSpan(this, R.style.RecipeStepNb), 0, stepNb.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            TextView tv = new TextView(this);
            tv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f);
            tv.setText(spannable);
            tv.setPadding(padding, padding, padding, padding);
            mSteps.addView(tv);
        }
    }

    private void initGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle connectionHint) {
                        Log.d(TAG, "onConnected: " + connectionHint);
                        sendNotificationToWearable();
                    }

                    @Override
                    public void onConnectionSuspended(int cause) {
                        Log.d(TAG, "onConnectionSuspended: " + cause);
                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult result) {
                        Log.d(TAG, "onConnectionFailed: " + result);
                    }
                })
                .addApi(Wearable.API)
                .build();
    }

    void sendNotificationToWearable() {
        PutDataMapRequest dataMapRequest = PutDataMapRequest.create(Constants.NOTIFICATION_RECIPE_PATH);
        DataMap dataMap = dataMapRequest.getDataMap();
        dataMap.putString(Constants.NOTIFICATION_RECIPE_TITLE, getString(mRecipe.nameRes));
        dataMap.putString(Constants.NOTIFICATION_RECIPE_INGREDIENTS, getString(mRecipe.ingredientsRes));
        dataMap.putStringArrayList(Constants.NOTIFICATION_RECIPE_STEPS,
                new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.recipe_steps))));
        dataMap.putAsset(Constants.NOTIFICATION_RECIPE_PHOTO, createAssetFromDrawableRes(mRecipe.drawableRes));
        Wearable.DataApi.putDataItem(mGoogleApiClient, dataMapRequest.asPutDataRequest());
    }

    private Asset createAssetFromDrawableRes(int drawableRes) {
        Bitmap origBitmap = BitmapFactory.decodeResource(getResources(), drawableRes);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(origBitmap, 280, 280, false);
        return Asset.createFromBytes(BitmapUtils.toByteArray(scaledBitmap));
    }
}
