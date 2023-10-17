package com.example.phonepeassignment.main.data

import com.example.phonepeassignment.main.data.models.LogoModel

interface DataRepository {

    suspend fun getLogosData() : List<LogoModel>

}