package com.app.features.floor_plan.view

import com.app.common.base.view.BaseMVVMFragment
import com.app.features.R
import com.app.features.databinding.FragmentFloorPlanBinding
import com.app.features.floor_plan.viewmodel.FloorPlanViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FloorPlanFragment : BaseMVVMFragment<FragmentFloorPlanBinding, FloorPlanViewModel>(
    R.layout.fragment_floor_plan,
    FloorPlanViewModel::class.java){


    override fun setUpViews() {

    }


    override fun observeData() {


    }


    override fun showPageLoading() {
        TODO("Not yet implemented")
    }

    override fun showPageContent() {
        TODO("Not yet implemented")
    }

    override fun showPageError(errorMessage: String?) {
        TODO("Not yet implemented")
    }

    override fun showPageEmpty(message: String?) {
        TODO("Not yet implemented")
    }
}