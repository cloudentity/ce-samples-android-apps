package com.example.simplepkce

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.simplepkce.databinding.FragmentHomeBinding
import com.example.simplepkce.model.TokenResponse
import com.example.simplepkce.repository.Repository
import com.example.simplepkce.util.JWTUtils.getJsonFromSegment


class HomeFragment : Fragment() {

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val repository = Repository.getInstance(this.requireContext())
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = activity?.let { ViewModelProvider(it, viewModelFactory).get(MainViewModel::class.java) }!!

        val binding = DataBindingUtil.inflate<FragmentHomeBinding>(inflater, R.layout.fragment_home,container, false)
        binding.headerButton.setOnClickListener {
            it.findNavController().navigate(R.id.headerFragment)
        }

        binding.resourcesButton.setOnClickListener {
            it.findNavController().navigate(R.id.resourceFragment)
        }

        viewModel.tokenInfo.observe(viewLifecycleOwner) {  it: TokenResponse? ->
            val segments = it?.accessToken?.split(".")?.toTypedArray()

            if (segments != null) {
                if (segments.size != 3) {
                    binding.tokenTextView.text = getString(R.string.not_enough_segments)
                } else {
                    binding.tokenTextView.text = getJsonFromSegment(segments[1])
                }
            } else {
                binding.tokenTextView.text = getString(R.string.unable_to_get_payload)
            }
        }

        return binding.root
    }
}