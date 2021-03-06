package me.niccorder.phunware.util.injection.lifecycle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.niccorder.scopes.AppScope
import javax.inject.Inject
import javax.inject.Provider

@AppScope
class AppViewModelFactory @Inject constructor(
  private val creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) : ViewModelProvider.Factory {

  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    val creator =
      creators[modelClass]
        ?: creators.asIterable().firstOrNull { modelClass.isAssignableFrom(it.key) }?.value
        ?: throw IllegalArgumentException("unknown model class $modelClass")

    return try {
      creator.get() as T
    } catch (e: Exception) {
      throw RuntimeException(e)
    }
  }
}