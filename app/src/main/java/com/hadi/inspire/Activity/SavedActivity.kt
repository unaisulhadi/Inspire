package com.hadi.inspire.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hadi.inspire.R
import kotlinx.android.synthetic.main.activity_saved.*

class SavedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved)

        back_saved.setOnClickListener {
            finishAfterTransition()
        }


    }
}
