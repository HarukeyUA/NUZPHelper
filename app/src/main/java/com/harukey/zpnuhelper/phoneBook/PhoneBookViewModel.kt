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

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException

enum class ContentState { Ok, FetchError, ParseError, Loading }

class PhoneBookViewModel(application: Application) : AndroidViewModel(application) {

    var phoneBookList: LiveData<List<PhoneBookItem>>
        private set

    private val repository =
        PhoneBookRepository(PhoneBookFetcher(), File(application.cacheDir, "phoneBook.json"))

    private val _state = MutableLiveData<ContentState>()
    val state: LiveData<ContentState>
        get() = _state

    init {
        phoneBookList = repository.phoneBook
    }

    fun loadPhoneBook() {
        viewModelScope.launch {
            try {
                _state.value = ContentState.Loading
                repository.loadPhoneBook()
                Log.d(this.javaClass.simpleName, "Parsed")
                _state.value = ContentState.Ok
            } catch (io: IOException) {
                _state.value = ContentState.FetchError
                io.printStackTrace()
            } catch (parse: Exception) {
                _state.value = ContentState.ParseError
                parse.printStackTrace()
            } finally {
                if (phoneBookList.value?.isNotEmpty() == true)
                    _state.value = ContentState.Ok
            }
        }
    }

    fun filterList(query: String) {
        if (state.value == ContentState.Ok)
            repository.filter(query)
    }

}