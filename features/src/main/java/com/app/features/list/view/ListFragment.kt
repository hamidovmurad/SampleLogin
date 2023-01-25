package com.app.features.list.view

import com.app.common.base.view.BaseMVVMFragment
import com.app.features.R
import com.app.features.databinding.FragmentListBinding
import com.app.features.list.viewmodel.ListViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ListFragment : BaseMVVMFragment<FragmentListBinding, ListViewModel>(
    R.layout.fragment_list,
    ListViewModel::class.java){


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