package com.boilerplate

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.facebook.FacebookSdk
import com.boilerplate.util.ResourceProvider
import com.boilerplate.data.db.AppDatabase
import com.boilerplate.data.network.MyApi
import com.boilerplate.data.network.NetworkConnectionInterceptor
import com.boilerplate.data.preferences.PreferenceProvider
import com.boilerplate.data.repositories.UserRepository
import com.boilerplate.ui.auth.AuthViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class MVVMApplication : Application(), KodeinAware {

    private var mResourceProvider: ResourceProvider? = null
    fun getResourceProvider(): ResourceProvider {
        if (mResourceProvider == null)
            mResourceProvider = ResourceProvider(this)
        return mResourceProvider as ResourceProvider
    }

    override fun onCreate() {
        super.onCreate()
        //Comment it if you want to work with Dark Themes
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    override val kodein = Kodein.lazy {
        import(androidXModule(this@MVVMApplication))

        bind() from singleton { NetworkConnectionInterceptor(instance()) }
        bind() from singleton { AppDatabase(instance()) }
        bind() from singleton { PreferenceProvider(instance()) }
        bind() from singleton { MyApi(instance()) }
        bind() from singleton { UserRepository(instance(), instance())}
        bind() from provider { AuthViewModelFactory(instance(),this@MVVMApplication) }




    }

}