package com.trenicalea.trintedapp.appwrite

import android.content.Context
import io.appwrite.Client
import io.appwrite.services.Account
import io.appwrite.services.Databases

data class AppwriteConfig(var context: Context) {

    private var client = Client(context)
        .setEndpoint("https://cloud.appwrite.io/v1")
        .setProject("645d4c2c39e030c6f6ba")
        .setSelfSigned(true)

    var account = Account(client)

    var databases = Databases(client)
}
