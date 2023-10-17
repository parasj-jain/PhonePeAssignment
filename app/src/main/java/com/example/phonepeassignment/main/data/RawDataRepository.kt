package com.example.phonepeassignment.main.data

import android.content.Context
import android.util.Log
import com.example.phonepeassignment.R
import com.example.phonepeassignment.main.data.models.LogoModel
import com.example.phonepeassignment.util.Utility
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import java.lang.reflect.Type
import javax.inject.Inject


class RawDataRepository @Inject constructor(
    @ApplicationContext private val context: Context
) : DataRepository {

    override suspend fun getLogosData() : List<LogoModel> {
        val data = Utility.getTextFromRawFile(
            context = context,
            fileResId = R.raw.input
        )

        try {
            val listType: Type = object : TypeToken<List<LogoModel>>() {}.type
            return Gson().fromJson(data, listType)
        } catch (e: JsonParseException) {
            e.message?.let { Log.e(RawDataRepository::class.java.simpleName, it) }
        } catch (e: JsonSyntaxException) {
            e.message?.let { Log.e(RawDataRepository::class.java.simpleName, it) }
        }
        return listOf()
    }

}