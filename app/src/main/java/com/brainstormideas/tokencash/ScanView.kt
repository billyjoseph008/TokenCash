package com.brainstormideas.tokencash

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.brainstormideas.tokencash.utils.*
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.zxing.BarcodeFormat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

class ScanView : AppCompatActivity() {

    private lateinit var back_scan_ibtn: ImageButton
    private lateinit var ligth_on_fab: FloatingActionButton
    private lateinit var input_code_etx: EditText
    private lateinit var getApi_btn: Button
    private lateinit var copy_btn: Button
    private var cameraSource: CameraSource? = null
    private var cameraView: SurfaceView? = null
    private val MY_PERMISSIONS_REQUEST_CAMERA = 1
    private var token = ""
    private var tokenanterior = ""
    var sessionManager: SessionManager? = null
    var tokenRecibido = ""
    var BaseUrl = "https://desarrollo.app.tokencash.mx"

    private lateinit var mAuth: FirebaseAuth
    private lateinit var databaseReferenceRegistros: DatabaseReference
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_view)
        getSupportActionBar()?.hide()

        initComponents()
        initQR()
    }

    fun initComponents() {

        sessionManager = SessionManager()
        sessionManager!!.SessionManager(getApplicationContext())
        back_scan_ibtn = findViewById(R.id.back_scan_ibtn)
        ligth_on_fab = findViewById(R.id.ligth_on_fab)
        input_code_etx = findViewById(R.id.input_code_etx)
        getApi_btn = findViewById(R.id.getApi_btn)
        copy_btn = findViewById(R.id.copy_btn)
        cameraView = findViewById(R.id.scan_view)

        database = FirebaseDatabase.getInstance()
        mAuth = FirebaseAuth.getInstance()
        databaseReferenceRegistros = database.reference.child("Registros")


        back_scan_ibtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                goToMain()
            }

        })

        getApi_btn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                getApiInfo()
            }

        })

        copy_btn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                copyToken()
            }

        })


    }

    internal fun getApiInfo() {

        val retrofit = Retrofit.Builder()
            .baseUrl(BaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(ApiService::class.java)
        val call = service.getToken()
        call.enqueue(object : Callback<Token> {
            override fun onResponse(call: Call<Token>, response: Response<Token>) {
                tokenRecibido = response.body()?.token.toString().substring(40, 44)
                input_code_etx.setText(tokenRecibido)
            }

            override fun onFailure(call: Call<Token>, t: Throwable) {
                Toast.makeText(applicationContext, t.toString(), Toast.LENGTH_LONG).show()
            }
        })

    }

    fun initQR() {

        val barcodeDetector = BarcodeDetector.Builder(this)
            .setBarcodeFormats(Barcode.ALL_FORMATS)
            .build()

        cameraSource = CameraSource.Builder(this, barcodeDetector)
            .setRequestedPreviewSize(1600, 1024)
            .setAutoFocusEnabled(true)
            .build()

        cameraView!!.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {

                if (ActivityCompat.checkSelfPermission(
                        this@ScanView,
                        Manifest.permission.CAMERA
                    ) != PackageManager.PERMISSION_GRANTED
                ) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(
                                Manifest.permission.CAMERA
                            )
                        )

                            requestPermissions(
                                arrayOf(Manifest.permission.CAMERA),
                                MY_PERMISSIONS_REQUEST_CAMERA
                            )
                    }

                    return

                } else {
                    try {
                        cameraSource!!.start(cameraView!!.holder)
                    } catch (ie: IOException) {
                        Log.e("CAMERA SOURCE", ie.message)
                    }

                }
            }

            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                cameraSource!!.stop()
            }
        })

        barcodeDetector.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {}

            @RequiresApi(Build.VERSION_CODES.O)
            override fun receiveDetections(detections: Detector.Detections<Barcode>) {
                val barcodes = detections.getDetectedItems()

                if (barcodes.size() > 0) {

                    token = barcodes.valueAt(0).displayValue.toString()

                    if (token != tokenanterior) {

                        tokenanterior = token
                        Log.i("token", token)

                        if (token.equals(tokenRecibido)) {

                            val currentTime = LocalDateTime.now()
                            var tokenActual = sessionManager!!.getToken()!!.toInt()
                            tokenActual++
                            sessionManager?.setToken(tokenActual.toString())

                            databaseReferenceRegistros.push().
                            setValue(tokenActual.toString()+" totales agregados. Fecha: "+ currentTime)

                            val i = Intent(applicationContext, MainActivity::class.java)
                            startActivity(i)


                        } else {

                        }
                        Thread(object : Runnable {
                            override fun run() {
                                try {
                                    synchronized(this) {
                                        Thread.sleep(5000)
                                        tokenanterior = ""
                                    }
                                } catch (e: InterruptedException) {
                                    // TODO Auto-generated catch block
                                    Log.e("Error", "Algo estuvo mal.")
                                    e.printStackTrace()
                                }
                            }
                        }).start()

                    }
                }
            }
        })
    }

    fun goToMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    fun copyToken() {

        if (!input_code_etx.text.equals("") && input_code_etx.text != null) {

            var qr_data = input_code_etx.text.toString()

            val text: Editable? = input_code_etx.text
            val clipboard: ClipboardManager =
                getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip: ClipData = ClipData.newPlainText("text", text)
            clipboard.setPrimaryClip(clip)

        }
    }

    /*
    fun QRGenerator(qrData: String) {

        val qrCodeDimention = 600
        val qrCodeEncoder = QRCodeEncoder(
            qrData, null,
            Contents.Type.TEXT, BarcodeFormat.QR_CODE.toString(), qrCodeDimention
        )
        var bitmap: Bitmap? = qrCodeEncoder.encodeAsBitmap()

        val date: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        try {
            val file_dir =
                Environment.getDataDirectory().absolutePath.toString() + "/" + packageName.toString() + "/qr_tokens"
            val file = File(file_dir, "QRtoken" + date + ".png")
            var fOut: FileOutputStream? = null
            fOut = FileOutputStream(file)
            bitmap!!.compress(Bitmap.CompressFormat.PNG, 100, fOut)
            fOut!!.flush()
            fOut!!.close()
            file.setReadable(true, false)
            val intent = Intent(Intent.ACTION_SEND)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file))
            intent.type = "image/png"
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }*/


    private fun <T> Call<T>?.enqueue(callback: Callback<Token>) {}
}