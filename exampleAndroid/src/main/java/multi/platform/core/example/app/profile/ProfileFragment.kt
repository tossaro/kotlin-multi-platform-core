package multi.platform.core.example.app.profile

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Lifecycle
import multi.platform.core.example.R
import multi.platform.core.example.databinding.ProfileFragmentBinding
import multi.platform.core.example.external.constant.ExampleConstant
import multi.platform.core.shared.app.common.BaseFragment
import multi.platform.core.shared.external.constant.AppConstant
import multi.platform.core.shared.external.extension.goTo
import multi.platform.core.shared.external.extension.launchAndCollectIn
import multi.platform.core.shared.external.extension.loadImage
import multi.platform.core.shared.external.extension.showErrorSnackbar
import multi.platform.core.shared.external.extension.showToast
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
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return if (view != null) view else super.onCreateView(
            inflater,
            container,
            savedInstanceState
        )
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
        }

        binding.ivProfile.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startForResultFromGallery.launch(intent)
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
                } catch (e: Exception) {
                    e.printStackTrace()
                    Timber.d("TAG", "" + e.message.toString())
                }
            }
        }

}