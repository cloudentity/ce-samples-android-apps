package com.example.simplepkce

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Space
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.simplepkce.databinding.FragmentResourceBinding
import com.example.simplepkce.model.ScopeData
import com.example.simplepkce.model.TokenResponse
import com.example.simplepkce.repository.Repository
import com.example.simplepkce.util.Utils.getScopeData


class ResourceFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var tokenInfo: TokenResponse

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val repository = Repository.getInstance(this.requireContext())
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = activity?.let { ViewModelProvider(it, viewModelFactory).get(MainViewModel::class.java) }!!
        val binding = DataBindingUtil.inflate<FragmentResourceBinding>(inflater, R.layout.fragment_resource,container, false)
        binding.homeButton.setOnClickListener {
            it.findNavController().navigate(R.id.homeFragment)
        }

        binding.headerButton.setOnClickListener {
            it.findNavController().navigate(R.id.headerFragment)
        }

        viewModel.tokenInfo.observe(viewLifecycleOwner) { it: TokenResponse? ->
            if (it != null) {
                tokenInfo = it

                // for each scope find the corresponding ScopeData
                tokenInfo.scope.split(" ").toTypedArray().forEach {
                    getScopeData(requireActivity())?.forEach { sd ->
                        if (it == sd.scope) {
                            // add a button for each scope
                            addButton(sd, binding.resourceButtonLayout)
                        }
                    }
                }
            }
        }

        return binding.root
    }

    private fun addSpacerToButtonView(layout: LinearLayout) {
        val space = Space(activity)
        space.setBackgroundColor(resources.getColor(R.color.white))
        space.layoutParams = LinearLayout.LayoutParams(80, 20, 20F)
        layout.addView(space)
    }

    private fun addButtonToView(
        scope: ScopeData,
        layout: LinearLayout
    ): Button {
        val button = Button(activity)
        button.text = scope.title
        button.setBackgroundColor(resources.getColor(R.color.black))
        button.setTextColor(resources.getColor(R.color.white))
        button.layoutParams = LinearLayout.LayoutParams(620, 135, 1F)
        layout.addView(button)

        return button
    }

    private fun addButton(scope: ScopeData, layout: LinearLayout) {
        val button = addButtonToView(scope, layout)
        button.setOnClickListener {
            viewModel.getResource(scope, tokenInfo)
            it.findNavController().navigate(R.id.resourceResultFragment)
        }
        addSpacerToButtonView(layout)
    }
}