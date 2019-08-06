package me.niccorder.phunware.internal.widget.recycler

import androidx.recyclerview.widget.RecyclerView

open abstract class DispatchingRecyclerAdapter<T, VH : RecyclerView.ViewHolder>(
        internal val positionToItemMapper: (position: Int) -> T,
        private val countFun: () -> Int
) : RecyclerView.Adapter<VH>() {
    override fun getItemCount(): Int = countFun.invoke()
}