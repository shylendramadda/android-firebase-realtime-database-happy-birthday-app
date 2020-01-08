package com.geeklabs.happybirthdayy.Utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

object NetworkUtils {

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
        if (connectivityManager is ConnectivityManager) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val activeNetwork = connectivityManager.activeNetwork
                if (activeNetwork != null) {
                    val nc = connectivityManager.getNetworkCapabilities(activeNetwork)
                    return (nc != null && nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || nc!!.hasTransport(
                        NetworkCapabilities.TRANSPORT_WIFI
                    ))
                }
                return false
            }
        }
        return false
    }
}