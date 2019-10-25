package me.niccorder.phunware.feature.location.ui

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_location.view.*
import me.niccorder.phunware.feature.location.R
import me.niccorder.phunware.model.Location
import me.niccorder.scopes.ActivityScope
import javax.inject.Inject

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
        View.inflate(context, R.layout.item_location, null)
      )
  }
}

/**
 * The [RecyclerView.Adapter] that binds the [Location]'s to their respective [RecyclerView.ViewHolder]'s.
 *
 * @see LocationHolder
 */
@ActivityScope
class LocationAdapter @Inject constructor() : ListAdapter<Location, LocationHolder>(
  LocationAdapterDiffer
) {

  private val locationClickSubject = PublishSubject.create<Location>()
  val locationClicks: Observable<Location> = locationClickSubject

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): LocationHolder = LocationHolder.create(
    parent.context
  ).apply {
    itemView.setOnClickListener {
      locationClickSubject.onNext(getItem(adapterPosition))
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