package multi.platform.core.example.app.profile

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Lifecycle
import multi.platform.core.example.R
import multi.platform.core.example.databinding.ProfileFragmentBinding
import multi.platform.core.shared.CoreApplication
import multi.platform.core.shared.app.common.BaseFragment
import multi.platform.core.shared.external.constant.AppConstant
import multi.platform.core.shared.external.extension.*
import multi.platform.example_lib.shared.app.profile.ProfileViewModel
import multi.platform.example_lib.shared.external.constant.ExampleConstant
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import multi.platform.core.shared.R as cR


class ProfileFragment : BaseFragment<ProfileFragmentBinding>(R.layout.profile_fragment) {
    private val vm: ProfileViewModel by viewModel()

    override fun actionBarTitle() = getString(cR.string.menu_profile)
    override fun showBottomNavBar() = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(ExampleConstant.SIGN_OUT) { _, b ->
            if (b.getBoolean(ExampleConstant.SIGN_OUT)) onResume()
        }
        setFragmentResultListener(AppConstant.RETRY_REQ) { _, b ->
            if (b.getString(AppConstant.RETRY_REQ, "") == "bold") {
                showToast("retrying bold")
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = vm.also {
            it.loadingIndicator.launchAndCollectIn(this, Lifecycle.State.STARTED) { l ->
                showFullLoading(l)
            }
            it.errorMessage.launchAndCollectIn(this, Lifecycle.State.STARTED) { m ->
                showErrorSnackbar(m)
                it.errorMessage.value = null
            }
            it.toastMessage.launchAndCollectIn(this, Lifecycle.State.STARTED) { m ->
                showToast(m)
                it.toastMessage.value = null
            }
            it.forceSignout.launchAndCollectIn(this, Lifecycle.State.STARTED) { ok ->
                if (ok) {
                    sharedPreferences.edit().remove(AppConstant.ACCESS_TOKEN)
                        .remove(AppConstant.REFRESH_TOKEN).remove(AppConstant.PHONE).apply()
                    onResume()
                    it.forceSignout.value = false
                }
            }
            it.user.launchAndCollectIn(this, Lifecycle.State.STARTED) { p ->
                binding.ivProfile.loadImage(p?.picture.toString())
            }
            it.onServerError.launchAndCollectIn(this, Lifecycle.State.STARTED) { nok ->
                if (nok) {
                    it.errorMessage.value = getString(cR.string.something_wrong)
                    it.onServerError.value = false
                }
            }
        }

        binding.ivProfile.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startForResultFromGallery.launch(intent)
        }

        val coreApp = (requireActivity().applicationContext as CoreApplication)
        binding.tvRegular.text = coreApp.protocol().name
        binding.tvSemiBold.text = coreApp.host()
        binding.tvBold.text = coreApp.sharedPrefsName()

        binding.tvBold.setOnClickListener {
            goTo(getString(R.string.route_error_connection_dialog).replace("{key}", "bold"))
        }

        binding.btnSignIn.setOnClickListener {
            sharedPreferences.edit()
                .putString(AppConstant.ACCESS_TOKEN, getString(R.string.sample_access_token))
                .putString(AppConstant.REFRESH_TOKEN, getString(R.string.sample_refresh_token))
                .putString(AppConstant.PHONE, getString(R.string.sample_phone)).apply()
            onResume()
        }

        binding.btnSignOut.setOnClickListener {
            goTo(getString(R.string.route_signout_dialog))
        }
    }

    override fun onResume() {
        super.onResume()
        vm.accessToken = sharedPreferences.getString(AppConstant.ACCESS_TOKEN, null)
        vm.checkToken()
        binding.btnSignIn.isVisible = vm.accessToken == null
        binding.btnSignOut.isVisible = vm.accessToken != null
    }

    private val startForResultFromGallery =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                try {
                    if (result.data != null) {
                        val selectedImageUri = result.data?.data
                        selectedImageUri?.let {
                            val bitmap = BitmapFactory.decodeStream(
                                requireContext().contentResolver.openInputStream(it)
                            )
                            binding.ivProfile.setImageBitmap(bitmap)
                        }
                    }
                } catch (exception: Exception) {
                    Timber.d("TAG", "" + exception.localizedMessage)
                }
            }
        }

}