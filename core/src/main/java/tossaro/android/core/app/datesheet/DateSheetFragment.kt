package tossaro.android.core.app.datesheet

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import tossaro.android.core.app.common.BaseSheetFragment
import tossaro.android.core.external.utility.LocaleUtil
import tossaro.android.core.R
import tossaro.android.core.databinding.DateSheetFragmentBinding
import tossaro.android.core.external.constant.AppConstant
import java.text.SimpleDateFormat
import java.util.Locale

class DateSheetFragment :
    BaseSheetFragment<DateSheetFragmentBinding>(R.layout.date_sheet_fragment) {
    override fun title() = arguments?.getString("title") ?: ""
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        val parser = SimpleDateFormat("dd-MM-yyyy", Locale(LocaleUtil.retrieveAppLanguage(requireContext(), LocaleUtil.ID)))

        try {
            val selected = arguments?.getString("selected")
            if (selected != null && selected != "{selected}") {
                parser.parse(selected)?.let { p -> binding.cvDate.date = p.time }
            }
            val min = arguments?.getString("min")
            if (min != null && min != "{min}") {
                parser.parse(min)?.let { p -> binding.cvDate.minDate = p.time }
            }
            val max = arguments?.getString("max")
            if (max != null && max != "{max}") {
                parser.parse(max)?.let { p -> binding.cvDate.maxDate = p.time }
            }
        } catch (e: Exception) {
            e.stackTrace
        }
        binding.cvDate.setOnDateChangeListener { _, y, m, d ->
            val parsed = parser.parse("$d-${m+1}-$y")
            parsed?.let {
                val dateStr = SimpleDateFormat("dd-MM-yyyy", Locale(LocaleUtil.retrieveAppLanguage(requireContext(), LocaleUtil.ID))).format(it)
                setFragmentResult(
                    AppConstant.SELECT_DATE,
                    bundleOf(AppConstant.SELECT_DATE to dateStr)
                )
                findNavController().navigateUp()
            }
        }
    }
}