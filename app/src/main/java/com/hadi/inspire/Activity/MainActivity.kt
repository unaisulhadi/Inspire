package com.hadi.inspire.Activity

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.provider.Settings
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
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
import kotlinx.android.synthetic.main.popup_screen.view.*
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
    lateinit var adapter: QuoteAdapter
    var rv_pos = 0;
    private var sharePath = "no"
    internal var dirPath = ""
    internal var saveDir=""
    lateinit var file_loc: File
    lateinit var file_saved:File


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val builder = StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure()

        this.window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        background = findViewById(R.id.bg_)
        val intent =
            this.intent //get the intent to receive the x and y coords, that you passed before
        val rootLayout =
            findViewById<RelativeLayout>(R.id.bg_) //there you have to get the root layout of your second activity
        mRevealAnimation = RevealAnimation(rootLayout, intent, this)


        btn_save.isEnabled = false
        btn_share.isEnabled = false


        file_loc = File(
            "" +
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/"
                    + "Inspire"
        );



        //FileUtils.deleteDirectory(file_loc)


        if (!file_loc.exists()) {
            file_loc.mkdir()
        } else {

//            file_saved =  File(
//                "" +
//                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/"
//                        + "Inspire/InspireCollections"
//            );
//            if (!file_saved.exists()) {
//                file_saved.mkdir()
//            }
            var files = file_loc.listFiles()
            for (i in 0..files.lastIndex) {
                files.get(i).delete()
            }

        }

        dirPath = file_loc.absolutePath
//        saveDir = file_saved.absolutePath




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
            requestReadPermissions()
        }

        rv_quote.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                var offset = rv_quote.computeHorizontalScrollOffset()
                var cellwidth = rv_quote.getChildAt(0).measuredWidth
                if (offset % cellwidth == 0) {
                    rv_pos = offset / cellwidth
                }
                //Toast.makeText(this@MainActivity, "$rv_pos", Toast.LENGTH_SHORT).show();
            }
        })
    }


    private fun getQuotes() {
//        progress.visibility = View.VISIBLE
        val apiInterface = RetrofitClient.getInstance(this)
        val call = apiInterface.quote.getQuote

        call.enqueue(object : Callback<QuoteModel?>, QuoteAdapter.OnItemClickListener {

            override fun onResponse(call: Call<QuoteModel?>, response: Response<QuoteModel?>) {

//                progress.visibility = View.GONE

                if (response.isSuccessful) {


                    btn_save.isEnabled = true
                    btn_share.isEnabled = true

                    //Log.d("RESSSSSSS", response.body().toString())

                    val data = response.body()
                    val list = data?.results as ArrayList
                    //list.shuffle()

                    adapter = QuoteAdapter(this@MainActivity, list, this)
                    rv_quote.adapter = adapter

                } else {
                    Toast.makeText(
                        this@MainActivity,
                        response.errorBody().toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<QuoteModel?>, t: Throwable) {
                Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_SHORT).show()

                btn_save.isEnabled = false
                btn_share.isEnabled = false
            }

            override fun onItemClick(item: ResultsItem) {

            }
        })
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

    private fun requestReadPermissions() {

        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) { // check if all permissions are granted
                    if (report.areAllPermissionsGranted()) {

                        takeSS(rv_quote.findViewHolderForAdapterPosition(rv_pos)!!.itemView)

                    }
                    // check for permanent denial of any permission
                    if (report.isAnyPermissionPermanentlyDenied) { // show alert dialog navigating to Settings
                        //showSettingsDialog()
                        Toast.makeText(
                            this@MainActivity,
                            "Storage Permission Required.!",
                            Toast.LENGTH_SHORT
                        ).show();
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
            img_item!!.setImageBitmap(ssbitmap)
            sharePath = filePath;
            //share(sharePath)
            popup_image(bitmap,sharePath)
        } catch (e: Throwable) {
            // Several error may come out with file handling or DOM
            e.printStackTrace()
        }

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


    override fun onBackPressed() {
        mRevealAnimation.unRevealActivity()
        //finish()
    }

    fun closeChoice(view: View) {
        mRevealAnimation.unRevealActivity()
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
}
