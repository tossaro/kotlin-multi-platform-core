package multi.platform.core.shared.external.extension

import androidx.navigation.NavController

/**
 * Extension for check route id in back stack
 */
@Suppress("UnUsed")
fun NavController.isInBackStack(destinationId: Int) =
    try {
        getBackStackEntry(destinationId)
        true
    } catch (e: Exception) {
        false
    }