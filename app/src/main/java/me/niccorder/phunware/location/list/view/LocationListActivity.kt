package me.niccorder.phunware.location.list.view

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.InputType
import com.afollestad.materialdialogs.MaterialDialog
import com.google.gson.Gson
import com.jakewharton.rxbinding2.view.RxView
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import kotterknife.bindView
import me.niccorder.phunware.R
import me.niccorder.phunware.data.model.Location
import me.niccorder.phunware.location.list.presenter.LocationListPresenter
import me.niccorder.phunware.weather.view.WeatherActivity
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

/**
 * @see LocationListView
 */
class LocationListActivity : DaggerAppCompatActivity(), LocationListView {

    private val disposables: CompositeDisposable = CompositeDisposable()

    @Inject lateinit var presenter: LocationListPresenter
    @Inject lateinit var gson: Gson

    private val root: CoordinatorLayout by bindView(R.id.root)
    private val locationRecycler: RecyclerView by bindView(R.id.location_recycler)
    private val addLocationFab: FloatingActionButton by bindView(R.id.add_location_btn)

    private lateinit var adapter: LocationAdapter
    private var addLocationDialog: MaterialDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_list)

        initRecycler()
        disposables.add(
                RxView.clicks(addLocationFab)
                        .subscribe({ presenter.onAddLocationClick() }, Timber::e)
        )

        presenter.onLoadLocations()
    }

    private fun initRecycler() {
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        locationRecycler.addItemDecoration(DividerItemDecoration(this, layoutManager.orientation))
        locationRecycler.layoutManager = layoutManager

        adapter = LocationAdapter(
                presenter::getLocation,
                presenter::locationCount,
                presenter::onLocationClicked
        )
        locationRecycler.adapter = adapter

        adapter.notifyDataSetChanged()
    }

    override fun showAddLocationInput(show: Boolean) {
        addLocationDialog = MaterialDialog.Builder(this)
                .title(R.string.add_location_title)
                .cancelable(true)
                .inputType(InputType.TYPE_NUMBER_VARIATION_NORMAL)
                .inputRange(5, 5)
                .input(
                        R.string.add_location_input_hint,
                        0,
                        false,
                        { _, input -> Timber.d(input.toString()) }
                )
                .negativeText(android.R.string.cancel)
                .positiveText(R.string.add)
                .onPositive { dialog, _ ->
                    presenter.onAddLocation(dialog.inputEditText?.text.toString())
                }
                .onNegative { dialog, _ -> dialog.dismiss() }
                .show()
    }

    override fun notifyLocationAdded(position: Int) {
        adapter.notifyItemInserted(position)
    }

    override fun refreshLocations() {
        adapter.notifyDataSetChanged()
    }

    override fun showWeather(location: Location) {
        startActivity(
                Intent(this, WeatherActivity::class.java).putExtra(
                        Location.KEY_LOCATION,
                        gson.toJson(location)
                )
        )
    }

    override fun showError(throwable: Throwable) {
        val errorMessage = when (throwable) {
            is IOException -> getString(R.string.network_error)
            is HttpException -> {
                if (throwable.code() != 404)
                    getString(R.string.network_error)
                else getString(R.string.zip_not_found_error)
            }
            else -> getString(R.string.unknown_error)
        }

        Snackbar.make(root, errorMessage, Snackbar.LENGTH_SHORT).show()
    }

    override fun setAddLocationInputError(throwable: Throwable?) {
        addLocationDialog?.inputEditText?.error = when (throwable) {
            null -> null
            is IOException -> getString(R.string.network_error)
            is HttpException -> getString(R.string.network_error)
            is NumberFormatException -> getString(R.string.illegal_zip_error)
            is IllegalArgumentException -> getString(R.string.zip_length_error)
            else -> getString(R.string.unknown_error)
        }
    }
}