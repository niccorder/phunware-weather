package me.niccorder.phunware.location.data;

import java.util.List;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import me.niccorder.phunware.data.model.Location;

/**
 * Our data-layer contract which handles logic for displaying/retrieving information relating to a
 * location.
 * <p/>
 * This allows us to easily keep the android/java barrier in place for unit testing (as well as
 * allows us easier unit testing by mocking).
 */
public interface LocationRepository {

  /**
   * @return a {@link Flowable} which emits all currently stored locations.
   */
  @NonNull
  Flowable<List<Location>> getLocations();

  /**
   * Converts a zip-code to a {@link Location}.
   *
   * @param zipCode a 5 digit-only string zip code.
   * @return a {@link Flowable} which emits the Location zip equivalent.
   */
  @NonNull
  Flowable<Location> getLocation(@NonNull final String zipCode);

  /**
   * Add's a location to the repository.
   *
   * @param location to be added.
   * @return a {@link Single} which emit's the original location once it has been added.
   */
  @NonNull
  Single<Location> addLocation(@NonNull final Location location);
}
