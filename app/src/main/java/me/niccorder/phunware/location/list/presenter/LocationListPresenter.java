package me.niccorder.phunware.location.list.presenter;

import io.reactivex.annotations.NonNull;
import me.niccorder.phunware.data.model.Location;

/**
 * Presenter for displaying a list of {@link Location} models.
 *
 * @see me.niccorder.phunware.location.list.view.LocationListView
 */
public interface LocationListPresenter {

  /**
   * To be called when the presenter will be destroyed.
   */
  void destroy();

  /**
   * Retrieves a location at a given position in the list.
   *
   * @param position of the {@link Location} to be retrieved.
   * @return the {@link Location} at that position.
   */
  @NonNull
  Location getLocation(int position);

  /**
   * @return the number of {@link Location}'s this presenter contains.
   */
  int locationCount();

  /**
   * To be called when a location has been clicked on in the list.
   *
   * @param position of the clicked {@link Location}
   */
  void onLocationClicked(final int position);

  /**
   * To be called when a location has been added.
   *
   * @param location that has been added.
   */
  void onLocationAdded(@NonNull final Location location);

  /**
   * To be called when there is a need to load/refresh all locations.
   */
  void onLoadLocations();

  /**
   * To be called when the user has clicked to add a location.
   */
  void onAddLocationClick();

  /**
   * To be called when the user has entered a zip-code of a location they wish to add.
   *
   * @param zipCode of the location to be added.
   */
  void onAddLocation(final String zipCode);
}
