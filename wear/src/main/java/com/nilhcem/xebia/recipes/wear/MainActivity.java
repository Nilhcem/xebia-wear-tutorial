package com.nilhcem.xebia.recipes.wear;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.GridViewPager;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;
import com.nilhcem.xebia.recipes.R;
import com.nilhcem.xebia.recipes.common.Constants;

public class MainActivity extends Activity implements GridViewPager.OnPageChangeListener {

    public static final String EXTRA_RECIPE_TITLE = "mTitle";
    public static final String EXTRA_RECIPE_INGREDIENTS = "mIngredients";
    public static final String EXTRA_RECIPE_STEPS = "mSteps";
    public static final String EXTRA_RECIPE_PHOTO = "mPhoto";

    private MainAdapter mAdapter;
    private GridViewPager mPager;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() == null || !getIntent().hasExtra(EXTRA_RECIPE_TITLE)) {
            finish();
            return;
        }
        initGoogleApiClient();

        setContentView(R.layout.activity_main);
        mPager = (GridViewPager) findViewById(R.id.fragment_container);
        mAdapter = new MainAdapter(getFragmentManager(), this, getIntent());
        mPager.setAdapter(mAdapter);
        mPager.setOnPageChangeListener(this);
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

    private void initGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();
    }

    @Override
    public void onPageScrolled(int ro, int i2, float v, float v2, int i3, int i4) {
    }

    @Override
    public void onPageSelected(int row, final int col) {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {
                @Override
                public void onResult(NodeApi.GetConnectedNodesResult getConnectedNodesResult) {
                    for (Node node : getConnectedNodesResult.getNodes()) {
                        Wearable.MessageApi.sendMessage(mGoogleApiClient, node.getId(), Constants.NOTIFICATION_STEP_PATH, Integer.valueOf(col).toString().getBytes());
                    }
                }
            });
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {
    }
}
