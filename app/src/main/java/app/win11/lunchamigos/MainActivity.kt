package app.win11.lunchamigos

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.webkit.WebViewClient
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import app.win11.lunchamigos.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var sharedPref : SharedPreferences
    private var consentDialog : AlertDialog.Builder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = getSharedPreferences(DataConfig.appCode, MODE_PRIVATE)

        binding.policyWV.webViewClient = WebViewClient()
        binding.policyWV.loadUrl(DataConfig.policyURL)

        binding.agreeBtn.setOnClickListener {
            consentDialog = AlertDialog.Builder(this@MainActivity)
            consentDialog!!.setTitle("User Data Consent")
            consentDialog!!.setMessage("We may collect your information based on your activities during the usage of the app, to provide better user experience.")
            consentDialog!!.setPositiveButton(
                "Agree"
            ) { dialogInterface: DialogInterface, _: Int ->
                sharedPref.edit().putBoolean("permitSendData", true).apply()
                dialogInterface.dismiss()
            }
            consentDialog!!.setNegativeButton(
                "Disagree"
            ) { dialogInterface: DialogInterface, _: Int ->
                sharedPref.edit().putBoolean("permitSendData", false).apply()
                dialogInterface.dismiss()
            }

            consentDialog!!.setOnDismissListener {
                if (sharedPref.getBoolean("permitSendData",true)) {
                    if (!permissionChecker()){
                        requestPermission()
                    }else openActivity()
                } else openActivity()
            }
            consentDialog!!.show()

        }
        binding.disagreeBtn.setOnClickListener {
            finishAffinity()
        }

    }

    private fun permissionChecker(): Boolean {
        val location = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        val camera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        val media =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
            } else {
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            }

        return location == PackageManager.PERMISSION_GRANTED
                && camera == PackageManager.PERMISSION_GRANTED
                && media == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        val permissions =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_MEDIA_IMAGES
                )
            } else {
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            }

        ActivityCompat.requestPermissions(this, permissions, DataConfig.PERMISSION_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == DataConfig.PERMISSION_REQUEST_CODE) {
            val locationGranted = grantResults.getOrNull(0) == PackageManager.PERMISSION_GRANTED
            val cameraGranted = grantResults.getOrNull(1) == PackageManager.PERMISSION_GRANTED
            val mediaGranted = grantResults.getOrNull(2) == PackageManager.PERMISSION_GRANTED

            sharedPref.edit {
                putBoolean("locationGranted", locationGranted)
                putBoolean("cameraGranted", cameraGranted)
                putBoolean("mediaGranted", mediaGranted)
                putBoolean("runOnce", locationGranted && cameraGranted && mediaGranted)
                apply()
            }
        }

        openActivity()
    }

    private fun openActivity() {
        val gameIntent = Intent(this, GameActivity::class.java)
        gameIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(gameIntent)
        finish()
    }
}