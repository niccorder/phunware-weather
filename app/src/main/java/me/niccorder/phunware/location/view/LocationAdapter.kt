package me.niccorder.phunware.location.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_location.view.*
import me.niccorder.phunware.R.layout
import me.niccorder.phunware.model.Location

/**
 * The [RecyclerView.ViewHolder] for a [Location] model.
 */
class LocationHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

  private val title = itemView.title
  private val description = itemView.description
  private val zipCode = itemView.zip_code

  @SuppressLint("SetTextI18n")
  fun bind(location: Location) {
    title.text = location.city
    description.text = "${location.latitude}, ${location.longitude}"
    zipCode.text = location.zipCode
  }

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
 */
class LocationAdapter(
  private val onLocationClick: (position: Int) -> Unit
) : ListAdapter<Location, LocationHolder>(LocationAdapterDiffer) {
  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): LocationHolder = LocationHolder.create(parent.context).apply {
    itemView.setOnClickListener {
      onLocationClick.invoke(adapterPosition)
    }
  }

  override fun onBindViewHolder(holder: LocationHolder, position: Int) {
    holder.bind(getItem(position))
  }
}

object LocationAdapterDiffer : ItemCallback<Location>() {
  override fun areItemsTheSame(
    oldItem: Location,
    newItem: Location
  ): Boolean = oldItem.zipCode == newItem.zipCode

  override fun areContentsTheSame(
    oldItem: Location,
    newItem: Location
  ): Boolean = oldItem.city == newItem.city &&
      oldItem.latitude == newItem.latitude &&
      oldItem.longitude == newItem.longitude
}