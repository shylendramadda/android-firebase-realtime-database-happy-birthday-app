package com.geeklabs.happybirthdayy.ui

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.geeklabs.happybirthdayy.R
import com.geeklabs.happybirthdayy.Utils.NetworkUtils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var name = ""
    private var url = "_z-1fTlSDF0"

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setContentView(R.layout.activity_main)

        if (NetworkUtils.isNetworkAvailable(this)) {
            showViews(webView, progress)
            // Write a message to the database
            val database = FirebaseDatabase.getInstance()
            val databaseReference = database.getReference("/")
            // Attach a listener to read the data at our posts reference
            databaseReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    name = dataSnapshot.child("name").value.toString()
                    url = dataSnapshot.child("url").value.toString()
                    println(name)
                    println(url)
                    val frameVideo =
                        "<html><body align=\"center\"><b> Happy happy happiest birth day $name.. :) :) </b><br>" +
                                "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/$url\" frameborder=\"0\" allow=\"accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>" +
                                "</body></html>"
                    webView.loadData(frameVideo, "text/html", "utf-8")
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    println("The read failed: " + databaseError.code)
                    showToast("Failed to get data from server. Error is: " + databaseError.message)
                    val frameVideo =
                        "<html><body align=\"center\"><b> Happy happy happiest birth day $name.. :) :) </b><br>" +
                                "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/$url\" frameborder=\"0\" allow=\"accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>" +
                                "</body></html>"
                    webView.loadData(frameVideo, "text/html", "utf-8")
                }
            })

            webView.webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    showViews(progress)
                    super.onPageStarted(view, url, favicon)
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    hideViews(progress)
                    super.onPageFinished(view, url)
                }

                override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                    return false
                }
            }
            val webSettings = webView.settings
            webSettings.javaScriptEnabled = true

        } else {
            hideViews(webView, progress)
            showViews(birthdayTV)
            showToast("No internet available. Please check your network connection and try again to play video.")
        }
    }

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showViews(vararg views: View) {
        views.forEach {
            it.visibility = View.VISIBLE
        }
    }

    private fun hideViews(vararg views: View) {
        views.forEach {
            it.visibility = View.GONE
        }
    }
}
