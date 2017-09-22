package me.niccorder.phunware.location.list.view

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotterknife.bindView
import me.niccorder.phunware.R
import me.niccorder.phunware.data.model.Location
import me.niccorder.phunware.internal.widget.recycler.DispatchingRecyclerAdapter

/**
 * The [RecyclerView.ViewHolder] for a [Location] model.
 */
class LocationHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    internal val title: TextView by bindView(R.id.title)
    internal val description: TextView by bindView(R.id.description)
    internal val zipCode: TextView by bindView(R.id.zip_code)

    companion object {
        fun create(context: Context): LocationHolder = LocationHolder(
                View.inflate(context, R.layout.item_location, null)
        )
    }
}

/**
 * The [RecyclerView.Adapter] that binds the [Location]'s to their respective [RecyclerView.ViewHolder]'s.
 *
 * @see LocationHolder
 * @see DispatchingRecyclerAdapter
 */
class LocationAdapter(
        positionToItemMapper: (position: Int) -> Location,
        countFun: () -> Int,
        private val onLocationClick: (position: Int) -> Unit
) : DispatchingRecyclerAdapter<Location, LocationHolder>(
        positionToItemMapper,
        countFun
) {
    override fun onCreateViewHolder(
            parent: ViewGroup?,
            viewType: Int
    ): LocationHolder {
        val holder = LocationHolder.create(parent!!.context)
        holder.itemView.setOnClickListener { onLocationClick.invoke(holder.adapterPosition) }

        return holder
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: LocationHolder?, position: Int) {
        val location = positionToItemMapper.invoke(position)

        holder!!.title.text = location.city
        holder.description.text = "${location.latitude}, ${location.longitude}"
        holder.zipCode.text = location.zipCode
    }
}