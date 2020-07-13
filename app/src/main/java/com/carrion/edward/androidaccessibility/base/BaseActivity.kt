package com.carrion.edward.androidaccessibility.base

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.carrion.edward.androidaccessibility.R

/**
 * @author : Edward Carrion
 */
abstract class BaseActivity : AppCompatActivity() {

    abstract fun getLayout(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayout())

        setSupportActionBar(findViewById(R.id.toolbar))

        supportActionBar?.setDisplayHomeAsUpEnabled(showHomeAsUp())
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            super.onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    open fun showHomeAsUp(): Boolean = true
}