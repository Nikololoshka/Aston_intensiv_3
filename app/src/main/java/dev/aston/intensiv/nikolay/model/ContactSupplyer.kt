package dev.aston.intensiv.nikolay.model

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dev.aston.intensiv.nikolay.R

object ContactSupplyer {

    fun loadContacts(context: Context): List<ContactItem> {
        return context.resources.openRawResource(R.raw.contacts)
            .bufferedReader()
            .use { reader ->
                val type = object : TypeToken<List<ContactItem>>() {}
                Gson().fromJson(reader, type)
            }
    }
}