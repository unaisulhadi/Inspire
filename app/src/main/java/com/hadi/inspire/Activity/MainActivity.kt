package com.hadi.inspire.Activity

import android.Manifest
import android.annotation.SuppressLint
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
import android.util.Log
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
import androidx.viewpager2.widget.ViewPager2
import com.hadi.inspire.Adapter.QuoteAdapter
import com.hadi.inspire.Components.AppDatabase
import com.hadi.inspire.Components.DatabaseClient
import com.hadi.inspire.Components.Quote
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
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.popup_screen.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.util.*

class MainActivity : AppCompatActivity(), QuoteAdapter.OnItemClickListener {

    var list = arrayListOf<ResultsItem>()
    var quote_list = mutableListOf<Quote>()
    var saved_list = mutableListOf<Quote>()
    lateinit var background: View
    lateinit var mRevealAnimation: RevealAnimation
    val colorList = arrayListOf<Int>()
    lateinit var adapter: QuoteAdapter
    var rv_pos = 0;
    private var sharePath = "no"
    internal var dirPath = ""
    internal var saveDir = ""
    lateinit var file_loc: File
    lateinit var file_saved: File
    lateinit var roomDb: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()





        ViewPressEffectHelper.attach(btn_save)
        btn_save.setOnClickListener {



            var id_list = arrayListOf<String>()
            for(i in 0..saved_list.lastIndex){
                id_list.add(saved_list[i].id)
            }


            if(saved_list.isEmpty()){

                val quote = Quote()

                quote.id = list[rv_pos].id
                quote.author = list[rv_pos].quoteAuthor
                quote.quote = list[rv_pos].quoteText

                Completable.fromAction {
                    roomDb.quoteDao().insert(quote)
                }.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        //Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
                        btn_save.setImageResource(R.drawable.ic_heart_fill)
                    }

            }
            else{

                val quote = Quote()

                quote.id = list[rv_pos].id
                quote.author = list[rv_pos].quoteAuthor
                quote.quote = list[rv_pos].quoteText
                quote.position = rv_pos


                for( i in 0..saved_list.lastIndex){

                    if(saved_list[i].id == list[rv_pos].id){

                        Completable.fromAction {
                            roomDb.quoteDao().delete(saved_list.get(i))
                        }.subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe {
                                //Toast.makeText(this, "Removed", Toast.LENGTH_SHORT).show();
                                btn_save.setImageResource(R.drawable.ic_heart)
                            }

                    }else{

                        Completable.fromAction {
                            roomDb.quoteDao().insert(quote)
                        }.subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe {
                                //Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
                                btn_save.setImageResource(R.drawable.ic_heart_fill)
                            }

                    }

                }

            }


        }

        ViewPressEffectHelper.attach(btn_share)
        btn_share.setOnClickListener {

            //takeSS(rv_quote.findViewHolderForAdapterPosition(rv_pos)!!.itemView)
            takeSS(vp)
        }

        //vp.orientation = ViewPager2.ORIENTATION_VERTICAL

        vp.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                rv_pos = position;

                var id_list = arrayListOf<String>()

                for(i in 0..saved_list.lastIndex){
                    id_list.add(saved_list[i].id)
                }

                if(id_list.contains(list[position].id)){
                   // Toast.makeText(this@MainActivity,"TRUE",Toast.LENGTH_SHORT).show()
                    btn_save.setImageResource(R.drawable.ic_heart_fill)
                }else{
                    //Toast.makeText(this@MainActivity,"FALSE",Toast.LENGTH_SHORT).show()
                    btn_save.setImageResource(R.drawable.ic_heart)
                }
            }
        })

    }

    fun init(){
        val builder = StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure()

        roomDb = DatabaseClient.getInstance(this).appDatabase

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

        requestReadPermissions()


//        if(getIntent()!=null){
//            val quote = intent.getSerializableExtra("QUOTE") as Quote
//
//            val pos = quote.position
//            val id = quote.id
//            val auth = quote.author
//            val text = quote.quote
//            val isSelected = true
//
//            val res = ResultsItem(text,auth,id)
//            var _list = arrayListOf<ResultsItem>()
//            _list.add(res)
//
//            val adapter = QuoteAdapter(this,list,this)
//            vp.adapter = adapter
//
//
//
//            val poss = intent.getIntExtra("QUOTE_POS",0)
//            vp.currentItem = poss
//
//
//        }else{
            getSaved()
            getQuotes()
//        }
    }

    @SuppressLint("CheckResult")
    private fun getSaved() {

            roomDb.quoteDao().all.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { t ->
                Log.d("SAVED_LI", ":$t ");
                saved_list = t
            }

    }



    private fun getQuotes() {
        lottie.playAnimation()

        val apiInterface = RetrofitClient.getInstance(this)
        val call = apiInterface.quote.getQuote

        call.enqueue(object : Callback<QuoteModel?>, QuoteAdapter.OnItemClickListener {

            override fun onResponse(call: Call<QuoteModel?>, response: Response<QuoteModel?>) {

//                progress.visibility = View.GONE

                lottie.cancelAnimation()
                lottie.visibility = View.GONE

                if (response.isSuccessful) {


                    btn_save.isEnabled = true
                    btn_share.isEnabled = true

                    //Log.d("RESSSSSSS", response.body().toString())

                    val data = response.body()
                    list = data?.results as ArrayList
                    //list.shuffle()

                    adapter = QuoteAdapter(this@MainActivity, list, this)
                    vp.adapter = adapter


//                    for( i in 0..list.lastIndex){
//                        quote_list.add(Quote(list.get(i).id,list[i].quoteText,list[i].quoteAuthor))
//                    }

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
                lottie.cancelAnimation()

                lottie.visibility = View.GONE
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

                        file_loc = File(
                            "" +
                                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/"
                                    + "Inspire"
                        );


                        //FileUtils.deleteDirectory(file_loc)


                        if (!file_loc.exists()) {
                            file_loc.mkdir()
                        } else {

                            var files = file_loc.listFiles()
                            if(files.isNotEmpty()) {
                                for (i in 0..files.lastIndex) {
                                    files.get(i).delete()
                                }
                            }

                        }

                        dirPath = file_loc.absolutePath



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
            sharePath = filePath;
            //share(sharePath)
            popup_image(bitmap, sharePath)
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

    override fun onItemClick(item: ResultsItem) {

    }
}
