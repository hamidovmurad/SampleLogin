package com.app.sample_login.navigation

import android.app.Activity
import com.app.common.navigation.Navigator
import com.app.common.navigation.Route
import com.app.data.coroutines.DefaultDispatcherProvider
import com.app.sample_login.R
import com.app.sample_login.SingleActivity
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


@ActivityRetainedScoped
class RetainedNavigator @Inject constructor() : Navigator,
    CoroutineScope {

    companion object {
        const val NAVIGATION_TIMEOUT = 300L
    }

    override val coroutineContext: CoroutineContext
        get() = DefaultDispatcherProvider().main() + SupervisorJob() + CoroutineExceptionHandler { _, throwable ->
            Timber.e(throwable, "Navigation Error")
        }


    var activity: Activity? = null

    private var lastNavigationTimeStamp: Long = 0L
    private val _routeFlow = MutableSharedFlow<Route>()

    private val routeFlow = _routeFlow.distinctUntilChanged { _, _ ->
        val now = System.currentTimeMillis()
        val timeElapsed = (lastNavigationTimeStamp + NAVIGATION_TIMEOUT) < now
        return@distinctUntilChanged !timeElapsed
    }

    init {
        launch {
            routeFlow.collectLatest(::navigateInternal)
        }
    }


    override fun navigate(route: Route) {
        launch {
            _routeFlow.emit(route)
        }
    }

    private fun navigateInternal(route: Route) {
        lastNavigationTimeStamp = System.currentTimeMillis()
        when (route) {

            is Route.BottomMenuFragment -> navigateToBottomMenuFragment()

            //auth
            is Route.AuthenticationFragment -> navigateToAuthenticationFragment()
            is Route.LogOut -> logout()



        }
    }

    fun logout() = (activity as? SingleActivity?)?.rootController()?.navigate(R.id.action_log_out)

    private fun navigateToBottomMenuFragment() {
        (activity as? SingleActivity?)?.rootController()
            ?.navigate(R.id.action_to_BottomMenuFragment)
    }

    private fun navigateToAuthenticationFragment() {
        (activity as? SingleActivity?)?.rootController()
            ?.navigate(R.id.action_to_AuthenticationFragment)
    }

}