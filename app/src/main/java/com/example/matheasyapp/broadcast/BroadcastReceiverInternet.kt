package com.example.matheasyapp.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.net.NetworkInfo

// Receiver để theo dõi thay đổi kết nối mạng
class BroadcastReceiverInternet(private val onNetworkChange: (Boolean) -> Unit) : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        onNetworkChange(isNetworkAvailable(context))
    }
}

// Đăng ký BroadcastReceiver
fun registerNetworkReceiver(context: Context, onNetworkChange: (Boolean) -> Unit): BroadcastReceiverInternet {
    val networkChangeReceiver = BroadcastReceiverInternet(onNetworkChange)
    val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
    context.registerReceiver(networkChangeReceiver, intentFilter)
    return networkChangeReceiver
}

// Hủy đăng ký BroadcastReceiver
fun unregisterNetworkReceiver(context: Context, receiver: BroadcastReceiverInternet) {
    context.unregisterReceiver(receiver)
}

// Kiểm tra kết nối mạng
fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    } else {
        @Suppress("DEPRECATION")
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}
