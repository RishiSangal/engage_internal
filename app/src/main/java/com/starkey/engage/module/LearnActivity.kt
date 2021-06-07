package com.starkey.engage.module

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.starkey.engage.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LearnActivity : AppCompatActivity() {

//    @Inject
//    internal lateinit var navDispatcher: NavDispatcher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lear)

//        val fragmentManager = supportFragmentManager

//        Utility.addOrReplaceFragment(
//            fragmentManager, R.id.fragmentContainer, LearnFragment.newInstance(),
//            LearnFragment.TAG_NAME, false, isAddBackStack = true
//        )

//        with(ActivityLearBinding.inflate(layoutInflater)) {
//            val navController = findNavController(R.id.engage_nav_host_fragment)
//            navController.addOnDestinationChangedListener { controller: NavController, destination: NavDestination, bundle: Bundle? ->
//
//                val inOnboardingSubGraph = destination.ancestorGraphsMatch {
//                    id in setOf(
//                        R.id.nav_to_learn,
//                    )
//                }
//            }
//            observe(navDispatcher.events) { navController.it(this@LearnActivity) }
//        }
    }

//    fun openVideoDetail(topicModel: SearchResultModel) {
//
//        startActivity(Intent(this, VideoActivity::class.java)
//            .putExtras(VideoActivity.getBundle(topicModel?.uniqueID.clean())))
//    }
}