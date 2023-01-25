package com.app.common.navigation

sealed class Route {

    object LogOut : Route()

    object BottomMenuFragment : Route()

    //auth
    object AuthenticationFragment : Route()

}
