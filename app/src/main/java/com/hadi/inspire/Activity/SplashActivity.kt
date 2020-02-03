package com.hadi.inspire.Activity

import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Pair
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import com.hadi.inspire.Components.AppDatabase
import com.hadi.inspire.Components.DatabaseClient
import com.hadi.inspire.R
import com.hadi.inspire.Utils.RevealAnimation
import com.hadi.inspire.Utils.ViewPressEffectHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {


    lateinit var roomDb: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        roomDb = DatabaseClient.getInstance(this).appDatabase
        var fade = AnimationUtils.loadAnimation(this,R.anim.fade_in)


        getSaved()


        this.window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

        //saved.startAnimation(fade)

        btn_go.setOnClickListener {
            startRevealActivity(it)
        }

        ViewPressEffectHelper.attach(saved)
        saved.setOnClickListener{

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                val pairIcon = Pair.create<View,String>(saved,"backTrans")
                val pairList = ArrayList<Pair<View,String>>()
                pairList.add(pairIcon)

                val pairArray = pairList.toTypedArray()
//                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,saved,ViewCompat.getTransitionName(saved) as String)

                val options = ActivityOptions.makeSceneTransitionAnimation(this@SplashActivity,pairIcon)

                val intent = Intent(this,SavedActivity::class.java)
                startActivity(intent,options.toBundle())
            }else{
                val intent = Intent(this,SavedActivity::class.java)
                startActivity(intent)
            }

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

    private fun getSaved() {
//        val compositeDisposable = CompositeDisposable()
//        val disposable =
        roomDb.quoteDao().all.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { t ->
                Log.d("SAVED_LI", "DATA:$t ");
            }
//        compositeDisposable.add(disposable)
//        compositeDisposable.dispose()


    }

}
