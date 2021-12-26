package com.praeter.data

import androidx.lifecycle.LiveData
import com.praeter.data.local.IDb
import com.praeter.data.remote.IApi


interface IRepository : IDb, IApi {


    fun getLocationStatusData(): LiveData<Boolean>

    fun addLocationStatusDataSource(data: LiveData<Boolean>)

    fun removeLocationStatusDataSource(data: LiveData<Boolean>)
}