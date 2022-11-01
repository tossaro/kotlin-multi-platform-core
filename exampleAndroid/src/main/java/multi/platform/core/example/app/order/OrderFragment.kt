package multi.platform.core.example.app.order

import android.os.Bundle
import android.view.View
import androidx.fragment.app.setFragmentResultListener
import multi.platform.core.example.R
import multi.platform.core.example.databinding.OrderFragmentBinding
import multi.platform.core.shared.app.common.BaseFragment
import multi.platform.core.shared.app.common.GenericAdapter
import multi.platform.core.shared.domain.common.entity.GenericItem
import multi.platform.core.shared.external.constant.AppConstant
import multi.platform.core.shared.external.extension.dpToPx
import multi.platform.core.shared.external.extension.goTo
import multi.platform.core.shared.R as cR

class OrderFragment : BaseFragment<OrderFragmentBinding>(R.layout.order_fragment) {
    var selectedDate = "12-12-2012"
    override fun actionBarTitle() = getString(cR.string.menu_order)
    override fun showBottomNavBar() = true
    override fun showActionBarSearch() = true
    override fun showActionBarSearchFilter() = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(AppConstant.SELECT_DATE) { _, b ->
            b.getString(AppConstant.SELECT_DATE)?.let {
                selectedDate = it
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        actionBar().apply {
            setPadding(0, 0, dpToPx(24f), 0)
        }
        val imgStr =
            "https://images.unsplash.com/photo-1518509562904-e7ef99cdcc86?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1074&q=80"
        val provinceCompactAdapter = GenericAdapter(0.6, 124, 8, 4)
        provinceCompactAdapter.items = mutableListOf(
            GenericItem(
                titleOverlay = "Title 1",
                subtitleOverlay = "Sub Title 1",
                fullImage = imgStr,
            ),
            GenericItem(
                titleOverlay = "Title 2",
                subtitleOverlay = "Sub Title 2",
                fullImage = imgStr,
            ),
            GenericItem(
                titleOverlay = "Title 3",
                subtitleOverlay = "Sub Title 3",
                fullImage = imgStr,
            )
        )
        binding.rvProvinceCompact.adapter = provinceCompactAdapter

        val provinceAdapter = GenericAdapter(1.0, 240, 8, 4)
        provinceAdapter.items = mutableListOf(
            GenericItem(
                topImage = imgStr,
                topTags = mutableListOf("Alam", "Seni"),
                title = "Jawa Barat",
                subtitle = "1 Desa Wisata"
            )
        )
        binding.rvProvince.adapter = provinceAdapter

        val villageAdapter = GenericAdapter(1.0, 240, 8, 4)
        villageAdapter.items = mutableListOf(
            GenericItem(
                topImage = imgStr,
                title = "Jawa Barat",
                subtitle = "Kec. Cisarua, Kab. Bogor, Jawa Barat",
                subtitleIconRes = cR.drawable.ic_pin,
                description = "Sisa kuota: kurang dari 5 orang",
                descriptionIconRes = cR.drawable.ic_user
            )
        )
        binding.rvVillage.adapter = villageAdapter

        val homeStayAdapter = GenericAdapter(0.4, 150, 8, 4)
        homeStayAdapter.items = mutableListOf(
            GenericItem(
                fullImage = imgStr,
                name = "Penginapan Ari"
            ),
            GenericItem(
                fullImage = imgStr,
                name = "Penginapan Lestari"
            ),
            GenericItem(
                fullImage = imgStr,
                name = "Penginapan Dewi"
            ),
        )
        binding.rvHomestay.adapter = homeStayAdapter

        val tripCompactAdapter = GenericAdapter(0.9, 140, 8, 4)
        tripCompactAdapter.items = mutableListOf(
            GenericItem(
                leftImage = imgStr,
                title = "Arum Jeram 1",
                bottomTags = mutableListOf("Alam", "Seni"),
                subtitle = "Desa Batulayang, Bogor, Jawa Barat ",
                middleDiscount = "Rp 500.000",
                middlePrice = "Rp 105.000",
                middlePriceUnit = "/orang"
            ),
            GenericItem(
                leftImage = imgStr,
                title = "Arum Jeram 2",
                bottomTags = mutableListOf("Alam", "Seni"),
                subtitle = "Desa Batulayang, Bogor, Jawa Barat ",
                middleDiscount = "Rp 300.000",
                middlePrice = "Rp 115.000",
                middlePriceUnit = "/orang"
            )
        )
        binding.rvTripCompact.adapter = tripCompactAdapter

        val tripAdapter = GenericAdapter(1.0, 261, 8, 4)
        tripAdapter.items = mutableListOf(
            GenericItem(
                topImage = imgStr,
                title = "Arum Jeram",
                topTags = mutableListOf("Alam", "Seni"),
                subtitle = "Kec. Cisarua, Kab. Bogor, Jawa Barat",
                description = "Sisa kuota: 2 orang",
                descriptionIconRes = cR.drawable.ic_user,
                rightDiscount = "Rp 500.000",
                rightPrice = "Rp 105.000"
            )
        )
        binding.rvTrip.adapter = tripAdapter

        val articleAdapter = GenericAdapter(0.9, 88, 8, 4)
        articleAdapter.onClick = {
            goTo(
                getString(R.string.route_date_sheet)
                    .replace("{title}", getString(R.string.title))
                    .replace("{selected}", selectedDate)
                    .replace("{min}", "07-12-2012")
                    .replace("{max}", "21-03-2013")
            )
        }
        articleAdapter.items = mutableListOf(
            GenericItem(
                leftImage = imgStr,
                title = "Desa Batulayang dengan view yang menarik 1",
                moreInfo = "Baca selengkapnya"
            ),
            GenericItem(
                leftImage = imgStr,
                title = "Desa Batulayang dengan view yang menarik 2",
                moreInfo = "Baca selengkapnya"
            )
        )
        binding.rvArticle.adapter = articleAdapter
    }
}