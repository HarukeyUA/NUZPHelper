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

package com.harukey.zpnuhelper.daySchedule.classSchedule

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.harukey.zpnuhelper.R
import com.harukey.zpnuhelper.databinding.ClassScheduleItemBinding
import com.harukey.zpnuhelper.daySchedule.classSchedule.db.ClassScheduleItem

class ClassScheduleAdapter(private val viewModel: ClassScheduleViewModel
): ListAdapter<ClassScheduleItem, ClassScheduleAdapter.ScheduleEntryViewHolder>(DiffCallback()) {

    class ScheduleEntryViewHolder(private val binding: ClassScheduleItemBinding,
                                  private val viewModel: ClassScheduleViewModel
    ): RecyclerView.ViewHolder(binding.root) {

        private var currentEntry: ClassScheduleItem? = null

        init {
            binding.viewModel = viewModel
            binding.classNameEditText.addTextChangedListener { name ->
                currentEntry?.let {
                    it.ClassName = name.toString()
                    viewModel.updateScheduleEntry(it)
                }

            }

            binding.classNumEditText.addTextChangedListener { number ->
                currentEntry?.let {
                    it.ClassNum = number.toString()
                    viewModel.updateScheduleEntry(it)
                }

            }
        }

        fun bind(entry: ClassScheduleItem) {
            currentEntry = entry
            binding.classIndex = entry.Time
            binding.classScheduleItem = entry
            binding.executePendingBindings()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleEntryViewHolder {
        val binding = DataBindingUtil.inflate<ClassScheduleItemBinding>(LayoutInflater.from(parent.context),
            R.layout.class_schedule_item, parent, false)
        return ScheduleEntryViewHolder(binding, viewModel)
    }

    override fun onBindViewHolder(holder: ScheduleEntryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<ClassScheduleItem>() {
        override fun areItemsTheSame(
            oldItem: ClassScheduleItem,
            newItem: ClassScheduleItem
        ): Boolean = oldItem === newItem


        override fun areContentsTheSame(
            oldItem: ClassScheduleItem,
            newItem: ClassScheduleItem
        ): Boolean = oldItem == newItem
    }
}