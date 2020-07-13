package com.carrion.edward.androidaccessibility.accessibilitydelegates

import android.view.View
import android.view.accessibility.AccessibilityNodeInfo
import androidx.core.view.AccessibilityDelegateCompat
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat

/**
 * @author : Edward Carrion
 */
class ButtonAccessibilityDelegate(private val label: String) : AccessibilityDelegateCompat() {

    override fun onInitializeAccessibilityNodeInfo(host: View, info: AccessibilityNodeInfoCompat) {
        super.onInitializeAccessibilityNodeInfo(host, info)
        val click = AccessibilityNodeInfoCompat.AccessibilityActionCompat(
            AccessibilityNodeInfo.ACTION_CLICK,
            label
        )

        info.addAction(click)
        info.isEnabled = host.isEnabled
    }
}