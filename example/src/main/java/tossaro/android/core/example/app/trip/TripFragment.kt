package tossaro.android.core.example.app.trip

import tossaro.android.core.app.BaseFragment
import tossaro.android.core.example.R
import tossaro.android.core.example.databinding.TripFragmentBinding

class TripFragment : BaseFragment<TripFragmentBinding>(R.layout.trip_fragment) {
    override fun isActionBarShown() = false
}