package com.example.phonepeassignment.util

import android.content.Context
import android.util.Log
import com.example.phonepeassignment.R
import java.io.InputStream

object Utility {

    fun getTextFromRawFile(context: Context, fileResId: Int) : String? {
        try {
            var result = ""
            val inputStream : InputStream = context.resources.openRawResource(fileResId)
            val buffer = ByteArray(inputStream.available())
            while (inputStream.read(buffer) != -1)
                result += String(buffer)
            return result

        } catch (e: Exception) {
            Log.e(Utility::class.java.simpleName, e.toString())
        }
        return null
    }

}