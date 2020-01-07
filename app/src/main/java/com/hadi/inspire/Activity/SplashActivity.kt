package com.hadi.inspire.Activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.hadi.inspire.R
import com.hadi.inspire.Utils.RevealAnimation
import com.hadi.inspire.Utils.ViewPressEffectHelper
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        this.window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

        btn_go.setOnClickListener {
            startRevealActivity(it)
        }

        ViewPressEffectHelper.attach(saved)
        saved.setOnClickListener{
            Toast.makeText(this, "TETT", Toast.LENGTH_SHORT).show();
        }
    }

    private fun startRevealActivity(v:View){

        //calculates the center of the View v you are passing
        val revealX = (v.x + v.width / 2).toInt()
        val revealY = (v.y + v.height / 2).toInt()

        //create an intent, that launches the second activity and pass the x and y coordinates
        //create an intent, that launches the second activity and pass the x and y coordinates
        val intent = Intent(this@SplashActivity, MainActivity::class.java)
        intent.putExtra(RevealAnimation.EXTRA_CIRCULAR_REVEAL_X, revealX)
        intent.putExtra(RevealAnimation.EXTRA_CIRCULAR_REVEAL_Y, revealY)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //just start the activity as an shared transition, but set the options bundle to null
        //just start the activity as an shared transition, but set the options bundle to null
        ActivityCompat.startActivity(this, intent, null)
        //startActivity(intent)
        //to prevent strange behaviours override the pending transitions
        //to prevent strange behaviours override the pending transitions
        overridePendingTransition(0, 0)
    }

}
