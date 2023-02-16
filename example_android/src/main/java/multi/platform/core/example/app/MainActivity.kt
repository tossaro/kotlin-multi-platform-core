package multi.platform.core.example.app

import multi.platform.core.example.R
import multi.platform.core.shared.app.common.BaseActivity
import multi.platform.example_lib.shared.R as eR

class MainActivity : BaseActivity() {
    override fun appVersion() = getString(R.string.app_version)
    override fun navGraph() = R.navigation.main_nav_graph
    override fun actionBarExpandedAutoCompleteHint() = eR.string.search_hint
    override fun actionBarSearchHint() = eR.string.search_hint
    override fun bottomNavBarMenu() = eR.menu.menu_bottom
    override fun topLevelDestinations(): Set<Int> {
        val list = HashSet<Int>()
        list.add(eR.id.homeFragment)
        list.add(eR.id.orderFragment)
        list.add(eR.id.profileFragment)
        list.add(eR.id.stockDetailSheetFgragment)
        return list
    }
}