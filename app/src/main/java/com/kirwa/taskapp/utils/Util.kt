package com.uda.grassrootelection.utils

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import com.kirwa.taskapp.utils.capitalizeWords
import com.uda.grassrootelection.R
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

internal class Util {
    companion object {
        fun getWeekDayFromUTC(timeString: Int?): String {
            val sdf = SimpleDateFormat("EEEE")
            val dateFormat: Date = Date(timeString.toString().toLong() * 1000)
            val weekday: String = sdf.format(dateFormat)
            return weekday
        }

        fun getMinWeekDayFromUTC(timeString: Int?): String {
            val sdf = SimpleDateFormat("EEE")
            val dateFormat: Date = Date(timeString.toString().toLong() * 1000)
            val weekday: String = sdf.format(dateFormat)
            return weekday
        }

        fun getMinMonthFromUTC(timeString: Int?): String {
            val sdf = SimpleDateFormat("d MMM")
            val dateFormat: Date = Date(timeString.toString().toLong() * 1000)
            val month: String = sdf.format(dateFormat)
            return month
        }

        fun getDateLatestUpdated(timeString: Int?): String {
            val sdf = SimpleDateFormat("hh:mm a")
            val dateFormat: Date = Date(timeString.toString().toLong() * 1000)
            val dateTime: String = sdf.format(dateFormat)
            return dateTime
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


        fun playSoundMistake(context: Context?) {
            val mp: MediaPlayer =
                MediaPlayer.create(context, R.raw.mistake_beep)
            mp.start()
        }

        fun playSoundSuccess(context: Context?) {
            val mp: MediaPlayer =
                MediaPlayer.create(context, R.raw.success_beep)
            mp.start()
        }

        fun playSoundSuccess2(context: Context?) {
            val mp: MediaPlayer =
                MediaPlayer.create(context, R.raw.add_beep)
            mp.start()
        }

        fun playSoundSuccessEndVoting(context: Context?) {
            val mp: MediaPlayer =
                MediaPlayer.create(context, R.raw.end_voting)
            mp.start()
        }


        fun vibrateDevice(context: Context) {
            val v = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            // Vibrate for 500 milliseconds
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                //deprecated in API 26
                v.vibrate(500)
            }
        }

        fun setRecyclerViewNoDivider(context: Context?, recyclerView: RecyclerView) {
            val mLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            recyclerView.layoutManager = mLayoutManager
            recyclerView.itemAnimator = DefaultItemAnimator()
        }

        fun getCurrentDateTimeWithTimeZone(): String? {
            val mSDF = SimpleDateFormat(
                Constants.SIMPLE_DATE_TIME_FORMAT_TIME_ZONE,
                Locale.getDefault()
            )
            return mSDF.format(Date())
        }

        fun getPage(position: String): Int {
            return when (position.trim()) {
                "Religious Group Representative" -> 1
                "MSME Representative" -> 2
                "Professional Group Representative" -> 3
                "Youth Representative" -> 4
                "Farmers Representative" -> 5
                "Member" -> 6
                "Special Interest Groups Rep" -> 7
                else -> 0
            }
        }

        fun displayPlaceHolder(gender: String, context: Context): Drawable? {
            return if (gender == "Male") {
                ContextCompat.getDrawable(context, R.drawable.man)
            } else {
                ContextCompat.getDrawable(context, R.drawable.woman)
            }
        }

        fun removeUnderScores(s: String?): String? {
            val removeUnderScores = s?.replace("_".toRegex(), " ")
            return removeUnderScores?.capitalizeWords()
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


        fun hasPermissions(context: Context?, permissions: List<String?>?): Boolean {
            if (context != null && permissions != null) {
                for (permission in permissions) {
                    if (ActivityCompat.checkSelfPermission(
                            context,
                            permission!!
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        return false
                    }
                }
            }
            return true
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
