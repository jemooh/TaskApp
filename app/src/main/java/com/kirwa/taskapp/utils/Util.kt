package com.kirwa.taskapp.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date

internal class Util {
    companion object {
        fun getWeekDayFromUTC(timeString: Int?): String {
            val sdf = SimpleDateFormat("EEEE")
            val dateFormat: Date = Date(timeString.toString().toLong() * 1000)
            val weekday: String = sdf.format(dateFormat)
            return weekday
        }



        fun getCurrentDayOfTheWeek(): String {
            val sdf = SimpleDateFormat("EEEE")
            val d = Date()
            return sdf.format(d)
        }

        fun ResponseBody.errorMessage(): String {
            val responseBodyString = this.string()
            return try {
                val error = JSONObject(responseBodyString)
                var message = "Unknown Error"
                //val statusMessage = error.getJSONObject("message")
                val name = error.optString("message")
                //if (statusMessage.has("message")) message = statusMessage["message"].toString()
                name
            } catch (e: JSONException) {
                "Internal Server Error, Please try again"
            }
        }



        fun setRecyclerViewNoDivider(context: Context?, recyclerView: RecyclerView) {
            val mLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            recyclerView.layoutManager = mLayoutManager
            recyclerView.itemAnimator = DefaultItemAnimator()
        }




        fun isConnected(context: Context): Boolean {
            var result = false
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val networkCapabilities = connectivityManager.activeNetwork ?: return false
                val actNw =
                    connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
                result = when {
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            } else {
                connectivityManager.run {
                    connectivityManager.activeNetworkInfo?.run {
                        result = oldVersionConnectivity(type)

                    }
                }
            }

            return result

        }

        private fun oldVersionConnectivity(type: Int): Boolean {
            return when (type) {
                ConnectivityManager.TYPE_WIFI -> true
                ConnectivityManager.TYPE_MOBILE -> true
                ConnectivityManager.TYPE_ETHERNET -> true
                else -> false
            }
        }

        fun clearTextInputEditText(
            textInputEditText: EditText,
            textInputLayout: TextInputLayout
        ) {
            textInputEditText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable) { // Do something after Text Change
                }

                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) { // Do something before Text Change
                }

                override fun onTextChanged(
                    s: CharSequence,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                    textInputLayout.setUDAError(null)
                }
            })
        }

        fun TextInputLayout.setUDAError(error: String?) {
            if (error.isNullOrEmpty()) {
                this.isErrorEnabled = false
                this.error = null
            } else {
                this.isErrorEnabled = true
                this.error = error
            }
        }

    }
}
