package com.trenicalea.trintedapp.models

import android.graphics.Bitmap
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.ToJson
import java.io.ByteArrayOutputStream

class BitmapAdapter {

    private val _encodedImage: String =
        "iVBORw0KGgoAAAANSUhEUgAAAIIAAACCCAMAAAC93eDPAAAAY1BMVEX///8AAADAwMDq6ur39/fy8vK8vLz6+vrOzs7ExMQbGxve3t7a2tqcnJyNjY1VVVUvLy+CgoJkZGR8fHyqqqohISFycnJHR0c2NjYVFRWzs7OTk5NsbGxCQkI9PT0qKioLCwujvJYNAAACUElEQVR4nO2ai3KCMBBFXcIjEBAhBaX46P9/ZbXU6WgN7CKbOtM9P8CZEHZvNqxWgiAIgiAIgvBPSJROU62SP3p8GFcb+GZTxaF3AVNlcENmjVcBVX3AbyrlzyDOHgic6QJfBtVjga+F8CIQ1m4DgNrHthw1ODvwG7TjBgAtt8HblAHAG6+BmTYA4C0QExthgHU7pBgDgJRRYYtT2PIZ5DgDgJxNoccq9GwKO6zCjssgwhoAREwKGq+gmRQKvELBpDDSpO/hatoWr2CZFCab5A9c7fIFXsQar7BmUkBkhStcmQHdIviaRIJXYDJABpYLfKEFvRn44iO6T3F1qRW6PnLVxgumwxh0rBEaVZ246tJAiAhOO+ZjJeIswz7qeJ8yeOc2mMxOXHnphtF1YD7TXtGNS6Dhiq2/UI781Hqcd630g49z520JrhL2ZuzWWd8CF0Ld78vD8Xgo9732P3y9kkRn/moELQiC8KJEypg8N0b5L5BJZILe1uVh05xTfddsjmVt+8BEnjpFXlSlK7JsbcHcMENdIKbQhyJlWg0V16fp5w+c6njxABWmreNi0EXWLroWau0MrKMW66WWQu/nPH+gXmJ3GvRoxSHx7NgpIswaXdinBh7x8wIX5h8zI8LEd5x25kIo5GUYhu2sb0MRC8E42QwHNasUuGnIDomzFc2lpDZ0wiUQFuLwQy1vAEB7FehLUQq0C9SF9+LAiWJAuBSlQClQhEtRCpS2GfAoUH57EgVREAVReEGFhZLzPTFBQcUBA8sfdQVBEARBEATBN5+8kR2XPHMgagAAAABJRU5ErkJggg=="

    @FromJson
    fun fromJson(reader: JsonReader): Bitmap? {
        try {
            val base64 = reader.nextString()
            val data = android.util.Base64.decode(base64, android.util.Base64.DEFAULT)
            return android.graphics.BitmapFactory.decodeByteArray(data, 0, data.size)
        } catch (e: Exception) {
            val decodedString =
                android.util.Base64.decode(_encodedImage, android.util.Base64.DEFAULT)
            return android.graphics.BitmapFactory.decodeByteArray(
                decodedString,
                0,
                decodedString.size
            )
        }
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
