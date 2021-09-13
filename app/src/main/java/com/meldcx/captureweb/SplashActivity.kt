package com.meldcx.captureweb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import java.util.*
import kotlin.concurrent.schedule

/**
 * Created by Lakshmi Srividya on 11/09/2019.
 *
 * Activity show the Splash Screen to the user
 */
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.requestFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setContentView(R.layout.activity_splash)

        Timer().schedule(3000) {
            val webIntent = Intent(applicationContext, WebActivity::class.java)
            startActivity(webIntent)
            finish()
        }
    }
}
