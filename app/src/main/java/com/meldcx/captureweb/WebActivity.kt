package com.meldcx.captureweb

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.meldcx.captureweb.Utils.Utils
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by Lakshmi Srividya on 11/09/2019.
 */

class WebActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var mWebView: WebView
    lateinit var mProgressBar: ProgressBar
    lateinit var mBtnGo: Button
    lateinit var mBtnCapture: Button
    lateinit var mBtnHistory: Button
    lateinit var mInputURL: EditText
    lateinit var mCapturedBitmap: Bitmap
    private var databaseHelper: DatabaseHelper? = null
    var url: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)

        //instance of Database
        databaseHelper = DatabaseHelper(this)

        //Activity views instantiation
        mBtnGo = findViewById(R.id.btnGo)
        mBtnCapture = findViewById(R.id.btnCapture)
        mBtnHistory = findViewById(R.id.btnHistory)
        mInputURL = findViewById(R.id.inputText)
        mBtnGo.setOnClickListener(this)
        mBtnCapture.setOnClickListener(this)
        mBtnHistory.setOnClickListener(this)
        mWebView = findViewById(R.id.webView)

        //Building Webview
        mWebView.webViewClient = MyWebClient()
        mWebView.settings.javaScriptEnabled = true
        mWebView.settings.builtInZoomControls = true
        mWebView.settings.displayZoomControls = false
        mProgressBar = findViewById(R.id.progressBar)
    }

    override fun onResume() {
        super.onResume()
        url=intent.extras?.get("URL").toString()
        if(url!!.isNotEmpty())
        mWebView.loadUrl(url)
    }

    inner class MyWebClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            mProgressBar!!.visibility = View.VISIBLE
            view.loadUrl(url)
            return true
        }

        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            mProgressBar!!.visibility = View.INVISIBLE
            mInputURL.isEnabled = true
        }
    }

    //onClick event handling for each view in an activity
    override fun onClick(view: View) {
        when (view.id) {
            /* onClick of Go Button
             Checks the URL and load it to the Webview*/
            R.id.btnGo -> {
                mInputURL.isEnabled= false
                if (mInputURL.text.isNotEmpty()&& mInputURL.text.contains("http",false)) {
                    Toast.makeText(applicationContext, mInputURL.text, Toast.LENGTH_SHORT).show()
                    url = mInputURL.text.toString()
                    mWebView.loadUrl(url)
                } else {
                    Toast.makeText(applicationContext, R.string.enter_valid_url, Toast.LENGTH_LONG)
                        .show()
                    mInputURL.isEnabled = true
                }
            }
            /* onClick of capture Button
             Saves the bitmap of Webview in an SQLite Database along with URL and Date Time*/
            R.id.btnCapture -> {
                mCapturedBitmap = getPreviewWebview(mWebView)
                Log.d("WebActivity", "Captured Bitmap is" + mCapturedBitmap.toString())
                val imageUrl = mInputURL.text
                val imageDate = getCurrentDateTime()
                val imageBitmap = Utils.getBytes(mCapturedBitmap)
                databaseHelper!!.addImageDetail(
                    imageUrl.toString(),
                    imageDate.toString(),
                    mCapturedBitmap.toString()
                )
            }
            /* onClick of History Button
             Navigates to History Activity*/
            R.id.btnHistory -> {
                val webIntent = Intent(applicationContext, HistoryActivity::class.java)
                startActivity(webIntent)
            }
            else -> {

            }
        }
    }

    //Function returns bitmap of captured screenshot from Webview
    private fun getPreviewWebview(mWebView: WebView): Bitmap {
        val bitmap = Bitmap.createBitmap(mWebView.width, mWebView.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        mWebView.draw(canvas)
        return bitmap
    }

    // Function returns date and time at the time of captured screenshot
    fun getCurrentDateTime(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val currentDate = sdf.format(Date())
        System.out.println(" C DATE is  " + currentDate)
        return currentDate.toString()
    }
}
