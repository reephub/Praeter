package com.praeter.data.local

interface IDb {
    suspend fun deleteAll()
}