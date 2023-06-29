package com.trenicalea.trintedapp.appwrite

import android.content.Context

data class AppwriteConfig(var context: Context) {

    val appContext: Context = context

    val endpoint: String = "https://cloud.appwrite.io/v1"

    val projectId: String = "645d4c2c39e030c6f6ba"

}
