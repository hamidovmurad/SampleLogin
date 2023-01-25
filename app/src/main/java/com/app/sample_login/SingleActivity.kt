package com.app.sample_login

import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.app.common.base.view.BaseActivity
import com.app.data.base.util.Constants
import com.app.data.features.response.repository.UserDataRepository
import com.app.sample_login.navigation.RetainedNavigator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class SingleActivity : BaseActivity()  {


    @Inject
    lateinit var retainedNavigator: RetainedNavigator

    @Inject
    lateinit var userDataRepository: UserDataRepository

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        when (key) {
            Constants.CLEARED_KEY -> {
                CoroutineScope(Dispatchers.Main).launch { retainedNavigator.logout() }
            }
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLocale()
        installSplashScreen()
        setContentView(R.layout.activity_single)

        setStartDestination()
    }



    private fun setStartDestination() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val inflater = navHostFragment.navController.navInflater
        val graph = inflater.inflate(R.navigation.main_nav_graph)

        val isLogged = userDataRepository.getToken().isNullOrEmpty().not()

        if (isLogged) {
            graph.setStartDestination(R.id.BottomMenuFragment)
        } else {
            graph.setStartDestination(R.id.AuthenticationFragment)
        }

        val navController = navHostFragment.navController
        navController.setGraph(graph, intent.extras)
    }

    override fun onResume() {
        retainedNavigator.activity = this
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
        super.onResume()
    }

    override fun onPause() {
        retainedNavigator.activity = null
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
        super.onPause()
    }


    override fun rootController(): NavController? =
        Navigation.findNavController(this, R.id.fragmentContainerView)

    override fun menuController(): NavController? =
        Navigation.findNavController(this, R.id.fragmentMenuView)

    private fun setLocale() {
        try {
            val locale = Locale(userDataRepository.getLanguageLocale())
            Locale.setDefault(locale)
            val config =
                Configuration()
            config.locale = locale
            resources
                .updateConfiguration(config, resources.displayMetrics)
        } catch (e: Exception) {
            Log.e("LanguageHelper", "[changeLanguage]" + e.message)
        }
    }

}