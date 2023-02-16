package multi.platform.example_lib.shared.app.stockedit

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import multi.platform.core.shared.app.common.BaseFragment
import multi.platform.core.shared.app.common.GenericAdapter
import multi.platform.core.shared.domain.common.entity.GenericItem
import multi.platform.core.shared.external.constant.AppConstant
import multi.platform.core.shared.external.extension.goTo
import multi.platform.core.shared.external.extension.launchAndCollectIn
import multi.platform.core.shared.external.extension.showErrorSnackbar
import multi.platform.core.shared.external.utility.Validation
import multi.platform.example_lib.shared.R
import multi.platform.example_lib.shared.databinding.StockEditFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.net.URLEncoder
import multi.platform.core.shared.R as cR

class StockEditFragment :
    BaseFragment<StockEditFragmentBinding>(R.layout.stock_edit_fragment), MenuProvider {
    private val vm: StockEditViewModel by viewModel()

    override fun actionBarTitle() = arguments?.getString("coin") ?: " "
    override fun showActionBarSearch() = false
    override fun showActionBarTitleOnExpanded() = false
    override fun expandActionBar() = true
    override fun showActionBarInfo() = true
    override fun showActionBarSearchFilter() = true
    override fun actionBarContentScrim() = cR.color.grey100

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreateMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_edit, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.love -> findNavController().navigateUp()
            R.id.share -> findNavController().navigateUp()
        }
        return false
    }

    override fun onTapFilterActionBarSearch() {
        sharedPreferences.edit().putBoolean(AppConstant.FILTERED_KEY, true).apply()
        setActionBarSearchIcon()
        Toast.makeText(requireContext(), "filter set", Toast.LENGTH_SHORT).show()
    }

    @Suppress("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = vm.also {
            it.errorEmptyField = getString(cR.string.error_empty_field)
            it.errorMinChar = getString(cR.string.error_min_character, 8)
            it.errorEmailFormat = getString(cR.string.error_email_format)
            it.errorPhoneFormat = getString(cR.string.error_phone_format)
            it.field1.launchAndCollectIn(this, Lifecycle.State.STARTED) { s ->
                val a = it.validateBlank(it.field1, it.field1Error)
                Timber.d("field1: $s: $a: ${it.field1Error.value}")
            }
            it.field2.launchAndCollectIn(this, Lifecycle.State.STARTED) { s ->
                val a = it.validateMinChar(8, it.field2, it.field2Error)
                Timber.d("field2: $s: $a: ${it.field2Error.value}")
            }
            it.field3.launchAndCollectIn(this, Lifecycle.State.STARTED) { s ->
                val a = it.validateEmailFormat(it.field3, it.field3Error)
                Timber.d("field3: $s: $a: ${it.field3Error.value}")
            }
            it.field4.launchAndCollectIn(this, Lifecycle.State.STARTED) { s ->
                val a = it.validatePhoneFormat(it.field4, it.field4Error)
                Timber.d("field4: $s: $a: ${it.field4Error.value}")
            }
        }

        val medias = listOf(
            "https://images.unsplash.com/photo-1518509562904-e7ef99cdcc86?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1074&q=80",
            "https://images.unsplash.com/photo-1667264151652-d7b6a5b77fed?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=687&q=80",
            "https://images.unsplash.com/photo-1646994354902-aca005ba7ab5?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1374&q=80",
            "https://images.unsplash.com/photo-1665383051584-79c224da663c?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1040&q=80",
            "https://images.unsplash.com/photo-1646297455102-320e7f364cf8?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1470&q=80"
        )

        val imageAdapter = GenericAdapter()
        imageAdapter.onSelected = {
            var images = ""
            medias.forEach {
                if (images.isNotEmpty()) images += ";"
                images += it
            }
            goTo(
                getString(R.string.route_gallery)
                    .replace("{title}", getString(R.string.title))
                    .replace("{images}", URLEncoder.encode(images, "utf-8"))
            )
        }
        imageAdapter.items.let { i ->
            medias.take(3).forEach { m ->
                i.add(GenericItem(id = 1, fullImage = m))
            }
        }
        actionBarExpandedViewPager()?.apply {
            adapter = imageAdapter
            actionBarExpandedDotIndicator()?.let { d ->
                TabLayoutMediator(
                    d,
                    this
                ) { _, _ -> }.attach()
            }
            actionBarExpandedInfoText()?.text =
                getString(R.string.stock_edit_image_info, medias.size - 1)
        }

        sharedPreferences.edit().remove(AppConstant.FILTERED_KEY).apply()
        binding.btnSubmit.setOnClickListener {
            val a = Validation.notBlank(binding.a.text)
            val b = Validation.minCharacter(8, binding.b.text)
            val c = Validation.emailFormat(binding.c.text)
            val d = Validation.phoneFormat(binding.d.text)
            showErrorSnackbar("$a, $b, $c, $d")
        }
    }
}