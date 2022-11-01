package multi.platform.core.example.app

import multi.platform.core.example.R
import multi.platform.core.shared.app.common.BaseActivity

class MainActivity : BaseActivity() {
    override fun appVersion() = getString(R.string.app_version)
    override fun navGraph() = R.navigation.main_nav_graph
    override fun topLevelDestinations(): Set<Int> {
        val list = HashSet<Int>()
        list.add(R.id.homeFragment)
        list.add(R.id.profileFragment)
        list.add(R.id.stockDetailSheetFgragment)
        return list
    }
}