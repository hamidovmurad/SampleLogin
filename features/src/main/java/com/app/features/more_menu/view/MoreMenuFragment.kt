package com.app.features.more_menu.view

import com.app.common.base.view.BaseMVVMFragment
import com.app.features.R
import com.app.features.databinding.FragmentMoreMenuBinding
import com.app.features.more_menu.viewmodel.MoreMenuViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MoreMenuFragment : BaseMVVMFragment<FragmentMoreMenuBinding, MoreMenuViewModel>(
    R.layout.fragment_more_menu,
    MoreMenuViewModel::class.java){


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