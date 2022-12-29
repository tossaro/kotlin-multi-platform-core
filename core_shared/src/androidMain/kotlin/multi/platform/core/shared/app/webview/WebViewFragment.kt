package multi.platform.core.shared.app.webview

import android.net.Uri
import android.os.Bundle
import android.view.*
import android.webkit.*
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import multi.platform.core.shared.R
import multi.platform.core.shared.app.common.BaseFragment
import multi.platform.core.shared.databinding.WebviewFragmentBinding
import multi.platform.core.shared.external.constant.AppConstant
import java.net.URLDecoder

class WebViewFragment : BaseFragment<WebviewFragmentBinding>(R.layout.webview_fragment) {

    private var mOnScrollChangedListener: ViewTreeObserver.OnScrollChangedListener? = null
    private var titleArgs: String? = null
    private var urlArgs: String? = null

    companion object {
        const val TYPE_PAYMENT = "payment"
    }

    override fun actionBarTitle() = titleArgs ?: ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        titleArgs = arguments?.getString("title", null)
        urlArgs = arguments?.getString("url", null)
    }

    @Suppress("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showFullLoading(true)
        if (arguments?.getString("type") == TYPE_PAYMENT) {
            binding.nsvWebview.isFillViewport = true
        }

        binding.nsvWebview.isNestedScrollingEnabled = false
        binding.srlWebview.setOnRefreshListener {
            binding.webview.reload()
            if (binding.srlWebview.isRefreshing) binding.srlWebview.isRefreshing = false
        }
        binding.webview.apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true

            clearCache(true)
            webViewClient = MyWebViewClient()

            setOnLongClickListener { true }
            isLongClickable = false
            urlArgs?.let { loadUrl(URLDecoder.decode(it, "utf-8")) }
        }
    }

    override fun onStart() {
        super.onStart()
        mOnScrollChangedListener = ViewTreeObserver.OnScrollChangedListener {
            binding.srlWebview.isEnabled = binding.webview.scrollY == 0
        }
        binding.srlWebview.viewTreeObserver?.addOnScrollChangedListener(mOnScrollChangedListener)
    }

    override fun onStop() {
        binding.srlWebview.viewTreeObserver?.removeOnScrollChangedListener(mOnScrollChangedListener)
        super.onStop()
    }

    inner class MyWebViewClient : WebViewClient() {
        override fun onReceivedError(
            view: WebView?,
            request: WebResourceRequest?,
            error: WebResourceError?
        ) {
            super.onReceivedError(view, request, error)
            binding.emptyView.ivEmpty.apply {
                setImageResource(android.R.drawable.ic_dialog_alert)
                setColorFilter(ContextCompat.getColor(requireContext(), R.color.info))
            }
            binding.emptyView.tvEmpty.text = getString(R.string.something_wrong)
            binding.emptyView.isEmpty = true
            binding.nsvWebview.isFillViewport = true
            binding.webview.isVisible = false
        }

        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            return request?.url?.let {
                val uriStr = it.toString()
                when {
                    uriStr.contains("transaction/finish") -> {
                        val orderId = Uri.parse(uriStr).getQueryParameter("order_id").toString()
                        setFragmentResult(
                            AppConstant.PAYMENT_REQ,
                            bundleOf(AppConstant.PAYMENT_REQ to orderId)
                        )
                        findNavController().navigateUp()
                        true
                    }
                    else -> {
                        binding.webview.loadUrl(uriStr)
                        false
                    }
                }
            } ?: false
        }

        override fun onPageFinished(view: WebView, url: String) {
            showFullLoading(false)
            super.onPageFinished(view, url)
        }
    }
}
