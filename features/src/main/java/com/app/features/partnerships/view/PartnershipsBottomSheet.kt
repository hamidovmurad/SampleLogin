package com.app.features.partnerships.view

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.app.common.base.view.BaseMVVMBottomSheetDialogFragment
import com.app.data.base.util.Constants
import com.app.data.base.util.ResultWrapper
import com.app.features.R
import com.app.features.databinding.BottomsheetPartnershipsBinding
import com.app.features.partnerships.viewmodel.PartnershipsViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PartnershipsBottomSheet(
    private val applyClickListener: (Boolean) -> Unit,
)  : BaseMVVMBottomSheetDialogFragment<BottomsheetPartnershipsBinding, PartnershipsViewModel>(
    R.layout.bottomsheet_partnerships,
    PartnershipsViewModel::class.java) {



    fun showDialog(fm : FragmentManager){
        if(fm.findFragmentByTag(PartnershipsBottomSheet::class.java.canonicalName) == null)
            show(fm, PartnershipsBottomSheet::class.java.canonicalName)
    }



    override fun setUpViews() {

        binding.apply {


        }
    }


    override fun observeData() {

    }



    fun showPageLoading() {
    }

    fun showPageContent() {
    }

}