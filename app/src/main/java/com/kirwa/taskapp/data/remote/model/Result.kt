package com.kirwa.taskapp.data.remote.model

import androidx.annotation.Keep
import okhttp3.ResponseBody

/**
 * Sealed class for networking and UI states
 */
@Keep
sealed class Result<out T : Any> {
    object Loading : Result<Nothing>()
    data class Success<out T : Any>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}
