package com.example.simplepkce

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.simplepkce.databinding.FragmentTitleBinding
import com.example.simplepkce.model.TokenResponse
import com.example.simplepkce.repository.Repository
import com.example.simplepkce.util.Config.AUTHORIZE_ENDPOINT
import com.example.simplepkce.util.Config.CLIENT_ID
import com.example.simplepkce.util.Config.GRANT_TYPE
import com.example.simplepkce.util.Config.REDIRECT_URI
import com.example.simplepkce.util.PKCEHelper
import org.json.JSONObject


class TitleFragment : Fragment() {

    private val TAG = "TitleFragment"
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.logout)?.isVisible = false
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val repository = Repository.getInstance(this.requireContext())
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = activity?.let { ViewModelProvider(it, viewModelFactory).get(MainViewModel::class.java) }!!

        val binding = DataBindingUtil.inflate<FragmentTitleBinding>(inflater, R.layout.fragment_title,container, false)
        binding.signInButton.setOnClickListener { view: View ->
            setLoading()
            viewModel.initiateAuth(requireContext(), CLIENT_ID, REDIRECT_URI, AUTHORIZE_ENDPOINT)
        }

        viewModel.intent.observe(viewLifecycleOwner) { intent: Intent? ->
            if (intent != null) {
                val callbackURI = Uri.parse(intent.toString())
                val code = callbackURI.getQueryParameter("code")
                if (code?.isEmpty() == false) {
                    getToken(code)
                } else {
                    stopLoading()
                }
            } else {
                stopLoading()
            }
        }

        viewModel.tokenInfo.observe(viewLifecycleOwner) {  it: TokenResponse? ->
            if (it != null) {
                this.view?.findNavController()?.navigate(R.id.homeFragment)
            }
        }

        return binding.root
    }

    private fun getToken(code: String) {
        viewModel.getTokenInfo(CLIENT_ID, REDIRECT_URI, GRANT_TYPE, PKCEHelper.getCodeVerifier(), code)
        viewModel.tokenResponse.observe(viewLifecycleOwner, Observer { response ->
            if (response.isSuccessful) {
                response.body()?.let {
                    viewModel.setTokenInfo(it)
                    this.view?.findNavController()?.navigate(R.id.homeFragment)
                    stopLoading()
                }
            } else {
                stopLoading()
                try {
                    val jObjError = JSONObject(response.errorBody()!!.string())
                    Log.i(TAG, "Error detail $jObjError")
                    val errView = this.view?.findViewById<TextView>(R.id.errTextView)
                    errView?.findViewById<TextView>(R.id.errTextView)?.text  = "${jObjError["error_description"]} because ${jObjError["cause"]}"
                    errView?.findViewById<TextView>(R.id.errTextView)?.isVisible = true
                } catch (e: Exception) {
                    Log.i(TAG, "Error message ${e.message}")
                }
            }

        })
    }

    fun setLoading() {
        this.view?.findViewById<TextView>(R.id.errTextView)?.isVisible = false
        this.view?.findViewById<View>(R.id.signInButton)?.isVisible = false
        this.view?.findViewById<ProgressBar>(R.id.loginProgressBar)?.isVisible  = true
    }

    fun stopLoading() {
        this.view?.findViewById<View>(R.id.signInButton)?.isVisible = true
        this.view?.findViewById<ProgressBar>(R.id.loginProgressBar)?.isVisible = false
    }
}

