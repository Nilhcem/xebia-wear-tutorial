package com.nilhcem.xebia.recipes.core;

import com.nilhcem.xebia.recipes.R;

public enum Recipe {

    MAKI(R.string.maki_name, R.drawable.food_maki, R.string.maki_ingredients, R.string.maki_credits),
    POTATOES(R.string.potatoes_name, R.drawable.food_potatoes, R.string.potatoes_ingredients, R.string.potatoes_credits),
    SHRIMP(R.string.shrimp_name, R.drawable.food_shrimp, R.string.shrimp_ingredients, R.string.shrimp_credits),
    PIE(R.string.pie_name, R.drawable.food_pie, R.string.pie_ingredients, R.string.pie_credits);

    public final int nameRes;
    public final int drawableRes;
    public final int ingredientsRes;
    public final int creditsRes;

    private Recipe(int nameResId, int drawableResId, int ingredientsResId, int creditsResId) {
        nameRes = nameResId;
        drawableRes = drawableResId;
        ingredientsRes = ingredientsResId;
        creditsRes = creditsResId;
    }
}
