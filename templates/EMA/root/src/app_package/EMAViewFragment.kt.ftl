package ${packageName}

import com.carmabs.ema.android.ui.EmaFragment
import com.carmabs.ema.core.navigator.EmaNavigator
import com.carmabs.ema.core.state.EmaExtraData

import org.kodein.di.Kodein
import org.kodein.di.generic.instance

class ${functionalityName}Fragment : EmaFragment<${functionalityName}State, ${functionalityName}ViewModel, <#if navigator?has_content>${navigator}<#else>${functionalityName}Navigator</#if>.Navigation>() {

    override fun injectFragmentModule(kodein: Kodein.MainBuilder): Kodein.Module? = null

    override val fragmentViewModelScope: Boolean = true

    override val androidViewModelSeed: Android${functionalityName}ViewModel by instance()

    override val navigator: EmaNavigator<<#if navigator?has_content>${navigator}<#else>${functionalityName}Navigator</#if>.Navigation> by instance<<#if navigator?has_content>${navigator}<#else>${functionalityName}Navigator</#if>>()

    override fun onStateNormal(data: ${functionalityName}State) {

    }

    override fun onStateAlternative(data: EmaExtraData) {

    }

    override fun onSingleEvent(data: EmaExtraData) {

    }

    override fun onStateError(error: Throwable) {

    }

    override val layoutId: Int = R.layout.fragmentID

}