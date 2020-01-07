package com.hadi.inspire.Activity

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.hadi.inspire.Adapter.LinePagerIndicatorDecoration
import com.hadi.inspire.Adapter.QuoteAdapter
import com.hadi.inspire.Model.QuoteModel
import com.hadi.inspire.Model.ResultsItem
import com.hadi.inspire.Network.RetrofitClient
import com.hadi.inspire.R
import com.hadi.inspire.Utils.RevealAnimation
import com.hadi.inspire.Utils.ViewPressEffectHelper
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.util.*


class MainActivity : AppCompatActivity() {

    var list = arrayListOf<ResultsItem>()
    lateinit var background: View
    lateinit var mRevealAnimation: RevealAnimation
    val colorList = arrayListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        background = findViewById(R.id.bg_)
        val intent =
            this.intent //get the intent to receive the x and y coords, that you passed before
        val rootLayout = findViewById<RelativeLayout>(R.id.bg_) //there you have to get the root layout of your second activity
        mRevealAnimation = RevealAnimation(rootLayout, intent, this)



        rv_quote.setHasFixedSize(true)
        rv_quote.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(rv_quote)

        //rv_quote.addItemDecoration(LinePagerIndicatorDecoration())

        //progress.progress = 0F
        //progress.playAnimation()

        getQuotes()

        ViewPressEffectHelper.attach(btn_save)
        btn_save.setOnClickListener {
            Toast.makeText(this, "Save", Toast.LENGTH_SHORT).show();
        }

        ViewPressEffectHelper.attach(btn_share)
        btn_share.setOnClickListener {
            //Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
            requestReadPermissions()
        }

    }


    private fun getQuotes() {
//        progress.visibility = View.VISIBLE
        val apiInterface = RetrofitClient.getInstance(this)
        val call = apiInterface.quote.getQuote

        call.enqueue(object : Callback<QuoteModel?>, QuoteAdapter.OnItemClickListener {

            override fun onResponse(call: Call<QuoteModel?>, response: Response<QuoteModel?>) {

//                progress.visibility = View.GONE

                if (response.isSuccessful) {

                    //Log.d("RESSSSSSS", response.body().toString())

                    val data = response.body()
                    val list = data?.results as ArrayList
                    list.shuffle()

                    val quoteAdapter = QuoteAdapter(this@MainActivity, list, this)
                    rv_quote.adapter = quoteAdapter

                } else {
                    Toast.makeText(this@MainActivity,response.errorBody().toString(),Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<QuoteModel?>, t: Throwable) {
                Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onItemClick(item: ResultsItem) {

            }
        })
    }

    private fun requestReadPermissions() {

        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) { // check if all permissions are granted
                    if (report.areAllPermissionsGranted()) {
                        Toast.makeText(
                            applicationContext,
                            "All permissions are granted!",
                            Toast.LENGTH_SHORT
                        ).show()


                    }
                    // check for permanent denial of any permission
                    if (report.isAnyPermissionPermanentlyDenied) { // show alert dialog navigating to Settings
                        showSettingsDialog()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            }).withErrorListener {
                Toast.makeText(applicationContext, "Error occurred! ", Toast.LENGTH_SHORT).show()
            }
            .onSameThread()
            .check()
    }


    private fun showSettingsDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Need Permissions")
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.")
        builder.setPositiveButton("GOTO SETTINGS",
            DialogInterface.OnClickListener { dialog, which ->
                dialog.cancel()
                openSettings()
            })
        builder.setNegativeButton("Cancel",
            DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
        builder.show()
    }

    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivityForResult(intent, 101)
    }

    private fun takeSS(v: View?) {
        val now = Date()
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now)

        try {
            // image naming and path  to include sd card  appending name you choose for file
            val mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpeg"

            // create bitmap screen capture
            v!!.isDrawingCacheEnabled = true
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

//            val ssbitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
//            ivpl!!.setImageBitmap(ssbitmap)
            //sharePath = filePath;

            share(filePath)

        } catch (e: Throwable) {
            // Several error may come out with file handling or DOM
            e.printStackTrace()
        }

    }

    private fun share(sharePath: String) {

        val file = File(sharePath)
        val uri = Uri.fromFile(file)
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        startActivity(intent)

    }


    override fun onBackPressed() {
         mRevealAnimation.unRevealActivity()
        //finish()
    }

    fun closeChoice(view: View) {
        mRevealAnimation.unRevealActivity()
    }
}
