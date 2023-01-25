package com.app.sample_login

import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.app.common.base.view.BaseVBFragment
import com.app.sample_login.databinding.FragmentBottomMenuBinding
import dagger.hilt.android.AndroidEntryPoint
import com.app.common.R as common

@AndroidEntryPoint
class BottomMenuFragment : BaseVBFragment<FragmentBottomMenuBinding>(
    R.layout.fragment_bottom_menu
) {

    private lateinit var navController : NavController

    override fun onResume() {
        requireActivity().window.navigationBarColor = ContextCompat.getColor(requireContext(), common.color.bottom_menu_background)
        super.onResume()
    }

    override fun onStop() {
        requireActivity().window.navigationBarColor = ContextCompat.getColor(requireContext(), common.color.background)
        super.onStop()
    }

    override fun setUpViews() {
        navController = Navigation.findNavController(requireActivity(),R.id.fragmentMenuView)
        binding.bottomNavigation.setupWithNavController(navController)
    }


    override fun showPageLoading() {
    }

    override fun showPageContent() {
    }

    override fun showPageError(errorMessage: String?) {
    }

    override fun showPageEmpty(message: String?) {
    }
}