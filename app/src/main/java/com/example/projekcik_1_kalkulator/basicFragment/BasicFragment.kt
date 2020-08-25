package com.example.projekcik_1_kalkulator.basicFragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.projekcik_1_kalkulator.R
import com.example.projekcik_1_kalkulator.databinding.FragmentBasicBinding

class BasicFragment : Fragment() {

    private lateinit var viewModel: BasicFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentBasicBinding.inflate(inflater, container, false)

        viewModel = ViewModelProviders.of(this).get(BasicFragmentViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }
}
