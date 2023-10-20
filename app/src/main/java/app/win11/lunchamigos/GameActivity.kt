package app.win11.lunchamigos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import app.win11.lunchamigos.databinding.ActivityGameBinding
import com.adjust.sdk.webbridge.AdjustBridge

class GameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AdjustBridge.registerAndGetInstance(getApplication(), binding.gameContent);

        try {
            binding.gameContent.loadUrl(DataConfig.gameURL)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}