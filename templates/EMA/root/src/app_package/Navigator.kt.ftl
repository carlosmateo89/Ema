package ${packageName}

import com.carmabs.ema.core.navigator.EmaNavigator
import com.carmabs.ema.core.navigator.EmaNavigationState

interface ${functionalityName}Navigator : EmaNavigator<${functionalityName}Navigator.Navigation> {

    sealed class Navigation : EmaNavigationState {

        object Destination : ${functionalityName}Navigator.Navigation() {
            override fun navigateWith(navigator: EmaBaseNavigator<out EmaNavigationState>) {
              (navigator as? ${functionalityName}Navigator)?.toDestination()
            }
        }
    }

    fun toDestination() {

    }
}