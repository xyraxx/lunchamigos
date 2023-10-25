package app.win11.lunchamigos

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.adjust.sdk.Adjust
import com.adjust.sdk.AdjustConfig
import com.adjust.sdk.LogLevel
import com.google.firebase.FirebaseApp
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings

class DataConfig : Application() {

    private var dataConfig = Application()

    companion object {
        const val appCode = "WB677176"
        var apiURL = ""
        var gameURL = ""
        var policyURL = ""
        var PERMISSION_REQUEST_CODE = 201235
        var facebookAppToken = ""
    }

    override fun onCreate() {
        super.onCreate()
        dataConfig = this

        val appToken = "46jck9amdglc"
        val environment = AdjustConfig.ENVIRONMENT_SANDBOX
        val config = AdjustConfig(this, appToken, environment)
        config.setLogLevel(LogLevel.WARN)
        Adjust.onCreate(config)

        registerActivityLifecycleCallbacks(AdjustLifecycleCallbacks())

        FirebaseApp.initializeApp(dataConfig)
        appConfig()
    }

    private class AdjustLifecycleCallbacks : Application.ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

        }

        override fun onActivityStarted(activity: Activity) {

        }

        override fun onActivityResumed(activity: Activity) {
            Adjust.onResume()
        }

        override fun onActivityPaused(activity: Activity) {
            Adjust.onPause()
        }

        override fun onActivityStopped(activity: Activity) {

        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

        }

        override fun onActivityDestroyed(activity: Activity) {

        }
    }

    private fun appConfig() {
        val mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(3600)
            .build()
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings)
    }

    @Synchronized
    private fun getInstance(): DataConfig? {
        return DataConfig() as DataConfig
    }

}