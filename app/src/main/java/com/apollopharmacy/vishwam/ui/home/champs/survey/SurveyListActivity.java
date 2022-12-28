package com.apollopharmacy.vishwam.ui.home.champs.survey;

import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.apollopharmacy.vishwam.R;
import com.apollopharmacy.vishwam.databinding.ActivitySurveyListBinding;
import com.apollopharmacy.vishwam.ui.home.champs.survey.adapter.SurveyListAdapter;
import com.apollopharmacy.vishwam.ui.home.champs.survey.model.SurveyDetails;

import java.util.ArrayList;
import java.util.List;

public class SurveyListActivity extends AppCompatActivity {
    private ActivitySurveyListBinding surveyListBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        surveyListBinding = DataBindingUtil.setContentView(this, R.layout.activity_survey_list);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        List<SurveyDetails> surveyDetailsList = new ArrayList<>();

        surveyDetailsList.add(new SurveyDetails(0, "16741", "Gandhi Nagar,Nellore", "Pending", "ramesh_p@apollopharmacy.org"));
        surveyDetailsList.add(new SurveyDetails(0, "16741", "Moti Nagar,Nellore", "Pending", "ramesh_p@apollopharmacy.org"));

        surveyDetailsList.add(new SurveyDetails(1, "16742", "Gandhi Nagar,Guntur", "Completed", "ramesh_p@apollopharmacy.org", "20 Dec,2022 - 10:45am"));
        surveyDetailsList.add(new SurveyDetails(1, "16742", "RamNagar,Guntur", "Completed", "ramesh_p@apollopharmacy.org", "19 Dec,2022 - 03:00am"));


        SurveyListAdapter adapter = new SurveyListAdapter(surveyDetailsList, getApplicationContext());
        surveyListBinding.surveyListRcv.setLayoutManager(new LinearLayoutManager(this));
        surveyListBinding.surveyListRcv.setAdapter(adapter);

    }
}