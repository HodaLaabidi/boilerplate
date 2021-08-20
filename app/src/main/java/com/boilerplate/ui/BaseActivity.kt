package com.boilerplate.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.boilerplate.util.LocaleHelper
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein

open class BaseActivity : AppCompatActivity(), KodeinAware {
    override val kodein by kodein()
    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase!!))
    }

}