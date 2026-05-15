package com.example.thenancalculator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class InstructionSheet extends BottomSheetDialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_modal, container, false);
        
        view.findViewById(R.id.btn_close_modal).setOnClickListener(v -> {
            SplashActivity.playSfx(SplashActivity.clickSoundId);
            dismiss();
        });
        
        return view;
    }
}