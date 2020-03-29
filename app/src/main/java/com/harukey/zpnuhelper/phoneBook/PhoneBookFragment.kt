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

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.harukey.zpnuhelper.R
import com.harukey.zpnuhelper.databinding.FragmentPhoneBookBinding
import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil

class PhoneBookFragment : Fragment() {

    private val viewModel: PhoneBookViewModel by viewModels()

    private lateinit var binding: FragmentPhoneBookBinding

    private var isSearchOpen = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_phone_book, container, false)

        val phoneBookListAdapter = PhoneBookListAdapter()
        binding.phoneCardsList.adapter = phoneBookListAdapter

        viewModel.phoneBookList.observe(viewLifecycleOwner, { list ->
            clearState()
            phoneBookListAdapter.submitList(list)
            if (list.isEmpty())
                binding.emptyListPlaceholder.visibility = View.VISIBLE
        })

        viewModel.state.observe(viewLifecycleOwner, { state ->
            when(state) {
                ContentState.Ok -> clearState()
                ContentState.FetchError -> fetchError()
                ContentState.ParseError -> fetchError()
                ContentState.Loading -> showLoadingState()
                else -> clearState()
            }
        })

        binding.refreshButton.setOnClickListener {
            viewModel.loadPhoneBook()
        }

        binding.searchButton.setOnClickListener {
            showSearch()
            isSearchOpen = true
        }

        binding.searchTextInput.addTextChangedListener { text ->
            viewModel.filterList(text.toString())
            binding.phoneCardsList.scrollToPosition(0)
        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (isSearchOpen) {
                    hideSearch()
                    isSearchOpen = false
                } else {
                    isEnabled = false
                    activity?.onBackPressed()
                }
            }

        })

        binding.helpButton.setOnClickListener {
            val dialog = Dialog(requireContext())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.phone_book_help_dialog)
            dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog.findViewById<Button>(R.id.okButton).setOnClickListener {
                dialog.dismiss()
            }
            dialog.setCanceledOnTouchOutside(true)
            dialog.show()
        }

        viewModel.loadPhoneBook()
        return binding.root
    }

    private fun showLoadingState() {
        clearState()
        binding.progressGroup.visibility = View.VISIBLE
    }

    private fun fetchError() {
        clearState()
        binding.connectionErrorGroup.visibility = View.VISIBLE
    }

    private fun clearState() {
        binding.progressGroup.visibility = View.INVISIBLE
        binding.connectionErrorGroup.visibility = View.INVISIBLE
        binding.emptyListPlaceholder.visibility = View.INVISIBLE
    }

    private fun showSearch() {
        binding.titleSearchAreaGroup.visibility = View.INVISIBLE
        binding.searchTextInputLayout.visibility = View.VISIBLE
        binding.searchTextInput.requestFocus()
        UIUtil.showKeyboard(requireContext(), binding.searchTextInput)
    }

    private fun hideSearch() {
        binding.searchTextInput.text?.clear()
        binding.searchTextInputLayout.visibility = View.INVISIBLE
        binding.titleSearchAreaGroup.visibility = View.VISIBLE
    }


}