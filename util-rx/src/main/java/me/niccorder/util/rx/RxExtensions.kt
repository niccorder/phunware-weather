package me.niccorder.util.rx

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * A helper function to add a [Disposable] to a group [CompositeDisposable].
 */
infix fun Disposable.addTo(disposable: CompositeDisposable) = disposable.add(this)