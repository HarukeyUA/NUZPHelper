/*
 * Copyright 2020 Nazar Rusnak
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

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.harukey.zpnuhelper.R
import com.harukey.zpnuhelper.databinding.PhoneBookEntryBinding
import com.harukey.zpnuhelper.databinding.PhoneBookTitleBinding

private const val TITLE_VIEW_TYPE = 0
private const val ENTRY_VIEW_TYPE = 1

class PhoneBookListAdapter: ListAdapter<PhoneBookItem, RecyclerView.ViewHolder>(DiffCallback()) {

    class TitleViewHolder(private val binding: PhoneBookTitleBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(title: PhoneBookTitle) {
            binding.phoneBookTitle = title
            binding.executePendingBindings()
        }
    }

    class EntryViewHolder(private val binding: PhoneBookEntryBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(entry: PhoneBookEntry) {
            binding.phoneBookEntry = entry
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TITLE_VIEW_TYPE) {
            TitleViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context),
                R.layout.phone_book_title, parent, false))
        } else {
            EntryViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context),
                R.layout.phone_book_entry, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == TITLE_VIEW_TYPE) {
            val titleHolder = holder as TitleViewHolder
            titleHolder.bind(getItem(position) as PhoneBookTitle)
        } else if (holder.itemViewType == ENTRY_VIEW_TYPE) {
            val entryHolder = holder as EntryViewHolder
            entryHolder.bind(getItem(position) as PhoneBookEntry)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)) {
            is PhoneBookTitle -> TITLE_VIEW_TYPE
            is PhoneBookEntry -> ENTRY_VIEW_TYPE
        }
    }

    class DiffCallback: DiffUtil.ItemCallback<PhoneBookItem>() {
        override fun areItemsTheSame(oldItem: PhoneBookItem, newItem: PhoneBookItem): Boolean {
            return newItem === oldItem
        }

        override fun areContentsTheSame(oldItem: PhoneBookItem, newItem: PhoneBookItem): Boolean {
            return when(oldItem) {
                is PhoneBookTitle -> if (newItem is PhoneBookTitle) oldItem == newItem else false
                is PhoneBookEntry -> if (newItem is PhoneBookEntry) oldItem == newItem else false
            }
        }

    }
}