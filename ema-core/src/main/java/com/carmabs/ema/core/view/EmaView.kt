package com.carmabs.ema.core.view

import com.carmabs.ema.core.navigator.EmaBaseNavigator
import com.carmabs.ema.core.navigator.EmaNavigationState
import com.carmabs.ema.core.state.EmaBaseState
import com.carmabs.ema.core.state.EmaExtraData
import com.carmabs.ema.core.state.EmaState
import com.carmabs.ema.core.viewmodel.EmaReceiverModel
import com.carmabs.ema.core.viewmodel.EmaResultModel
import com.carmabs.ema.core.viewmodel.EmaViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.jvm.internal.PropertyReference0
import kotlin.reflect.KProperty


/**
 * View to handle VM view logic states through [EmaState].
 * The user must provide in the constructor by template:
 *  - The view model class [EmaViewModel] is going to use the view
 *  - The navigation state class [EmaNavigationState] will handle the navigation
 *
 * @author <a href="mailto:apps.carmabs@gmail.com">Carlos Mateo Benito</a>
 */
interface EmaView<S : EmaBaseState, VM : EmaViewModel<S, NS>, NS : EmaNavigationState> {

    /**
     * Scope for flow updates
     */
    val coroutineScope: CoroutineScope

    /**
     * The view mdeol seed [EmaViewModel] for the view
     */
    val viewModelSeed: VM

    /**
     * The navigator [EmaBaseNavigator]
     */
    val navigator: EmaBaseNavigator<NS>?

    /**
     * The state set up form previous views when it is launched.
     */
    val inputState: S?

    /**
     * The previous state of the View
     */
    var previousState: S?


    /**
     * Determine if the previousState updates automatically after onNormalState or if
     * has to be set up  manually
     */
    val updatePreviousStateAutomatically: Boolean


    /**
     * Called when view model trigger an update view event
     * @param state of the view
     */
    fun onDataUpdated(state: EmaState<S>) {
        onStateNormal(state.data)
        when (state) {
            is EmaState.Alternative -> {
                onStateAlternative(state.dataAlternative)
            }
            is EmaState.Error -> {
                onStateError(state.error)
            }
        }

        if (updatePreviousStateAutomatically)
            previousState = state.data
    }

    /**
     * Check EMA state selected property to execute action with new value if it has changed
     * @param action Action to execute. Current value passed in lambda.
     * @param field Ema State field to check if it has been changed.
     * @param areEqualComparator Comparator to determine if both objects are equals. Useful for complex objects
     */
    fun <T> bindForUpdate(
        field: KProperty<T>,
        areEqualComparator: ((previous: T?, current: T?) -> Boolean)? = null,
        action: (current: T?) -> Unit
    ) {
        val currentClass = (field as PropertyReference0).boundReceiver as? S
        currentClass?.also { _ ->
            val currentValue = field.get() as T
            previousState?.also {
                try {
                    val previousField = it.javaClass.getDeclaredField(field.name)
                    previousField.isAccessible = true
                    val previousValue = previousField.get(previousState) as T
                    if (areEqualComparator?.invoke(previousValue, currentValue)?.not()
                            ?: (previousValue != currentValue)
                    ) {
                        action.invoke(currentValue)
                    }
                } catch (e: Exception) {
                    println("EMA : Field not found")
                }
            } ?: action.invoke(currentValue)
        } ?: println("EMA : Bounding class must be the state of the view")
    }

    /**
     * Check EMA state selected property to execute action with new value if it has changed
     * @param action Action to execute. Current and previous value passed in lambda
     * @param field Ema State field to check if it has been changed
     * @param areEqualComparator Comparator to determine if both objects are equals. Useful for complex objects
     */
    fun <T> bindForUpdateWithPrevious(
        field: KProperty<T>,
        areEqualComparator: ((previous: T?, current: T?) -> Boolean)? = null,
        action: (previous: T?, current: T?) -> Unit
    ) {
        val currentClass = (field as PropertyReference0).boundReceiver as? S
        currentClass?.also { _ ->
            val currentValue = field.get() as T
            previousState?.also {
                try {
                    val previousField = it.javaClass.getDeclaredField(field.name)
                    previousField.isAccessible = true
                    val previousValue = previousField.get(previousState) as T
                    if (areEqualComparator?.invoke(previousValue, currentValue)?.not()
                            ?: (previousValue != currentValue)
                    ) {
                        action.invoke(previousValue, currentValue)
                    }
                } catch (e: Exception) {
                    println("EMA : Field not found")
                }
            } ?: action.invoke(null, currentValue)
        } ?: println("EMA : Bounding class must be the state of the view")
    }

    /**
     * Called when view model trigger a result event
     * @param result model
     */
    fun onResultSetHandled(result: EmaResultModel) {
        onResultSetEvent(result)
    }

    /**
     * Called when view model invoke a result receiver event
     * @param receiver model
     */
    fun onResultReceivedHandled(receiver: EmaReceiverModel) {
        onResultReceiverInvokeEvent(receiver)
    }

    /**
     * Called when view model trigger an only once notified event
     * @param data for extra information
     */
    fun onSingleData(data: EmaExtraData) {
        onSingleEvent(data)
    }

    /**
     * Called when view model trigger an only once notified event for navigation
     * @param navigation state with information about the destination
     */
    fun onNavigation(navigation: EmaNavigationState?) {
        navigation?.let {
            navigate(navigation)
        } ?: navigateBack()
    }

    /**
     * Called when view model trigger an update view event
     * @param data with the state of the view
     */
    fun onStateNormal(data: S)

    /**
     * Called when view model trigger a updateAlternativeState event
     * @param data with information about updateAlternativeState
     */
    fun onStateAlternative(data: EmaExtraData)

    /**
     * Called when view model trigger an only once notified event.Not called when the view is first time attached to the view model
     * @param data with information about updateAlternativeState
     */
    fun onSingleEvent(data: EmaExtraData)

    /**
     * Called when view model trigger an error event
     * @param error generated by view model
     */
    fun onStateError(error: Throwable)

    /**
     * Called when a result has been notified from view model
     * @param emaResultModel generated by view model
     */
    fun onResultSetEvent(emaResultModel: EmaResultModel)

    /**
     * Called when a result receiver has been invoked from view model
     * @param emaReceiverModel generated by view model
     */
    fun onResultReceiverInvokeEvent(emaReceiverModel: EmaReceiverModel)

    /**
     * Called when view model trigger a navigation event
     * @param state with info about destination
     */
    fun navigate(state: EmaNavigationState) {
        navigator?.navigate(state as NS)
    }

    /**
     * Called when view model trigger a navigation back event
     * @return True
     */
    fun navigateBack(): Boolean {
        return navigator?.navigateBack() ?: false
    }

    fun onStartView(coroutineScope: CoroutineScope, viewModel: VM): Job {
        return coroutineScope.launch {
            viewModel.apply {
                onStart(inputState?.let { EmaState.Normal(it) })
                getObservableState().collect {
                    onDataUpdated(it)
                }
                getSingleObservableState().collect {
                    onSingleData(it)
                }
                getNavigationState().collect {
                    onNavigation(it)
                }
            }
        }
    }

    fun onStopView(viewJob: Job?) {
        viewJob?.cancel()
    }
}