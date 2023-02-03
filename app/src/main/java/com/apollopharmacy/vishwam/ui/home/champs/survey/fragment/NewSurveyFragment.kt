package com.apollopharmacy.vishwam.ui.home.champs.survey.fragment

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.databinding.FragmentChampsSurveyBinding
import com.apollopharmacy.vishwam.ui.home.champs.survey.activity.surveydetails.SurveyDetailsActivity

class NewSurveyFragment : BaseFragment<NewSurveyViewModel, FragmentChampsSurveyBinding>(),
    NewSurveyCallback {


    override val layoutRes: Int
        get() = R.layout.fragment_champs_survey

    override fun retrieveViewModel(): NewSurveyViewModel {
        return ViewModelProvider(this).get(NewSurveyViewModel::class.java)
    }

    override fun setup() {
        viewBinding.callback = this
        viewBinding.enterStoreEdittext.addTextChangedListener(object :
            TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                if(editable.length>0){
                    viewBinding.storeIdEdittextLayout.elevation=0.0f
                    viewBinding.enterStoreEdittext.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f);
                    viewBinding.enterStoreEdittext.background=resources.getDrawable(R.drawable.backgrounf_for_blue_edittext)
                    viewBinding.storeIdEdittextLayout.background=resources.getDrawable(R.drawable.backgrounf_for_blue_edittext)
                    viewBinding.closeIcon.visibility= View.VISIBLE
                }else{
                    viewBinding.enterStoreEdittext.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f);
                    viewBinding.enterStoreEdittext.background=resources.getDrawable(R.drawable.edit_comment_bg)
                    viewBinding.storeIdEdittextLayout.background=resources.getDrawable(R.drawable.edit_comment_bg)
                    viewBinding.closeIcon.visibility= View.GONE
                }


            }
        })
    }


    override fun onClickSearch() {
        viewBinding.searchButton.visibility=View.GONE
        viewBinding.cardViewStore.visibility=View.VISIBLE
    }

    override fun onClickCardView() {
        val intent = Intent(context, SurveyDetailsActivity::class.java)
        startActivity(intent)
        activity?.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }

    override fun onClickCloseIcon() {
       viewBinding.enterStoreEdittext.setText("")
    }
}