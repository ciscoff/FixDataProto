package s.yarlykov.fixdataproto

import android.util.Log

fun logIt(message: String, tag: String = "APP_TAG") {
    Log.i(tag, message)
    System.out.println("$tag: $message")
}