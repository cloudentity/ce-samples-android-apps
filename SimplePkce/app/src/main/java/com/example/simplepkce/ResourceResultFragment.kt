package com.example.simplepkce

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.simplepkce.databinding.FragmentResourceResultBinding
import com.example.simplepkce.repository.Repository


class ResourceResultFragment : Fragment() {

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val repository = Repository.getInstance(this.requireContext())
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = activity?.let { ViewModelProvider(it, viewModelFactory).get(MainViewModel::class.java) }!!
        val binding = DataBindingUtil.inflate<FragmentResourceResultBinding>(inflater, R.layout.fragment_resource_result,container, false)
        binding.resourcesButton.setOnClickListener {
            viewModel.clearResource()
            it.findNavController().navigate(R.id.resourceFragment)
        }
        viewModel.resourceResult.observe(viewLifecycleOwner) {
            binding.resultTextView.text = it
        }

        return binding.root
    }

}