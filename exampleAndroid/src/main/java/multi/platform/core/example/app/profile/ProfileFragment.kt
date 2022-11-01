package multi.platform.core.example.app.profile

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import multi.platform.core.example.R
import multi.platform.core.example.databinding.ProfileFragmentBinding
import multi.platform.core.example.external.constant.ExampleConstant
import multi.platform.core.shared.app.common.BaseFragment
import multi.platform.core.shared.external.constant.AppConstant
import multi.platform.core.shared.external.extension.goTo
import multi.platform.core.shared.external.extension.loadImage
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = vm

        val scope = viewLifecycleOwner.lifecycleScope
        scope.launchWhenResumed { vm.user.collect { binding.ivProfile.loadImage(it?.picture.toString()) } }

        binding.ivProfile.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startForResultFromGallery.launch(intent)
        }

        binding.btnSignOut.setOnClickListener {
            goTo(getString(R.string.route_signout_dialog))
        }

        sharedPreferences.edit()
            .putString(AppConstant.REFRESH_TOKEN, getString(R.string.sample_refresh_token))
            .putString(AppConstant.PHONE, getString(R.string.sample_phone)).apply()
    }

    override fun onResume() {
        super.onResume()
        vm.accessToken = getString(R.string.sample_access_token)
        vm.checkToken()
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