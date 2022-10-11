package tossaro.android.core.example.app.profile

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import timber.log.Timber
import tossaro.android.core.app.common.BaseFragment
import tossaro.android.core.example.ExampleRouters
import tossaro.android.core.example.R
import tossaro.android.core.example.databinding.ProfileFragmentBinding
import tossaro.android.core.example.external.constant.ExampleConstant
import tossaro.android.core.external.constant.AppConstant
import tossaro.android.core.external.extension.goTo


class ProfileFragment : BaseFragment<ProfileFragmentBinding>(R.layout.profile_fragment) {

    override fun actionBarTitle() = getString(R.string.menu_profile)
    override fun showBottomNavBar() = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(ExampleConstant.SIGN_OUT) { _, b ->
            if (b.getBoolean(ExampleConstant.SIGN_OUT)) onResume()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignIn.setOnClickListener {
            goTo(ExampleRouters.SIGN_IN)
        }
        binding.btnSignOut.setOnClickListener {
            goTo(ExampleRouters.SIGN_OUT)
        }
        binding.ivProfile.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startForResultFromGallery.launch(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        val accessToken = sharedPreferences.getString(AppConstant.ACCESS_TOKEN, null)
        binding.btnSignOut.isVisible = accessToken != null
        binding.btnSignIn.isVisible = accessToken == null
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