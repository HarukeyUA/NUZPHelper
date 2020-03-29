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

package com.harukey.zpnuhelper.classSearch

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.harukey.zpnuhelper.R
import com.harukey.zpnuhelper.databinding.ClassroomSearchFragmentBinding


class ClassroomSearchFragment : Fragment() {

    private val viewModel: ClassroomSearchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<ClassroomSearchFragmentBinding>(inflater, R.layout.classroom_search_fragment, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.shareInfoFAB.hide()

        binding.queryInput.setOnEditorActionListener { v, _, _ ->
            v?.let {
                viewModel.findClassroom(v.text.toString())
            }
            false
        }

        viewModel.currentClassInfoItem.observe(viewLifecycleOwner, { classInfoItem ->
            binding.classroomNum.text = getString(R.string.classroom_num, binding.queryInput.text)
            binding.buildingNum.text = getString(R.string.university_building_num, classInfoItem.building)
            binding.floorNum.text = getString(R.string.floor_num, classInfoItem.floor)
            binding.shareInfoFAB.show()
            binding.textInputLayout.error = null
        })

        viewModel.notFoundEvent.observe(viewLifecycleOwner, { status ->
            if (status) {
                binding.queryInput.text?.clear()
                binding.textInputLayout.error = getString(R.string.class_search_error)
                viewModel.notFoundEventHandled()
            }
        })

        binding.shareInfoFAB.setOnClickListener {
            viewModel.currentClassInfoItem.value?.let {
                shareInfo(viewModel.searchedClassroomNum,
                    it.floor,
                    it.building)
            }
        }

        return binding.root
    }

    private fun shareInfo(num: String, floor: String, building: String) {
        val sendIntent = Intent()
        with(sendIntent) {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT, getString(
                    R.string.text_to_share,
                    num,
                    building,
                    floor
                )
            )
            type = "text/plain"
        }

        sendIntent.resolveActivity(requireActivity().packageManager)?.let {
            startActivity(Intent.createChooser(sendIntent, getString(R.string.share)))
        }
    }

}
