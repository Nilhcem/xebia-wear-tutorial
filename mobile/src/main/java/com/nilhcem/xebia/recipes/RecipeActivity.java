package com.nilhcem.xebia.recipes;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.TextAppearanceSpan;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nilhcem.xebia.recipes.core.Recipe;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe);
        ButterKnife.inject(this);
        bindRecipeData();
        createNotification();
    }

    private void createNotification() {
        int notifId = 1;

        Intent intent = new Intent(this, RecipeActivity.class);
        intent.putExtra(EXTRA_RECIPE_NAME, mRecipe.name());

        // Gets a PendingIntent containing the entire back stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(RecipeActivity.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setLargeIcon(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), mRecipe.drawableRes), 280, 280, false))
                        .setContentTitle(getTitle())
                        .setContentText(getString(mRecipe.nameRes));

        // Affichage de la notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.cancelAll();
        notificationManager.notify(notifId, builder.build());
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
}
