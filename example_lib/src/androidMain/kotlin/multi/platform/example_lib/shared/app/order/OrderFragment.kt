package multi.platform.example_lib.shared.app.order

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import multi.platform.core.shared.app.common.BaseFragment
import multi.platform.core.shared.app.common.GenericAdapter
import multi.platform.core.shared.domain.common.entity.GenericItem
import multi.platform.core.shared.external.constant.AppConstant
import multi.platform.core.shared.external.extension.goTo
import multi.platform.example_lib.shared.R
import multi.platform.example_lib.shared.databinding.OrderFragmentBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.URLEncoder
import multi.platform.core.shared.R as cR


class OrderFragment : BaseFragment<OrderFragmentBinding>(R.layout.order_fragment) {
    var selectedDate = "12-12-2012"
    private val imgStr =
        "https://images.unsplash.com/photo-1518509562904-e7ef99cdcc86?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1074&q=80"

    override fun actionBarTitle() = getString(R.string.menu_order)
    override fun actionBarHeight() = 160
    override fun showBottomNavBar() = true
    override fun expandActionBar() = true
//    override fun showActionBarSearch() = true
//    override fun showActionBarSearchFilter() = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(AppConstant.SELECT_DATE_KEY) { _, b ->
            b.getString(AppConstant.SELECT_DATE_KEY)?.let {
                selectedDate = it
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        actionBar()?.apply {
//            setPadding(0, 0, dpToPx(24f), 0)
//        }
        actionBarExpandedDescription()?.apply {
            isVisible = true
            setText(getString(R.string.home_village_info))
        }
        actionBarExpandedAutoComplete()?.apply {
            isVisible = true
            setAdapter(
                ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    arrayListOf("Belgium", "France", "Italy", "Germany", "Spain")
                )
            )
        }
        setupProvinceCompact()
        setupProvince()
        setupVillage()
        setupHomestay()
        setupTripCompact()
        setupTrip()
        setupArticle()
    }

    private fun setupProvinceCompact() {
        val provinceCompactAdapter = GenericAdapter(0.6, 124, 8, 4)
        provinceCompactAdapter.skeleton = GenericItem(
            titleOverlay = "",
            subtitleOverlay = "",
            fullImage = "",
        )
        provinceCompactAdapter.showSkeletons(3)
        provinceCompactAdapter.onSelected = {
            goTo(
                getString(R.string.route_webview)
                    .replace("{type}", "website")
                    .replace("{title}", "Load 404")
                    .replace("{url}", URLEncoder.encode("http://poitiylesas.com", "utf-8"))
            )
        }
        provinceCompactAdapter.onClickMore = {
            viewLifecycleOwner.lifecycleScope.launch {
                provinceCompactAdapter.showSkeletons(3)
                delay(5000)
                provinceCompactAdapter.clear()
                provinceCompactAdapter.items = mutableListOf(
                    GenericItem(
                        id = 1,
                        titleOverlay = "Title 1",
                        subtitleOverlay = "Sub Title 1",
                        fullImage = imgStr,
                    ),
                    GenericItem(
                        id = 2,
                        titleOverlay = "Title 2",
                        subtitleOverlay = "Sub Title 2",
                        fullImage = imgStr,
                    ),
                    GenericItem(
                        id = 3,
                        titleOverlay = "Title 3",
                        subtitleOverlay = "Sub Title 3",
                        fullImage = imgStr,
                    )
                )
                provinceCompactAdapter.notifyItemRangeChanged(0, 2)
            }
        }
        binding.rvProvinceCompact.adapter = provinceCompactAdapter
        viewLifecycleOwner.lifecycleScope.launch {
            delay(10000)
            provinceCompactAdapter.showInfo(
                getString(cR.string.something_wrong),
                getString(cR.string.retry)
            )
        }
    }

    private fun setupProvince() {
        val provinceAdapter = GenericAdapter(1.0, 240, 8, 4)
        provinceAdapter.skeleton = GenericItem(
            topImage = "",
            topTags = mutableListOf("", "", "", ""),
            title = "",
            subtitle = ""
        )
        provinceAdapter.showSkeletons(1)
        binding.rvProvince.adapter = provinceAdapter
        viewLifecycleOwner.lifecycleScope.launch {
            delay(10000)
            provinceAdapter.showInfo(getString(cR.string.empty))
        }
    }

    private fun setupVillage() {
        val villageAdapter = GenericAdapter(1.0, 240, 8, 4)
        villageAdapter.skeleton = GenericItem(
            topImage = "",
            title = "",
            subtitle = "",
            description = "",
        )
        villageAdapter.showSkeletons(1)
        binding.rvVillage.adapter = villageAdapter
        viewLifecycleOwner.lifecycleScope.launch {
            delay(5000)
            villageAdapter.clear()
            villageAdapter.items = mutableListOf(
                GenericItem(
                    id = 1,
                    topImage = imgStr,
                    title = "Jawa Barat",
                    subtitle = "Kec. Cisarua, Kab. Bogor, Jawa Barat",
                    subtitleIconRes = R.drawable.ic_pin,
                    description = "Sisa kuota: kurang dari 5 orang",
                    descriptionIconRes = R.drawable.ic_user
                )
            )
            villageAdapter.notifyItemRangeChanged(0, 1)
        }
    }

    private fun setupHomestay() {
        val homeStayAdapter = GenericAdapter(0.4, 150, 8, 4)
        homeStayAdapter.skeleton = GenericItem(fullImage = "", name = "")
        homeStayAdapter.showSkeletons(3)
        binding.rvHomestay.adapter = homeStayAdapter
        viewLifecycleOwner.lifecycleScope.launch {
            delay(5000)
            homeStayAdapter.clear()
            homeStayAdapter.items = mutableListOf(
                GenericItem(
                    id = 1,
                    fullImage = imgStr,
                    name = "Penginapan Ari"
                ),
                GenericItem(
                    id = 2,
                    fullImage = imgStr,
                    name = "Penginapan Lestari"
                ),
                GenericItem(
                    id = 3,
                    fullImage = imgStr,
                    name = "Penginapan Dewi"
                ),
            )
            homeStayAdapter.notifyItemRangeChanged(0, 2)
        }
    }

    private fun setupTripCompact() {
        val tripCompactAdapter = GenericAdapter(0.9, 140, 8, 4)
        tripCompactAdapter.skeleton = GenericItem(
            leftImage = "",
            title = "",
            bottomTags = mutableListOf("", "", "", "", ""),
            subtitle = "",
            middleDiscount = "",
            middlePrice = "",
            middlePriceUnit = ""
        )
        tripCompactAdapter.showSkeletons(3)
        binding.rvTripCompact.adapter = tripCompactAdapter
        viewLifecycleOwner.lifecycleScope.launch {
            delay(5000)
            tripCompactAdapter.clear()
            tripCompactAdapter.items = mutableListOf(
                GenericItem(
                    id = 1,
                    leftImage = imgStr,
                    title = "Arum Jeram 1",
                    bottomTags = mutableListOf("Alam", "Seni"),
                    subtitle = "Desa Batulayang, Bogor, Jawa Barat ",
                    middleDiscount = "Rp 500.000",
                    middlePrice = "Rp 105.000",
                    middlePriceUnit = "/orang"
                ),
                GenericItem(
                    id = 2,
                    leftImage = imgStr,
                    title = "Arum Jeram 2",
                    bottomTags = mutableListOf("Alam", "Seni"),
                    subtitle = "Desa Batulayang, Bogor, Jawa Barat ",
                    middleDiscount = "Rp 300.000",
                    middlePrice = "Rp 115.000",
                    middlePriceUnit = "/orang"
                )
            )
            tripCompactAdapter.notifyItemRangeChanged(0, 1)
        }
    }

    private fun setupTrip() {
        val tripAdapter = GenericAdapter(1.0, 261, 8, 4)
        tripAdapter.skeleton = GenericItem(
            topImage = "",
            title = "",
            topTags = mutableListOf("", "", "", "", ""),
            subtitle = "",
            description = "",
            rightDiscount = "",
            rightPrice = ""
        )
        tripAdapter.showSkeletons(1)
        binding.rvTrip.adapter = tripAdapter
        viewLifecycleOwner.lifecycleScope.launch {
            delay(5000)
            tripAdapter.clear()
            tripAdapter.items = mutableListOf(
                GenericItem(
                    id = 1,
                    topImage = imgStr,
                    title = "Arum Jeram",
                    topTags = mutableListOf("Alam", "Seni"),
                    subtitle = "Kec. Cisarua, Kab. Bogor, Jawa Barat",
                    description = "Sisa kuota: 2 orang",
                    descriptionIconRes = R.drawable.ic_user,
                    rightDiscount = "Rp 500.000",
                    rightPrice = "Rp 105.000"
                )
            )
        }
    }

    private fun setupArticle() {
        val articleAdapter = GenericAdapter(0.9, 88, 8, 4)
        articleAdapter.skeleton = GenericItem(
            leftImage = "",
            title = "",
            moreInfo = ""
        )
        articleAdapter.showSkeletons(3)
        binding.rvArticle.adapter = articleAdapter
        viewLifecycleOwner.lifecycleScope.launch {
            delay(5000)
            articleAdapter.clear()
            articleAdapter.onSelected = {
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
                    id = 1,
                    leftImage = imgStr,
                    title = "Desa Batulayang dengan view yang menarik 1",
                    moreInfo = "Baca selengkapnya"
                ),
                GenericItem(
                    id = 2,
                    leftImage = imgStr,
                    title = "Desa Batulayang dengan view yang menarik 2",
                    moreInfo = "Baca selengkapnya"
                )
            )
        }
    }
}