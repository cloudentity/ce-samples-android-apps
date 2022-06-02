package com.example.simplepkce

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.simplepkce.databinding.FragmentHeaderBinding
import com.example.simplepkce.model.TokenResponse
import com.example.simplepkce.repository.Repository
import com.example.simplepkce.util.JWTUtils


class HeaderFragment : Fragment() {

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val repository = Repository.getInstance(this.requireContext())
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = activity?.let { ViewModelProvider(it, viewModelFactory).get(MainViewModel::class.java) }!!
        val binding = DataBindingUtil.inflate<FragmentHeaderBinding>(inflater, R.layout.fragment_header,container, false)
        binding.homeButton.setOnClickListener {
            it.findNavController().navigate(R.id.homeFragment)
        }

        binding.resourcesButton.setOnClickListener {
            it.findNavController().navigate(R.id.resourceFragment)
        }

        viewModel.tokenInfo.observe(viewLifecycleOwner) { it: TokenResponse? ->
            val segments = it?.accessToken?.split(".")?.toTypedArray()

            if (segments != null) {
                if (segments.size != 3) {
                    binding.headerTokenTextView.text = getString(R.string.not_enough_segments)
                } else {
                    binding.headerTokenTextView.text = JWTUtils.getJsonFromSegment(segments[0])
                }
            } else {
                binding.headerTokenTextView.text = getString(R.string.unable_to_get_header)
            }
        }

        return binding.root
    }
}