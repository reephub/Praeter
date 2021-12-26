package com.praeter.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.praeter.data.local.DbImpl
import com.praeter.data.remote.ApiImpl
import com.praeter.data.remote.dto.ApiResponse
import com.praeter.data.remote.dto.directions.GoogleDirectionsResponse
import com.praeter.data.remote.dto.user.User
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val dbImpl: DbImpl,
    private val apiImpl: ApiImpl
) : IRepository {


    private val mLocationData: MediatorLiveData<Boolean>
        get() = MediatorLiveData()

    override fun getLocationStatusData(): LiveData<Boolean> {
        return mLocationData
    }

    override fun addLocationStatusDataSource(data: LiveData<Boolean>) {
        mLocationData.addSource(data, mLocationData::setValue)
    }

    override fun removeLocationStatusDataSource(data: LiveData<Boolean>) {
        mLocationData.removeSource(data)
    }


    override suspend fun deleteAll() = dbImpl.deleteAll()


    override suspend fun dbConnection(): ApiResponse {
        return apiImpl.dbConnection()
    }

    override suspend fun loginUser(user: User): ApiResponse {
        return apiImpl.loginUser(user)
    }

    override suspend fun saveUser(user: User): ApiResponse {
        return apiImpl.saveUser(user)
    }

    override suspend fun getDirections(
        origin: String,
        destination: String
    ): GoogleDirectionsResponse {
        return apiImpl.getDirections(origin, destination)
    }

}