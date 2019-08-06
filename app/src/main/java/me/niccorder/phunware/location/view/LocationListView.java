package me.niccorder.phunware.location.view;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import me.niccorder.phunware.model.Location;
import me.niccorder.phunware.location.presenter.LocationListPresenter;

/**
 * The view contract which the presenter interacts with to display information for the list of
 * {@link Location} models.
 *
 * @see LocationListPresenter
 */
public interface LocationListView {

  /**
   * Show/Hide an input prompt for a user to add a location.
   */
  void showAddLocationInput(final boolean show);

  /**
   * Set the error to be displayed for input issues with adding a location.
   */
  void setAddLocationInputError(@Nullable final Throwable throwable);

  /**
   * Refreshes the view state for the {@link Location} at the given position.
   */
  void notifyLocationAdded(int position);

  /**
   * Refreshes the view state for all locations.
   */
  void refreshLocations();

  /**
   * Displays the weather for the given {@link Location}.
   */
  void showWeather(@NonNull final Location location);

  /**
   * Shows a user-friendly error prompt if we run into any errors.
   */
  void showError(@NonNull final Throwable throwable);
}
