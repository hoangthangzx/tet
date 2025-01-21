package com.metalsensor.gold.detector.finder.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.InetSocketAddress
import java.net.Socket

class InternetConnectionChecker(private val context: Context) {
    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val _connectionState = MutableStateFlow<ConnectionState>(ConnectionState.Unknown)
    val connectionState = _connectionState.asStateFlow()
    private var isNetworkCallbackRegistered = false
    private var periodicCheckJob: kotlinx.coroutines.Job? = null
    // Định nghĩa các trạng thái có thể có
    sealed class ConnectionState {
        object Unknown : ConnectionState()
        object NoConnection : ConnectionState()        // Không có kết nối mạng
        object HasConnection : ConnectionState()       // Có kết nối mạng
        object HasInternet : ConnectionState()         // Có thể truy cập internet
        object NoInternet : ConnectionState()          // Có mạng nhưng không truy cập được internet
    }

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            _connectionState.value = ConnectionState.HasConnection
            // Khi có kết nối mạng, kiểm tra khả năng truy cập internet
            checkInternetAccess()
        }

        override fun onLost(network: Network) {
            _connectionState.value = ConnectionState.NoConnection
        }
    }

    init {
        registerNetworkCallback()
    }

    private fun registerNetworkCallback() {
        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager.registerNetworkCallback(request, networkCallback)
    }

    private fun checkInternetAccess() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Thử kết nối đến một số domain đáng tin cậy
                val socket = Socket()
                val socketAddress = InetSocketAddress("8.8.8.8", 53)
                socket.connect(socketAddress, 1000) // timeout 3 giây
                socket.close()

                // Kiểm tra thêm bằng cách ping Google DNS
                val runtime = Runtime.getRuntime()
                val process = runtime.exec("/system/bin/ping -c 1 8.8.8.8")
                val exitValue = process.waitFor()

                if (exitValue == 0) {
                    _connectionState.value = ConnectionState.HasInternet
                } else {
                    _connectionState.value = ConnectionState.NoInternet
                }
            } catch (e: Exception) {
                _connectionState.value = ConnectionState.NoInternet
            }
        }
    }

    // Kiểm tra định kỳ
    fun startPeriodicCheck(interval: Long = 1000L) {
        CoroutineScope(Dispatchers.Default).launch {
            while (true) {
                if (_connectionState.value == ConnectionState.HasConnection) {
                    checkInternetAccess()
                }
                delay(interval)
            }
        }
    }

    fun unregisterNetworkCallback() {
        if (isNetworkCallbackRegistered) {
            connectivityManager.unregisterNetworkCallback(networkCallback)
            isNetworkCallbackRegistered = false
        }
        periodicCheckJob?.cancel()
    }
}