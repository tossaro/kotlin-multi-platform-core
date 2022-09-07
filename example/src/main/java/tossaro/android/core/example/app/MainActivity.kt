package tossaro.android.core.example.app

import androidx.navigation.fragment.NavHostFragment
import tossaro.android.core.app.BaseActivity
import tossaro.android.core.databinding.MainActivityBinding
import tossaro.android.core.example.R

class MainActivity : BaseActivity<MainActivityBinding>(
    R.layout.main_activity
) {
    override fun isDebugNavStack() = true
    override fun actionBar() = binding.toolbar
    override fun bottomNavBar() = binding.bottomNav
    override fun navHostFragment() = binding.contentFragment.getFragment() as NavHostFragment
    override fun mainNavGraph() = R.navigation.main_nav_graph
    override fun authNavGraph() = R.navigation.auth_nav_graph
    override fun topLevelDestinations(): Set<Int> {
        val list = HashSet<Int>()
        list.add(R.id.homeFragment)
        list.add(R.id.tripFragment)
        list.add(R.id.profileFragment)
        return list
    }
}