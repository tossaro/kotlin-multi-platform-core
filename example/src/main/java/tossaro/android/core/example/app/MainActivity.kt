package tossaro.android.core.example.app

import tossaro.android.core.app.common.BaseActivity
import tossaro.android.core.example.R

class MainActivity : BaseActivity() {
    override fun appVersion() = getString(R.string.app_version)
    override fun navGraph() = R.navigation.main_nav_graph
    override fun topLevelDestinations(): Set<Int> {
        val list = HashSet<Int>()
        list.add(R.id.signInFragment)
        list.add(R.id.homeFragment)
        list.add(R.id.profileFragment)
        list.add(R.id.stockDetailSheetFgragment)
        return list
    }
}