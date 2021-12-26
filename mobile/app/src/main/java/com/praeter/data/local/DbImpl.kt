package com.praeter.data.local

import javax.inject.Inject

class DbImpl @Inject constructor(
) : IDb {

    override suspend fun deleteAll() {
    }
}