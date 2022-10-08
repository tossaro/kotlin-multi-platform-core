package tossaro.android.core.example.app.stockedit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import tossaro.android.core.app.BaseFragment
import tossaro.android.core.example.ExampleRouters
import tossaro.android.core.example.R
import tossaro.android.core.example.databinding.StockEditFragmentBinding
import tossaro.android.core.external.constant.AppConstant
import tossaro.android.core.external.extension.goTo
import tossaro.android.core.external.extension.showSuccessSnackbar
import tossaro.android.core.external.utility.ValidationUtil

class StockEditFragment :
    BaseFragment<StockEditFragmentBinding>(R.layout.stock_edit_fragment), MenuProvider {
    override fun actionBarTitle() = arguments?.getString("coin") ?: " "
    override fun showActionBarSearch() = false
    override fun showActionBarTitleOnExpanded() = false
    override fun expandActionBar() = true
    override fun showActionBarInfo() = true
    override fun showActionBarSearchFilter() = true
    override fun actionBarContentScrim() = R.color.grey100

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
        sharedPreferences.edit().putBoolean(AppConstant.FILTERED, true).apply()
        setActionBarSearchIcon()
        Toast.makeText(requireContext(), "filter set", Toast.LENGTH_SHORT).show()
    }

    @Suppress("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val images = listOf(
            R.drawable.c,
            R.drawable.c,
            R.drawable.c,
            R.drawable.c,
        )
        actionBarViewPager().adapter = StockEditImageAdapter(images.take(3))
        TabLayoutMediator(
            actionBarDotIndicator(),
            actionBarViewPager()
        ) { _, _ -> }.attach()
        actionBarInfoText().text = getString(R.string.stock_edit_image_info, images.size)
        sharedPreferences.edit().remove(AppConstant.FILTERED).apply()
        binding.btnSubmit.setOnClickListener {
            val a = ValidationUtil.notBlank(binding.a.text.toString())
            val b = ValidationUtil.minCharacter(8, binding.b.text.toString())
            val c = ValidationUtil.emailFormat(binding.c.text.toString())
            val d = ValidationUtil.phoneFormat("+62", binding.d.text.toString())
            showSuccessSnackbar("$a, $b, $c, $d")
            goTo(ExampleRouters.STOCK_EDIT.replace("{coin}", arguments?.getString("coin") ?: ""))
        }
    }
}