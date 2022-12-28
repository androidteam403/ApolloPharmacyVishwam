package com.apollopharmacy.vishwam.ui.home.champs.survey;

import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.apollopharmacy.vishwam.R;
import com.apollopharmacy.vishwam.base.BaseFragment;
import com.apollopharmacy.vishwam.databinding.FragmentChampsSurveyBinding;
import com.apollopharmacy.vishwam.dialog.ChampsSurveyDialog;


public class ChampsSurveyFragment extends BaseFragment<ChampsSurveyViewModel, FragmentChampsSurveyBinding> {
    @Override
    public int getLayoutRes() {
        return R.layout.fragment_champs_survey;
    }

    @NonNull
    @Override
    public ChampsSurveyViewModel retrieveViewModel() {
        return new ChampsSurveyViewModel();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void setup() {
        viewBinding.seekbar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Toast.makeText(getContext(), "Value: " + getConvertedValue(progress), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        viewBinding.seekbar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Toast.makeText(getContext(), "Value: " + getConvertedValue(progress), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        viewBinding.seekbar3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Toast.makeText(getContext(), "Value: " + getConvertedValue(progress), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        viewBinding.seekbar4.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Toast.makeText(getContext(), "Value: " + getConvertedValue(progress), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        viewBinding.saveDraft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ChampsSurveyDialog().show(getChildFragmentManager(), "");
            }
        });

        viewBinding.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SurveyListActivity.class));
            }
        });
    }

    private float getConvertedValue(int intVal) {
        float floatVal = 0.0f;
        floatVal = .5f * intVal;
        return floatVal;
    }
}