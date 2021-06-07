package com.starkey.engage.Utils

import androidx.navigation.NavDestination
import androidx.navigation.NavGraph

/**
 * Recursively navigate the navigation graph from this destination and test all parent nodes against [predicate].
 *
 * Example:
 * ```
 *     val inOnboardingSubGraph = destination.ancestorGraphsMatch { id == R.id.navgraph_onboarding }
 *     showBottomNavigationView(!inOnboardingSubGraph) // Hide the bottom nav while onboarding
 * ```
 */
public fun NavDestination.ancestorGraphsMatch(predicate: NavGraph.() -> Boolean): Boolean = parent.orItsAncestorMatches(predicate)
internal tailrec fun NavGraph?.orItsAncestorMatches(predicate: NavGraph.() -> Boolean): Boolean = when {
    this == null -> false
    predicate()  -> true
    else         -> parent.orItsAncestorMatches(predicate)
}