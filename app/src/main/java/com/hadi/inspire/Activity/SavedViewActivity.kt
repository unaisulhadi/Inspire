package com.hadi.inspire.Activity

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.core.content.ContextCompat
import com.hadi.inspire.Components.Quote
import com.hadi.inspire.R
import com.hadi.inspire.Utils.ColorStore
import com.hadi.inspire.Utils.ColorStore.colorList
import kotlinx.android.synthetic.main.activity_saved_view.*
import kotlinx.android.synthetic.main.popup_screen.view.*
import java.io.File
import java.io.FileOutputStream
import java.util.*

class SavedViewActivity : AppCompatActivity() {

    private var sharePath = ""
    internal var dirPath = ""
    internal var saveDir = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_view)
       // overridePendingTransition(0,0)

        init()
        ColorStore()


        bg_quote.setOnClickListener {
            bg_quote.setBackgroundColor(ContextCompat.getColor(applicationContext,colorList.random()))
        }

        back__.setOnClickListener {
            finishAfterTransition()
        }

        btn_share.setOnClickListener {
           takeSS(bg_)
        }
    }

    private fun takeSS(v: View) {
        val now = Date()
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now)

        try {
            // image naming and path  to include sd card  appending name you choose for file
            val mPath = dirPath + "/" + now + ".jpeg"

            // create bitmap screen capture
            v.isDrawingCacheEnabled = true
            val bitmap = Bitmap.createBitmap(v.drawingCache)
            v.isDrawingCacheEnabled = false

            val imageFile = File(mPath)

            val outputStream = FileOutputStream(imageFile)
            val quality = 100
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)




            outputStream.flush()
            outputStream.close()

            //setting screenshot in imageview
            val filePath = imageFile.path

            val ssbitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
            //img_item!!.setImageBitmap(ssbitmap)
            sharePath = filePath;
            //share(sharePath)
            popup_image(bitmap, sharePath)
        } catch (e: Throwable) {
            // Several error may come out with file handling or DOM
            e.printStackTrace()
        }

    }


    private fun init(){

        val quote = intent.getSerializableExtra("QUOTE") as Quote

        quote_text.text = quote.quote;
        by_text.text = quote.author

    }

    fun popup_image(img: Bitmap, sharepath: String) {
        val now = Date()
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now)
        val path = saveDir + "/" + now + ".jpeg"

        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val image_popup = inflater.inflate(R.layout.popup_screen, null)
        val mPopupWindow = PopupWindow(
            image_popup,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        image_popup.img_alert.setImageBitmap(img)


        image_popup.close.setOnClickListener {
            mPopupWindow.dismiss()
        }

        image_popup.pop_bg.setOnClickListener {
            mPopupWindow.dismiss()
        }

        image_popup.wa.setOnClickListener {
            val file = File(sharePath)
            val uri = Uri.fromFile(file)
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "image/*"
                putExtra(Intent.EXTRA_STREAM, uri)
                `package` = "com.whatsapp"
            }
            startActivity(Intent.createChooser(intent, "Share Quote.."))
        }

        image_popup.insta.setOnClickListener {
            val file = File(sharePath)
            val uri = Uri.fromFile(file)
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "image/*"
                putExtra(Intent.EXTRA_STREAM, uri)
                `package` = "com.instagram.android"
            }
            startActivity(Intent.createChooser(intent, "Share Quote.."))
        }

        image_popup.dwnld.setOnClickListener {

            share(sharePath)

        }


        if (Build.VERSION.SDK_INT >= 21) {
            mPopupWindow.elevation = 5.0f
        }

        mPopupWindow.isFocusable = true
        mPopupWindow.animationStyle = R.style.popupAnimation
        mPopupWindow.showAtLocation(bg_, Gravity.CENTER, 0, 0)
    }

    private fun share(sharePath: String) {

        val file = File(sharePath)
        val uri = Uri.fromFile(file)
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_STREAM, uri)
        }
        startActivity(Intent.createChooser(intent, "Share Quote.."))
    }
}
