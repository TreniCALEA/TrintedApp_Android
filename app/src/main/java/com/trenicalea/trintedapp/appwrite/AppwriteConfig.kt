package com.trenicalea.trintedapp.appwrite

import io.appwrite.services.Account
import android.app.Application
import io.appwrite.Client
import io.appwrite.services.Databases

val _application: Application = Application()

val client: Client = Client(_application.applicationContext)
                            .setEndpoint("https://cloud.appwrite.io/v1")
                            .setProject("645d4c2c39e030c6f6ba")

val account: Account = Account(client)

val databases: Databases = Databases(client)
