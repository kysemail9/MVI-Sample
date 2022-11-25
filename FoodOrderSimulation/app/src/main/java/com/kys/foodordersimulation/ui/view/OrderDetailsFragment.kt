package com.kys.foodordersimulation.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.kys.foodordersimulation.R
import com.kys.foodordersimulation.databinding.FragmentOrderDetailsBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class OrderDetailsFragment : Fragment() {

    private var _binding: FragmentOrderDetailsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentOrderDetailsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonPrev.setOnClickListener {
            findNavController().navigate(R.id.action_OrderDetailsFragment_to_FoodOrderFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}