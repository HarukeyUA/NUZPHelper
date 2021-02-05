/*
 * Copyright 2021 Nazar Rusnak
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.harukey.zpnuhelper.phoneBook

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.PrintWriter
import java.util.*

class PhoneBookRepository(private val fetcher: PhoneBookFetcher, private val cache: File) {

    private val _phoneBook = MutableLiveData<List<PhoneBookItem>>()

    val phoneBook: LiveData<List<PhoneBookItem>> get() = _phoneBook

    private var phoneBookCopy: List<PhoneBookItem> = listOf()

    private val moshi by lazy {
        Moshi.Builder().add(
            PolymorphicJsonAdapterFactory.of(PhoneBookItem::class.java, "entryType")
                .withSubtype(PhoneBookTitle::class.java, "title")
                .withSubtype(PhoneBookEntry::class.java, "entry")
        ).add(KotlinJsonAdapterFactory())
            .build()
    }

    private val moshiType = Types.newParameterizedType(
        List::class.java,
        PhoneBookItem::class.java
    )

    suspend fun loadPhoneBook(forceUpdate: Boolean = false) {
        if (cache.exists() && !forceUpdate) {
            try {
                phoneBookCopy = readCache()
                _phoneBook.value = phoneBookCopy.toList()
            } catch (e: Exception) {
                e.printStackTrace()
                getListFromWeb()
            }
        } else {
            getListFromWeb()
        }
    }

    private suspend fun writeCacheFile() {
        withContext(Dispatchers.IO) {
            val adapter = moshi.adapter<List<PhoneBookItem>>(moshiType)
            val json = adapter.toJson(phoneBookCopy)
            try {
                PrintWriter(cache).use { out -> out.print(json) }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private suspend fun readCache(): List<PhoneBookItem> {
        return withContext(Dispatchers.IO) {
            val adapter = moshi.adapter<List<PhoneBookItem>>(moshiType)
            val json = cache.bufferedReader().use { it.readText() }
            val loadedList = adapter.fromJson(json)
            if (loadedList != null) {
                loadedList
            } else {
                throw NullPointerException()
            }
        }
    }

    private suspend fun getListFromWeb() {
        phoneBookCopy = fetcher.getPhoneList()
        _phoneBook.value = phoneBookCopy.toList()
        writeCacheFile()
    }

    fun clearFilter() {
        _phoneBook.value = phoneBookCopy.toList()
    }

    fun filter(query: String) {
        if (query.isEmpty())
            clearFilter()
        else {
            _phoneBook.value = getFilteredList(query.toLowerCase(Locale.getDefault()))
        }
    }

    private fun getFilteredList(query: String): List<PhoneBookItem> {
        val tempList = mutableListOf<PhoneBookItem>()
        var lastTitle: PhoneBookTitle? = null
        for ((index, item) in phoneBookCopy.withIndex()) {
            if (item is PhoneBookTitle)
                continue

            if (isEntryHasContent(item as PhoneBookEntry, query)) {
                val title = getTitleForEntry(index)
                title?.let {
                    if (lastTitle == null || lastTitle != title) {
                        tempList.add(title)
                        lastTitle = title
                    }
                    tempList.add(item)
                }
            }
        }
        return tempList
    }

    private fun isEntryHasContent(entry: PhoneBookEntry, content: String): Boolean {
        return entry.name.toLowerCase(Locale.getDefault()).contains(content) ||
                entry.assignment.toLowerCase(Locale.getDefault()).contains(content) ||
                entry.location.toLowerCase(Locale.getDefault()).contains(content)
    }

    private fun getTitleForEntry(index: Int): PhoneBookTitle? {
        for (i in index downTo 0) {
            if (phoneBookCopy[i] is PhoneBookTitle) return phoneBookCopy[i] as PhoneBookTitle
        }
        return null
    }

}