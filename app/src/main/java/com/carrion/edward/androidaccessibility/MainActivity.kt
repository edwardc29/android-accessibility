package com.carrion.edward.androidaccessibility

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import com.carrion.edward.androidaccessibility.accessibilitydelegates.ButtonAccessibilityDelegate
import com.carrion.edward.androidaccessibility.base.BaseActivity
import com.carrion.edward.androidaccessibility.util.Util
import kotlinx.android.synthetic.main.activity_main.*


/**
 * @author : Edward Carrion
 */
class MainActivity : BaseActivity() {
    override fun getLayout() =
        R.layout.activity_main

    override fun showHomeAsUp() = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        parentConstraintLayout.requestFocus()

        initView()

        initAccessibility()
    }

    private fun initView() {
        rememberPasswordBcpCheckBox.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                Util.hideKeyboard(this)
            }
        }

        acceptBcpButtonRectangle.setOnClickListener {
            Util.hideKeyboard(this)

            var userNameSuccess = false
            var passwordSuccess = false

            userNameTextInputLayout.editText?.text?.toString().let {
                userNameTextInputLayout.error = if (it.isNullOrEmpty()) {
                    getString(R.string.user_name_is_required)
                } else {
                    userNameSuccess = true
                    null
                }
            }

            passwordTextInputLayout.editText?.text?.toString().let {
                passwordTextInputLayout.error = if (it.isNullOrEmpty()) {
                    getString(R.string.password_is_required)
                } else {
                    passwordSuccess = true
                    null
                }
            }

            if (userNameSuccess && passwordSuccess) {
                userNameTextInputLayout.editText?.text?.clear()
                passwordTextInputLayout.editText?.text?.clear()
                parentConstraintLayout.requestFocus()

                startActivity(Intent(this, SecondActivity::class.java))
            }
        }

        rememberPasswordBcpCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                setAccessibilityDelegate(
                    rememberPasswordBcpCheckBox,
                    getString(R.string.accessibility_not_trust_this_device)
                )
            } else {
                setAccessibilityDelegate(
                    rememberPasswordBcpCheckBox,
                    getString(R.string.trust_this_device)
                )
            }
        }
    }

    private fun initAccessibility() {
        setAccessibilityDelegate(rememberPasswordBcpCheckBox, getString(R.string.trust_this_device))
        setAccessibilityDelegate(
            acceptBcpButtonRectangle,
            getString(R.string.accessibility_validate_user_name_and_password)
        )
        setAccessibilityDelegate(locateUsTextView, getString(R.string.accessibility_locate_offices))
        setAccessibilityDelegate(callUsTextView, getString(R.string.accessibility_call))
    }

    private fun setAccessibilityDelegate(view: View, label: String) {
        ViewCompat.setAccessibilityDelegate(view, ButtonAccessibilityDelegate(label))
    }
}