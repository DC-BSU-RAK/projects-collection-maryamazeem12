package com.example.thenancalculator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.switchmaterial.SwitchMaterial;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ImageView ivMixMachine, ivRefillGlass, ivResultDrink, ivInstructions, ivSettings, ivAppLogo;
    private ImageView boardItem1, boardItem2, boardItem3;
    private TextView plus1, plus2, tvResultName;
    private ConstraintLayout resultOverlay, instructionsOverlay, settingsOverlay;
    private GridLayout ingredientsGrid;
    
    private final List<Ingredient> selectedIngredients = new ArrayList<>();
    private final Map<String, DrinkInfo> drinkRecipes = new HashMap<>();
    private final Map<String, Integer> ingredientIcons = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        ivMixMachine = findViewById(R.id.mix_machine);
        ivRefillGlass = findViewById(R.id.refill_glass);
        ivResultDrink = findViewById(R.id.iv_result_drink);
        ivInstructions = findViewById(R.id.btn_instructions);
        ivSettings = findViewById(R.id.btn_settings);
        ivAppLogo = findViewById(R.id.app_logo_small);
        tvResultName = findViewById(R.id.tv_result_name);
        
        resultOverlay = findViewById(R.id.result_overlay);
        instructionsOverlay = findViewById(R.id.instructions_overlay);
        settingsOverlay = findViewById(R.id.settings_overlay);
        
        ingredientsGrid = findViewById(R.id.ingredients_grid);

        boardItem1 = findViewById(R.id.board_item_1);
        boardItem2 = findViewById(R.id.board_item_2);
        boardItem3 = findViewById(R.id.board_item_3);
        plus1 = findViewById(R.id.plus_1);
        plus2 = findViewById(R.id.plus_2);

        initRecipes();
        initIngredientIcons();
        setupIngredients();
        
        // Ensure BG Music volume is correct and playing
        if (SplashActivity.globalBgPlayer != null) {
            SplashActivity.globalBgPlayer.setVolume(0.6f, 0.6f);
            if (!SplashActivity.globalBgPlayer.isPlaying() && SplashActivity.isSoundEnabled) {
                SplashActivity.globalBgPlayer.start();
            }
        }

        // click sound helper
        View.OnClickListener clickSfx = v -> SplashActivity.playSfx(SplashActivity.clickSoundId);

        // Audio Switch Logic
        SwitchMaterial volumeSwitch = findViewById(R.id.switch_volume);
        if (volumeSwitch != null) {
            volumeSwitch.setChecked(SplashActivity.isSoundEnabled);
            volumeSwitch.setOnCheckedChangeListener((btn, isChecked) -> {
                // Ensure we hear the click when toggling
                boolean oldEnabled = SplashActivity.isSoundEnabled;
                SplashActivity.isSoundEnabled = true;
                SplashActivity.playSfx(SplashActivity.clickSoundId);
                SplashActivity.isSoundEnabled = isChecked;

                if (isChecked) {
                    if (SplashActivity.globalBgPlayer != null && !SplashActivity.globalBgPlayer.isPlaying()) {
                        SplashActivity.globalBgPlayer.start();
                    }
                } else {
                    if (SplashActivity.globalBgPlayer != null && SplashActivity.globalBgPlayer.isPlaying()) {
                        SplashActivity.globalBgPlayer.pause();
                    }
                }
            });
        }

        // Control listeners
        ivMixMachine.setOnClickListener(v -> {
            clickSfx.onClick(v);
            startMixing();
        });
        
        ivRefillGlass.setOnClickListener(v -> {
            clickSfx.onClick(v);
            SplashActivity.playSfx(SplashActivity.refillSoundId);
            resetSelection();
        });
        
        findViewById(R.id.btn_close_result).setOnClickListener(v -> {
            clickSfx.onClick(v);
            resultOverlay.setVisibility(View.GONE);
            resetSelection();
        });
        
        ivInstructions.setOnClickListener(v -> {
            clickSfx.onClick(v);
            instructionsOverlay.setVisibility(View.VISIBLE);
            instructionsOverlay.setAlpha(0f);
            instructionsOverlay.animate().alpha(1f).setDuration(400).start();
        });
        findViewById(R.id.btn_close_instructions).setOnClickListener(v -> {
            clickSfx.onClick(v);
            instructionsOverlay.setVisibility(View.GONE);
        });
        findViewById(R.id.btn_got_it).setOnClickListener(v -> {
            clickSfx.onClick(v);
            instructionsOverlay.setVisibility(View.GONE);
        });
        
        // click sound for instruction page elements
        View instHeading = findViewById(R.id.tv_instructions_heading);
        if (instHeading != null) instHeading.setOnClickListener(clickSfx);
        
        View instLogo = findViewById(R.id.iv_instructions_logo);
        if (instLogo != null) instLogo.setOnClickListener(clickSfx);
        
        int[] stepIds = {R.id.instruction_step_1, R.id.instruction_step_2, R.id.instruction_step_3, R.id.instruction_step_4};
        for (int id : stepIds) {
            View sv = findViewById(id);
            if (sv != null) sv.setOnClickListener(clickSfx);
        }

        ivSettings.setOnClickListener(v -> {
            clickSfx.onClick(v);
            settingsOverlay.setVisibility(View.VISIBLE);
            settingsOverlay.setAlpha(0f);
            settingsOverlay.animate().alpha(1f).setDuration(300).start();
        });
        findViewById(R.id.btn_close_settings).setOnClickListener(v -> {
            clickSfx.onClick(v);
            settingsOverlay.setVisibility(View.GONE);
        });
        
        // Universal click sound for all visual components
        findViewById(R.id.main_layout).setOnClickListener(clickSfx);
        ivAppLogo.setOnClickListener(clickSfx);
        findViewById(R.id.board).setOnClickListener(clickSfx);
        plus1.setOnClickListener(clickSfx);
        plus2.setOnClickListener(clickSfx);
        boardItem1.setOnClickListener(clickSfx);
        boardItem2.setOnClickListener(clickSfx);
        boardItem3.setOnClickListener(clickSfx);
        ivResultDrink.setOnClickListener(clickSfx);
        tvResultName.setOnClickListener(clickSfx);
        
        instructionsOverlay.setOnClickListener(clickSfx);
        settingsOverlay.setOnClickListener(clickSfx);
        resultOverlay.setOnClickListener(clickSfx);

        animateEntrance();
        startFloatingAnimations();
    }

    private void initRecipes() {
        addRecipe("Taro", "Strawberry", "Pearls", "Taro-Strawberry Fusion", R.drawable.tarostrawberrywithtapiocapearls);
        addRecipe("Taro", "Matcha", "Pearls", "Verant Amethyst", R.drawable.taromatchawithtapiocapearls);
        addRecipe("Taro", "Oreo", "Pearls", "Taro Oreo Cloud", R.drawable.tarooreowithtapiocapearls);
        addRecipe("Taro", "Mango", "Pearls", "Taro Mango Harmony", R.drawable.taromangowithtapiocapearls);
        addRecipe("Taro", "Coffee", "Pearls", "Taro Espresso Cloud", R.drawable.tarocoffeebeanswithtapiocapearls);
        addRecipe("Taro", "Strawberry", "Foam", "Taro Strawberry Silk", R.drawable.tarostrawberrywithvanillafoam);
        addRecipe("Taro", "Matcha", "Foam", "Taro Matcha Silk", R.drawable.taromatchawithvanillafoam);
        addRecipe("Taro", "Oreo", "Foam", "Taro Oreo Silk", R.drawable.tarooreowithvanillafoam);
        addRecipe("Taro", "Mango", "Foam", "Taro Mango Silk", R.drawable.taromangowithvanillafoam);
        addRecipe("Taro", "Coffee", "Foam", "Taro Coffee Silk", R.drawable.tarocoffeebeanswithvanillafoam);
        addRecipe("Matcha", "Oreo", "Pearls", "Matcha Oreo Crunch", R.drawable.matchaoreowithtapiocapearls);
        addRecipe("Strawberry", "Matcha", "Pearls", "Berry Matcha Cloud", R.drawable.strawberrymatchawithtapiocapearls);
        addRecipe("Matcha", "Mango", "Pearls", "Matcha Mango Bliss", R.drawable.matchamangowithtapiocapearls);
        addRecipe("Matcha", "Coffee", "Pearls", "Matcha Espresso Cloud", R.drawable.matchacoffeebeanswithtapiocapearls);
        addRecipe("Matcha", "Oreo", "Foam", "Matcha Oreo Silk", R.drawable.matchaoreowithvanillafoam);
        addRecipe("Strawberry", "Matcha", "Foam", "Berry Matcha Silk", R.drawable.strawberrymatchawithvanillafoam);
        addRecipe("Matcha", "Mango", "Foam", "Matcha Mango Silk", R.drawable.matchamangowithvanillafoam);
        addRecipe("Matcha", "Coffee", "Foam", "Matcha Coffee Silk", R.drawable.matchacoffeebeanswithvanillafoam);
        addRecipe("Strawberry", "Oreo", "Pearls", "Strawberry Oreo Bomb", R.drawable.strawberryoreowithtapiocapearls);
        addRecipe("Strawberry", "Mango", "Pearls", "Tropic Twist", R.drawable.strawberrymangowithtapiocapearls);
        addRecipe("Strawberry", "Coffee", "Pearls", "Strawberry Coffee Cloud", R.drawable.strawberrycoffeebeanswithtapiocapearls);
        addRecipe("Strawberry", "Oreo", "Foam", "Berry Oreo Silk", R.drawable.strawberryoreowithvanillafoam);
        addRecipe("Strawberry", "Mango", "Foam", "Berry Mango Silk", R.drawable.strawberrymangowithvanillafoam);
        addRecipe("Strawberry", "Coffee", "Foam", "Berry Coffee Silk", R.drawable.strawberrycoffeebeanswithvanillafoam);
        addRecipe("Mango", "Oreo", "Pearls", "Golden Oreo Fusion", R.drawable.mangooreowithtapiocapearls);
        addRecipe("Coffee", "Mango", "Pearls", "Coffee Mango Bliss", R.drawable.coffeebeansmangowithtapiocapearls);
        addRecipe("Mango", "Oreo", "Foam", "Mango Oreo Silk", R.drawable.mangooreowithvanillafoam);
        addRecipe("Coffee", "Mango", "Foam", "Coffee Mango Silk", R.drawable.coffeebeansmangowithvanillafoam);
        addRecipe("Coffee", "Oreo", "Pearls", "Espresso Oreo Cloud", R.drawable.coffeebeansoreowithtapiocapearls);
    }

    private void addRecipe(String i1, String i2, String i3, String name, int imageRes) {
        List<String> keyList = new ArrayList<>();
        keyList.add(i1);
        keyList.add(i2);
        keyList.add(i3);
        Collections.sort(keyList);
        String key = keyList.get(0) + "+" + keyList.get(1) + "+" + keyList.get(2);
        drinkRecipes.put(key, new DrinkInfo(name, imageRes));
    }

    private void initIngredientIcons() {
        ingredientIcons.put("Taro", R.drawable.taro);
        ingredientIcons.put("Matcha", R.drawable.matcha);
        ingredientIcons.put("Oreo", R.drawable.oreo);
        ingredientIcons.put("Strawberry", R.drawable.strawberry);
        ingredientIcons.put("Mango", R.drawable.mango);
        ingredientIcons.put("Coffee", R.drawable.coffeebeans);
        ingredientIcons.put("Pearls", R.drawable.tapiocapearls);
        ingredientIcons.put("Foam", R.drawable.vanillafoam);
    }

    private void setupIngredients() {
        for (int i = 0; i < ingredientsGrid.getChildCount(); i++) {
            ViewGroup container = (ViewGroup) ingredientsGrid.getChildAt(i);
            View iconView = container.getChildAt(0); 

            container.setOnClickListener(v -> {
                SplashActivity.playSfx(SplashActivity.clickSoundId);
                if (selectedIngredients.size() < 3) {
                    String tag = (String) v.getTag();
                    Integer iconRes = ingredientIcons.get(tag);
                    if (iconRes != null) {
                        selectedIngredients.add(new Ingredient(tag, iconRes));
                        updateBoard();
                        v.animate().scaleX(1.2f).scaleY(0.8f).setDuration(100).withEndAction(() -> 
                            v.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).start()
                        ).start();
                    }
                } else {
                    Toast.makeText(this, "Board is full!", Toast.LENGTH_SHORT).show();
                }
            });

            ObjectAnimator floatAnim = ObjectAnimator.ofFloat(iconView, "translationY", 0, -25);
            floatAnim.setDuration(1200 + (i * 150L));
            floatAnim.setRepeatCount(ValueAnimator.INFINITE);
            floatAnim.setRepeatMode(ValueAnimator.REVERSE);
            floatAnim.setInterpolator(new AccelerateDecelerateInterpolator());
            floatAnim.start();

            ObjectAnimator shinyAnim = ObjectAnimator.ofPropertyValuesHolder(iconView,
                    PropertyValuesHolder.ofFloat("scaleX", 1.0f, 1.12f),
                    PropertyValuesHolder.ofFloat("scaleY", 1.0f, 1.12f),
                    PropertyValuesHolder.ofFloat("alpha", 0.7f, 1.0f));
            shinyAnim.setDuration(1500 + (i * 200L));
            shinyAnim.setRepeatCount(ValueAnimator.INFINITE);
            shinyAnim.setRepeatMode(ValueAnimator.REVERSE);
            shinyAnim.setInterpolator(new AccelerateDecelerateInterpolator());
            shinyAnim.start();
        }
    }

    private void startFloatingAnimations() {
        ObjectAnimator logoHover = ObjectAnimator.ofPropertyValuesHolder(ivAppLogo,
                PropertyValuesHolder.ofFloat("translationY", 0, -15),
                PropertyValuesHolder.ofFloat("scaleX", 1.0f, 1.08f),
                PropertyValuesHolder.ofFloat("scaleY", 1.0f, 1.08f));
        logoHover.setDuration(1800);
        logoHover.setRepeatCount(ValueAnimator.INFINITE);
        logoHover.setRepeatMode(ValueAnimator.REVERSE);
        logoHover.setInterpolator(new AccelerateDecelerateInterpolator());
        logoHover.start();

        ObjectAnimator glassFloat = ObjectAnimator.ofFloat(ivRefillGlass, "translationY", 0, -12);
        glassFloat.setDuration(2000);
        glassFloat.setRepeatCount(ValueAnimator.INFINITE);
        glassFloat.setRepeatMode(ValueAnimator.REVERSE);
        glassFloat.setInterpolator(new AccelerateDecelerateInterpolator());
        glassFloat.start();
    }

    private void updateBoard() {
        boardItem1.setVisibility(View.GONE);
        boardItem2.setVisibility(View.GONE);
        boardItem3.setVisibility(View.GONE);
        plus1.setVisibility(View.GONE);
        plus2.setVisibility(View.GONE);

        if (!selectedIngredients.isEmpty()) {
            boardItem1.setImageResource(selectedIngredients.get(0).iconRes);
            boardItem1.setVisibility(View.VISIBLE);
        }
        if (selectedIngredients.size() >= 2) {
            boardItem2.setImageResource(selectedIngredients.get(1).iconRes);
            boardItem2.setVisibility(View.VISIBLE);
            plus1.setVisibility(View.VISIBLE);
        }
        if (selectedIngredients.size() >= 3) {
            boardItem3.setImageResource(selectedIngredients.get(2).iconRes);
            boardItem3.setVisibility(View.VISIBLE);
            plus2.setVisibility(View.VISIBLE);
        }
    }

    private void resetSelection() {
        selectedIngredients.clear();
        updateBoard();
    }

    private void startMixing() {
        if (selectedIngredients.size() < 3) {
            Toast.makeText(this, "Select 3 ingredients first!", Toast.LENGTH_SHORT).show();
            return;
        }

        SplashActivity.playSfx(SplashActivity.mixSoundId);

        // Shaking animation (3 seconds as requested)
        ObjectAnimator shake = ObjectAnimator.ofFloat(ivMixMachine, "translationX", 0, 25, -25, 25, -25, 25, -25, 15, -15, 6, -6, 0);
        shake.setDuration(3000);
        shake.setInterpolator(new AccelerateDecelerateInterpolator());
        shake.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // Show result after 3 seconds of mixing
                showResult();
            }
        });
        shake.start();
    }

    private void showResult() {
        List<String> names = new ArrayList<>();
        for (Ingredient i : selectedIngredients) {
            names.add(i.name);
        }
        Collections.sort(names);
        String key = names.get(0) + "+" + names.get(1) + "+" + names.get(2);

        DrinkInfo result = drinkRecipes.get(key);
        if (result != null) {
            ivResultDrink.setImageResource(result.imageRes);
            tvResultName.setText(result.name);
        } else {
            ivResultDrink.setImageResource(R.drawable.glass);
            tvResultName.setText(R.string.mystery_mix);
        }

        // Play ready sound once when splash appears
        SplashActivity.playSfx(SplashActivity.readySoundId);

        resultOverlay.setVisibility(View.VISIBLE);
        resultOverlay.setAlpha(0f);
        resultOverlay.setScaleX(0.5f);
        resultOverlay.setScaleY(0.5f);
        
        resultOverlay.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(500)
                .setInterpolator(new OvershootInterpolator())
                .start();
    }

    private void animateEntrance() {
        View mainContent = findViewById(R.id.main_layout);
        mainContent.setAlpha(0f);
        mainContent.animate().alpha(1f).setDuration(1000).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SplashActivity.globalBgPlayer != null && SplashActivity.isSoundEnabled && !SplashActivity.globalBgPlayer.isPlaying()) {
            SplashActivity.globalBgPlayer.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
