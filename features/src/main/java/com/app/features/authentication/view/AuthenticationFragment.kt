package com.app.features.authentication.view

import androidx.lifecycle.lifecycleScope
import com.app.common.base.view.BaseMVVMFragment
import com.app.common.navigation.Navigator
import com.app.common.navigation.Route
import com.app.common.utils.isValidEmail
import com.app.data.base.util.Constants
import com.app.data.base.util.ResultWrapper
import com.app.features.R
import com.app.common.R as common
import com.app.features.authentication.viewmodel.AuthenticationViewModel
import com.app.features.databinding.FragmentAuthenticationBinding
import com.app.features.partnerships.view.PartnershipsBottomSheet
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class AuthenticationFragment : BaseMVVMFragment<FragmentAuthenticationBinding, AuthenticationViewModel>(
    R.layout.fragment_authentication,
    AuthenticationViewModel::class.java){

    @Inject
    lateinit var navigator: Navigator

    override fun setUpViews() {

        binding.apply{

            authentication.setOnClickListener {
                hideKeyboard()
                when(true){
                    !email.text.toString().isValidEmail()->show(getString(common.string.false_email))
                    password.text.toString().isEmpty()->show(getString(common.string.enter_password))
                    else ->{
                        viewModel.authentication(
                            username = email.text.trim().toString(),
                            password = password.text.trim().toString()
                        )
                    }
                }
            }



            partnerships.setOnClickListener {
                PartnershipsBottomSheet{ isSuccessful ->

                    show("Something went wrong!" , true)
                }.showDialog(parentFragmentManager)
            }

        }

    }


    override fun observeData() {
        lifecycleScope.launchWhenStarted {
            viewModel.data.collectLatest {
                when (it) {
                    is ResultWrapper.Error -> showPageError(it.error.message)
                    is ResultWrapper.Loading -> showPageLoading()
                    is ResultWrapper.Success -> {
                        showPageContent()
                        navigateToMainMenu()
                    }
                    else -> showPageContent()
                }
            }
        }

    }

    private fun navigateToMainMenu() = navigator.navigate(Route.BottomMenuFragment)



    override fun showPageLoading() {
        binding.vaPage.displayedChild = Constants.LOADING_PAGE_1
    }

    override fun showPageContent() {
        binding.vaPage.displayedChild = Constants.CONTENT_PAGE_0
    }

    override fun showPageError(errorMessage: String?) {
        showPageContent()
        errorMessage?.let { show(it, false) }
    }

    override fun showPageEmpty(message: String?) {
    }
}