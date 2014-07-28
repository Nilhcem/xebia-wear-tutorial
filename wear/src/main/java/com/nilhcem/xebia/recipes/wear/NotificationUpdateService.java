package com.nilhcem.xebia.recipes.wear;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;
import com.nilhcem.xebia.recipes.R;
import com.nilhcem.xebia.recipes.common.BitmapUtils;
import com.nilhcem.xebia.recipes.common.Constants;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class NotificationUpdateService extends WearableListenerService {

    private static final String TAG = NotificationUpdateService.class.getSimpleName();
    private static final String RECEIVED_PHOTO_NAME = "background.png";

    private GoogleApiClient mGoogleApiClient;

    @Override
    public void onCreate() {
        super.onCreate();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        Log.d(TAG, "onDataChanged: " + dataEvents);

        // Loop through the events and send a message
        for (DataEvent dataEvent : dataEvents) {
            if (dataEvent.getType() == DataEvent.TYPE_CHANGED) {
                if (Constants.NOTIFICATION_RECIPE_PATH.equals(dataEvent.getDataItem().getUri().getPath())) {
                    DataMapItem dataMapItem = DataMapItem.fromDataItem(dataEvent.getDataItem());
                    DataMap dataMap = dataMapItem.getDataMap();
                    String title = dataMap.getString(Constants.NOTIFICATION_RECIPE_TITLE);
                    String ingredients = dataMap.getString(Constants.NOTIFICATION_RECIPE_INGREDIENTS);
                    ArrayList<String> steps = dataMap.getStringArrayList(Constants.NOTIFICATION_RECIPE_STEPS);
                    Bitmap photo = loadBitmapFromAsset(dataMap.getAsset(Constants.NOTIFICATION_RECIPE_PHOTO));
                    sendNotification(title, ingredients, steps, photo);
                }
            }
        }
    }

    private Bitmap loadBitmapFromAsset(Asset asset) {
        if (asset == null) {
            throw new IllegalArgumentException("Asset must be non-null");
        }
        ConnectionResult result = mGoogleApiClient.blockingConnect(30, TimeUnit.SECONDS);
        if (!result.isSuccess()) {
            return null;
        }

        // convert asset into a file descriptor and block until it's ready
        InputStream assetInputStream = Wearable.DataApi.getFdForAsset(mGoogleApiClient, asset).await().getInputStream();
        mGoogleApiClient.disconnect();

        // decode the stream into a bitmap if non null
        if (assetInputStream == null) {
            Log.w(TAG, "Requested an unknown Asset.");
            return null;
        }
        return BitmapFactory.decodeStream(assetInputStream);
    }

    private void sendNotification(String title, String ingredients, ArrayList<String> steps, Bitmap photo) {
        // this intent will open the activity when the user taps the "open" action on the notification
        Intent viewIntent = new Intent(this, MainActivity.class);
        viewIntent.putExtra(MainActivity.EXTRA_RECIPE_TITLE, title);
        viewIntent.putExtra(MainActivity.EXTRA_RECIPE_INGREDIENTS, ingredients);
        viewIntent.putExtra(MainActivity.EXTRA_RECIPE_STEPS, steps);
        viewIntent.putExtra(MainActivity.EXTRA_RECIPE_PHOTO, saveDrawableTemporary(photo));
        PendingIntent pendingViewIntent = PendingIntent.getActivity(this, 0, viewIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        saveDrawableTemporary(photo);

        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setLargeIcon(photo)
                .setContentText(title)
                .addAction(new Notification.Action(R.drawable.ic_fork, getString(R.string.get_started), pendingViewIntent))
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }

    private String saveDrawableTemporary(Bitmap photo) {
        String fileName = RECEIVED_PHOTO_NAME;
        FileOutputStream stream = null;
        try {
            stream = openFileOutput(fileName, MODE_PRIVATE);
            stream.write(BitmapUtils.toByteArray(photo));
        } catch (IOException e) {
            Log.e(TAG, "", e);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    Log.e(TAG, "", e);
                }
            }
        }
        return fileName;
    }
}
