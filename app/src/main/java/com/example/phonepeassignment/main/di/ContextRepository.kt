package com.example.phonepeassignment.main.di

import android.content.Context
import android.content.res.Resources
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ContextRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun getContext() : Context = context

    fun getResource() : Resources = context.resources

}