package com.trenicalea.trintedapp.models

import android.graphics.Bitmap
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.ToJson
import java.io.ByteArrayOutputStream

class BitmapAdapter {
    @FromJson
    fun fromJson(reader: JsonReader): Bitmap? {
        // Read the JSON value and convert it back to a Bitmap object
        val base64 = reader.nextString()
        val data = android.util.Base64.decode(base64, android.util.Base64.DEFAULT)
        return android.graphics.BitmapFactory.decodeByteArray(data, 0, data.size)
    }

    @ToJson
    fun toJson(writer: JsonWriter, value: Bitmap?) {
        // Convert the Bitmap object to a base64-encoded string and write it as JSON
        if (value != null) {
            val outputStream = ByteArrayOutputStream()
            value.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            val data = outputStream.toByteArray()
            val base64 = android.util.Base64.encodeToString(data, android.util.Base64.DEFAULT)
            writer.value(base64)
        } else {
            writer.nullValue()
        }
    }
}
