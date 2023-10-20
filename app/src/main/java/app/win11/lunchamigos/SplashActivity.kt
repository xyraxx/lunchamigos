package app.win11.lunchamigos

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.ImageView
import app.win11.lunchamigos.databinding.ActivitySplashBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.remoteconfig.FirebaseRemoteConfig

class SplashActivity : AppCompatActivity() {


    private lateinit var binding : ActivitySplashBinding

    private lateinit var img : ImageView

    private lateinit var sharedPref : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        img = binding.logoImg

        val credAnim = AnimationUtils.loadAnimation(this, R.anim.cred_anim)
        img.startAnimation(credAnim)

        sharedPref = getSharedPreferences(DataConfig.appCode, MODE_PRIVATE)

        val mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        mFirebaseRemoteConfig.fetchAndActivate()
            .addOnCompleteListener { task: Task<Boolean?> ->
                if (task.isSuccessful) {
                    Log.d("Firebase Remote Config", "Connected")
                    DataConfig.apiURL = mFirebaseRemoteConfig.getString("apiURL")
                    DataConfig.policyURL = mFirebaseRemoteConfig.getString("policyURL")
                    DataConfig.facebookAppToken = mFirebaseRemoteConfig.getString("facebookAppId")
                    DataConfig.gameURL = DataConfig.apiURL+"?appid="+DataConfig.appCode
                    Log.d("apiURL", DataConfig.apiURL)
                    Log.d("policyURL", DataConfig.policyURL)



                    val handler = Handler()
                    handler.postDelayed({
                        val policyIntent = Intent(this, MainActivity::class.java)
                        policyIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(policyIntent)
                        finish()
                    },1500)

                }
            }

    }
}