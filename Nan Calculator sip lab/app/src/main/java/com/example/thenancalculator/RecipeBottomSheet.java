package com.example.thenancalculator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class RecipeBottomSheet extends BottomSheetDialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_recipe_manual, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View title = view.findViewById(R.id.manual_title);
        View step1 = view.findViewById(R.id.step_1);
        View step2 = view.findViewById(R.id.step_2);
        View step3 = view.findViewById(R.id.step_3);
        View button = view.findViewById(R.id.btn_close_manual);

        // Entrance Sequence Animations
        title.animate().alpha(1f).translationY(0)
                .setDuration(600)
                .setInterpolator(new OvershootInterpolator())
                .start();

        step1.animate().alpha(1f).translationX(0)
                .setDuration(500)
                .setStartDelay(300)
                .setInterpolator(new OvershootInterpolator())
                .start();

        step2.animate().alpha(1f).translationX(0)
                .setDuration(500)
                .setStartDelay(500)
                .setInterpolator(new OvershootInterpolator())
                .start();

        step3.animate().alpha(1f).translationX(0)
                .setDuration(500)
                .setStartDelay(700)
                .setInterpolator(new OvershootInterpolator())
                .start();

        button.animate().alpha(1f).scaleX(1f).scaleY(1f)
                .setDuration(600)
                .setStartDelay(1000)
                .setInterpolator(new AnticipateOvershootInterpolator())
                .start();

        view.findViewById(R.id.btn_close_manual).setOnClickListener(v -> {
            SplashActivity.playSfx(SplashActivity.clickSoundId);
            dismiss();
        });
    }
}