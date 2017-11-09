package io.rocketico.signapp

import android.app.Application
import devliving.online.securedpreferencestore.DefaultRecoveryHandler
import devliving.online.securedpreferencestore.SecuredPreferenceStore

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        SecuredPreferenceStore.init(applicationContext, DefaultRecoveryHandler())
    }
}