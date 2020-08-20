package com.radiostudies.main.common.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

/**
 * from https://github.com/googlesamples/android-architecture-components/tree/master/GithubBr
 *
 * Created by eduardo.delito on 8/17/20.
 */
class ViewModelFactory
@Inject constructor(private val creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>)
    : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val creator = creators[modelClass] ?: creators.entries.firstOrNull {
            modelClass.isAssignableFrom(it.key)
        }?.value ?: throw IllegalArgumentException("unknown model class $modelClass")
        try {
            @Suppress("UNCHECKED_CAST")
            return creator.get() as T
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}
