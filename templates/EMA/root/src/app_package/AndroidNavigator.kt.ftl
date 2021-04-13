package ${packageName}

import android.app.Activity
import androidx.navigation.NavController
import com.carmabs.ema.android.navigation.EmaAndroidNavigator

class ${functionalityName}AndroidNavigator(
    override val navController: NavController,
    override val activity:Activity
) : ${functionalityName}EmaAndroidNavigator<${functionalityName}Navigator.Navigation>,${functionalityName}Navigator {

    override fun toDestination() {

    }
}