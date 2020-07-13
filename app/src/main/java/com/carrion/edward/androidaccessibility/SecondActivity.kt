package com.carrion.edward.androidaccessibility

import android.os.Bundle
import androidx.core.content.ContextCompat
import com.carrion.edward.androidaccessibility.base.BaseActivity
import kotlinx.android.synthetic.main.activity_second.*

/**
 * @author : Edward Carrion
 */
class SecondActivity : BaseActivity() {
    override fun getLayout() =
        R.layout.activity_second

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        downloadConstraintLayout.contentDescription =
            getString(R.string.accessibility_number_download, 100)
        starsConstraintLayout.contentDescription =
            getString(R.string.accessibility_description_stars, 4.4, 1000)

        optionsRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (optionsRadioGroup.indexOfChild(findViewById(checkedId))) {
                0 -> {
                    resultTextView.setText(R.string.correct)
                    resultTextView.setBackgroundColor(
                        ContextCompat.getColor(this, R.color.colorAccent)
                    )
                }
                else -> {
                    resultTextView.setText(R.string.incorrect)
                    resultTextView.setBackgroundColor(
                        ContextCompat.getColor(this, R.color.colorPrimaryDark)
                    )
                }
            }
        }
    }
}