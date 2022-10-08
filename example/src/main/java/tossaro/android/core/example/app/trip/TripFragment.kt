package tossaro.android.core.example.app.trip

import android.os.Bundle
import android.view.View
import tossaro.android.core.app.BaseFragment
import tossaro.android.core.example.R
import tossaro.android.core.example.databinding.TripFragmentBinding
import tossaro.android.core.external.extension.dpToPx

class TripFragment : BaseFragment<TripFragmentBinding>(R.layout.trip_fragment) {
    override fun actionBarTitle() = getString(R.string.menu_trip)
    override fun showBottomNavBar() = true
    override fun showActionBarSearch() = true
    override fun showActionBarSearchFilter() = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        actionBar().apply {
            setPadding(0, 0, dpToPx(24f), 0)
        }
    }
}