package ${packageName}

import com.carmabs.ema.core.viewmodel.EmaViewModel


class ${functionalityName}ViewModel: EmaViewModel<${functionalityName}State,<#if navigator?has_content>${navigator}<#else>${functionalityName}Navigator</#if>.Navigation>(){
	
	override fun onStartFirstTime(statePreloaded: Boolean) {
    
    }

    override fun onResume(firstTime:Boolean){

    }

   override val initialViewState: ${functionalityName}State = ${functionalityName}State()
   
}