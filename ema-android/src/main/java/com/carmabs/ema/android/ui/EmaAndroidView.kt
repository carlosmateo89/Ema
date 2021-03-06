package com.carmabs.ema.android.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import com.carmabs.ema.android.viewmodel.EmaAndroidViewModel
import com.carmabs.ema.android.viewmodel.EmaFactory
import com.carmabs.ema.core.navigator.EmaNavigationState
import com.carmabs.ema.core.state.EmaBaseState
import com.carmabs.ema.core.state.EmaState
import com.carmabs.ema.core.view.EmaView
import com.carmabs.ema.core.viewmodel.EmaResultHandler
import com.carmabs.ema.core.viewmodel.EmaViewModel
import kotlinx.coroutines.Job


/**
 * View to handle VM view logic states through [EmaState].
 * The user must provide in the constructor by template:
 *  - The view model class [EmaViewModel] is going to use the view
 *  - The navigation state class [EmaNavigationState] will handle the navigation
 *
 * @author <a href="mailto:apps.carmabs@gmail.com">Carlos Mateo Benito</a>
 */
interface EmaAndroidView<S : EmaBaseState, VM : EmaViewModel<S, NS>, NS : EmaNavigationState> :
    EmaView<S, VM, NS> {

    val androidViewModelSeed: EmaAndroidViewModel<VM>

    fun initializeViewModel(
        fragmentActivity: FragmentActivity,
        fragment: Fragment? = null,
        resultHandler: EmaResultHandler? = null
    ): VM {
        val emaFactory = EmaFactory(androidViewModelSeed)
        val vm = (fragment?.let {
            ViewModelProviders.of(it, emaFactory)[androidViewModelSeed::class.java]
        } ?: ViewModelProviders.of(
            fragmentActivity,
            emaFactory
        )[androidViewModelSeed::class.java])

        return vm.emaViewModel
    }

    /**
     * Method called to start viewModel and bind data updated
     */
    fun onStartAndBindData(
        lifeCycleOwner: LifecycleOwner,
        viewModel: VM,
        resultHandler: EmaResultHandler? = null
    ): MutableList<Job> {
        return onStartView(lifeCycleOwner.lifecycleScope, viewModel)
    }


    suspend fun onStopBinding(
        job: MutableList<Job>?
    ) {
        onStopView(job)
    }
}