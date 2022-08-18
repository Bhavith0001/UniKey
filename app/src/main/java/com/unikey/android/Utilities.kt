package com.unikey.android

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.ColorInt
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialSharedAxis

const val TAG = "TAG"



/** Duration of the fragment transaction animations - 300L*/
const val ANIM_DURATION = 300L



/** Color value that is used for notification items that are not viewed - 0x1c0634 */
@ColorInt
const val NOT_VIEWED = -0x1c0634



/** Color value that is used in Calendar for current date - 0x1c0634 */
@ColorInt
const val CURRENT_DATE = -0x97fffd


/** Color value that is used in Calendar for current date - 0x1c0634 */
@ColorInt
const val STUD_RESULT_FAIL = -0x247a77

@ColorInt
const val SELECT_PDF_COLOR = -0xc2c501

/** Sets ths tittle and up-navigation for navigation icon
 * @param fragment [Fragment] in which this [Toolbar] is placed
 * @param title Title for the ToolBar*/
fun Toolbar.setUp(fragment: Fragment, title: String){
    this.title = title
    this.setNavigationOnClickListener {
        fragment.findNavController().navigateUp()
    }
}

// Shared Transition function

fun Fragment.setHostSharedAxisAnim(axis: Int) {
    exitTransition = MaterialSharedAxis(axis, true).apply {
        duration = ANIM_DURATION
    }

    reenterTransition = MaterialSharedAxis(axis, false).apply {
        duration = ANIM_DURATION
    }
}

fun Fragment.setTargetSharedAxisAnim(axis: Int){
    enterTransition = MaterialSharedAxis(axis, true).apply {
        duration = ANIM_DURATION
    }

    returnTransition = MaterialSharedAxis(axis, false).apply {
        duration = ANIM_DURATION
    }
}


// Shared Preferences
const val USharedPref = "Register_no"
const val REG_NO = "REG_NO"
const val DefaultValue = 0L

fun Activity.getUnikeySharedPref(): SharedPreferences {
    return getSharedPreferences(USharedPref, Context.MODE_PRIVATE)
}

