package com.carmabs.ema.android.navigation

import android.app.Activity
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavDirections
import androidx.navigation.NavHost
import androidx.navigation.NavOptions
import com.carmabs.ema.core.navigator.EmaBaseNavigator
import com.carmabs.ema.core.navigator.EmaNavigationState
import com.carmabs.ema.core.state.EmaBaseState

/**
 * Project: Ema
 * Created by: cmateob on 20/1/19.
 */
interface EmaNavigator<NS : EmaNavigationState> : EmaBaseNavigator<NS> {

    val navHost: NavHost

    /**
     * Set the initial state for the incoming fragment. Only work for fragment at the same activity
     * at the moment
     * @param emaBaseState the state for the incoming view
     * @param inputStateKey the key to identify the state. If it not provided, it will take the canonical name
     * of the state
     */
    fun addInputState(emaBaseState: EmaBaseState,inputStateKey: String?=null) : Bundle =
            Bundle().apply { putSerializable(inputStateKey?:emaBaseState.javaClass.canonicalName, emaBaseState)
    }

    /**
     * Navigate with android architecture components within action ID
     * @param actionID
     * @param data
     * @param navOptions
     */
    fun navigateWithAction(@IdRes actionID:Int,data:Bundle?=null,navOptions: NavOptions?=null){
        navHost.navController.navigate(actionID,data,navOptions)
    }

    /**
     * Navigate with android architecture components within navDirections safeargs
     * @param navDirections
     * @param navOptions
     */
    fun navigateWithDirections(navDirections: NavDirections,navOptions: NavOptions?=null){
        navHost.navController.navigate(navDirections,navOptions)
    }

    /**
     * Finish the current activity
     */
    fun finishActivity(){
        (navHost as? Activity)?.finish()
    }

    /**
     * Navigates back
     */
    fun navigateBack(){
        navHost.navController.popBackStack()
    }
}