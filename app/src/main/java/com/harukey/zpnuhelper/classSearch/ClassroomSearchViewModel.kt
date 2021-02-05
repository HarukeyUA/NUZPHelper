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

package com.harukey.zpnuhelper.classSearch

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.harukey.zpnuhelper.R
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.reflect.Type

class ClassroomSearchViewModel(application: Application) : AndroidViewModel(application) {

    private val _currentClassInfoItem = MutableLiveData<ClassInfoItem>()
    val currentClassInfoItem: LiveData<ClassInfoItem>
        get() = _currentClassInfoItem

    var searchedClassroomNum: String = ""
        private set

    private val _notFoundEvent = MutableLiveData<Boolean>()
    val notFoundEvent: LiveData<Boolean>
        get() = _notFoundEvent

    private var classInfoList: Map<String, ClassInfoItem>? = null

    private val _isCardShown = MutableLiveData(false)
    val isCardShown: LiveData<Boolean>
        get() = _isCardShown

    private val moshi: Moshi by lazy {
        Moshi.Builder().build()
    }

    private val jsonStructType: Type by lazy {
        Types.newParameterizedType(
            Map::class.java,
            String::class.java,
            ClassInfoItem::class.java
        )
    }

    init {
        viewModelScope.launch {
            classInfoList = loadJsonString()
        }
    }

    fun findClassroom(query: String) {
        val value = classInfoList?.get(query)
        if (value != null) {
            _isCardShown.value = true
            searchedClassroomNum = query
            _currentClassInfoItem.value = value
        } else
            _notFoundEvent.value = true
    }

    fun notFoundEventHandled() {
        _notFoundEvent.value = false
    }

    private suspend fun loadJsonString(): Map<String, ClassInfoItem>? {
        return withContext(Dispatchers.IO) {
            val json = getApplication<Application>().resources.openRawResource(R.raw.rooms)
                .bufferedReader()
                .use { it.readText() }
            val jsonAdapter: JsonAdapter<Map<String, ClassInfoItem>> = moshi.adapter(jsonStructType)
            jsonAdapter.fromJson(json)
        }
    }

}
