package com.example.projekcik_1_kalkulator.scientificFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.example.projekcik_1_kalkulator.R
import com.example.projekcik_1_kalkulator.databinding.FragmentScientificBinding
import kotlinx.android.synthetic.main.activity_main.*

class ScientificFragment : Fragment() {

    private lateinit var viewModel: ScientificFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentScientificBinding.inflate(inflater, container, false)

        viewModel = ViewModelProviders.of(this).get(ScientificFragmentViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }
}