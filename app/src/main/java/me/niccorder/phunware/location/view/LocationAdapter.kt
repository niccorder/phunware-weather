package me.niccorder.phunware.location.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_location.view.*
import me.niccorder.phunware.R.layout
import me.niccorder.phunware.internal.widget.recycler.DispatchingRecyclerAdapter
import me.niccorder.phunware.model.Location

/**
 * The [RecyclerView.ViewHolder] for a [Location] model.
 */
class LocationHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

  internal val title = itemView.title
  internal val description = itemView.description
  internal val zipCode = itemView.zip_code

  companion object {
    fun create(context: Context): LocationHolder =
      LocationHolder(
        View.inflate(context, layout.item_location, null)
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
  positionToItemMapper: (position: Int) -> me.niccorder.phunware.model.Location,
  countFun: () -> Int,
  private val onLocationClick: (position: Int) -> Unit
) : DispatchingRecyclerAdapter<me.niccorder.phunware.model.Location, LocationHolder>(
  positionToItemMapper,
  countFun
) {
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationHolder {
    val holder =
      LocationHolder.create(parent.context)
    holder.itemView.setOnClickListener { onLocationClick.invoke(holder.adapterPosition) }

    return holder
  }

  @SuppressLint("SetTextI18n")
  override fun onBindViewHolder(holder: LocationHolder, position: Int) {
    val location = positionToItemMapper.invoke(position)

    holder.title.text = location.city
    holder.description.text = "${location.latitude}, ${location.longitude}"
    holder.zipCode.text = location.zipCode
  }
}