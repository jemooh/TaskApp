package com.kirwa.taskapp.utils

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import timber.log.Timber
import java.lang.reflect.Type
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * DateDeserializer
 */
class DateDeserializer : JsonDeserializer<Date> {
    @Throws(JsonParseException::class)
    override fun deserialize(
        jsonElement: JsonElement, typeOF: Type,
        context: JsonDeserializationContext
    ): Date {
        for (format in DATE_FORMATS) {
            val formatter =
                SimpleDateFormat(format, Locale.getDefault())
            formatter.timeZone = TimeZone.getTimeZone("UTC")
            try {
                return formatter.parse(jsonElement.asString)
            } catch (e: ParseException) {
                Timber.e("Failed to parse Date due to:\$e$e")
            }
        }
        throw JsonParseException(
            "Unparseable date: \"" + jsonElement.asString
                    + "\". Supported formats: " + Arrays.toString(DATE_FORMATS)
        )
    }

    companion object {
        private val DATE_FORMATS = arrayOf(
            "yyyy-MM-dd HH:mm:ss",
            "yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ",
            "yyyy-MM-dd'T'HH:mm:ss.SSSSSS",
            "yyyy-MM-dd'T'HH:mm:ss",
            "yyyy-MM-dd'T'HH:mm:ssZ",
            "EEE MMM d HH:mm:ss Z yyyy"
        )
    }
}