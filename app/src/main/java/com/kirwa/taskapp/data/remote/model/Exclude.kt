package com.kirwa.taskapp.data.remote.model

import androidx.annotation.Keep
import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes

/**
 * Class Exclude
 */
@Keep
class Exclude : ExclusionStrategy {

    /**
     * skip classs
     */
    override fun shouldSkipClass(arg0: Class<*>): Boolean {
        return false
    }

    /**
     * Criteria for Skipping a field.
     *
     * @param field The field to be evaluated for skipping.
     * @return True/False - Whether the field should be skipped or not.
     */
    override fun shouldSkipField(field: FieldAttributes): Boolean {
        //Check the field if it has annotation of type @ExcludeSerialize
        val sn = field.getAnnotation(ExcludeSerialize::class.java)

        var skip = sn != null

        if (field.name == "id") {
            skip = true
        }

        return skip
    }
}