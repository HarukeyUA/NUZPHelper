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

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.util.*


class PhoneBookFetcher {

    private val tableURL =
        "https://zp.edu.ua/spisok-telefoniv-strukturnih-pidrozdiliv-universitetu/"
    private val cityNumberPrefix = "38061"
    private val localNumberPrefix = "380617698"

    suspend fun getPhoneList(): List<PhoneBookItem> {
        Log.d(this.javaClass.simpleName, "Fetching data...")
        val nuzpPhoneBookPage = getDocumentToParse()
        Log.d(this.javaClass.simpleName, "Received Document")
        return parsePhoneTable(nuzpPhoneBookPage)
    }

    private suspend fun parsePhoneTable(
        mBlogDocument: Document
    ): List<PhoneBookItem> {
        return withContext(Dispatchers.Default) {
            val phoneBookItems = mutableListOf<PhoneBookItem>()
            val table: Element = mBlogDocument.select("table")[0].select("tbody")[0]
            val rows: Elements = table.select("tr")
            for (row in rows) {
                val cols: Elements = row.select("td")
                when (cols.size) {
                    1 -> {
                        phoneBookItems.add(
                            PhoneBookTitle(
                                cols[0].text().toLowerCase(Locale.getDefault())
                                    .capitalize(Locale.ROOT)
                            )
                        )
                    }
                    2 -> {
                        phoneBookItems.add(
                            PhoneBookEntry(
                                "", cols[1].text(), "", "", ""
                            )
                        )
                    }
                    4 -> {
                        phoneBookItems.add(
                            PhoneBookEntry(
                                cols[0].text(), cols[1].text(), cols[2].text(), "",
                                expandNumber(cols[3].text(), localNumberPrefix)
                            )
                        )
                    }
                    5 -> {
                        phoneBookItems.add(
                            PhoneBookEntry(
                                cols[0].text(),
                                cols[1].text(),
                                cols[2].text(),
                                expandNumber(cols[3].text(), cityNumberPrefix),
                                expandNumber(cols[4].text(), localNumberPrefix)
                            )
                        )
                    }
                    else -> {
                        Log.e(
                            this.javaClass.simpleName, "Unknown number of columns: ${cols.size}"
                        )
                    }
                }
            }
            phoneBookItems
        }
    }

    private suspend fun getDocumentToParse(): Document {
        return withContext(Dispatchers.IO) {
            Jsoup.connect(tableURL).get()
        }
    }

    private fun expandNumber(item: String, prefix: String): String {
        if (item == "")
            return item

        val numbers = item.replace("-", "").split(" ").toMutableList()
        for (i in numbers.indices) {
            if (numbers[i][0].isLetter()) // Fax section starts, stop appending prefixes
                break
            if (numbers[i].length > 7) // If number length > 7 - it's already expanded, no need to add anything
                continue
            numbers[i] = prefix.plus(numbers[i])
        }

        return numbers.reduce { curr, result -> "$curr $result" }
    }
}