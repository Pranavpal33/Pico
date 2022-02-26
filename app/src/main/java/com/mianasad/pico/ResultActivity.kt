package com.mianasad.pico

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.InterstitialAd
import android.os.Bundle
import com.mianasad.pico.databinding.ActivityResultBinding
import kotlinx.android.synthetic.main.activity_result.*

class ResultActivity : AppCompatActivity() {
    var binding: ActivityResultBinding? = null
    private var mInterstitialAd: InterstitialAd? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        img.setImageURI(intent.data)
        home_btn.setOnClickListener{
            val intent= Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

    }


}