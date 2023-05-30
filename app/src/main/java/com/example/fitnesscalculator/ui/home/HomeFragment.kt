package com.example.fitnesscalculator.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.fitnesscalculator.databinding.FragmentHomeBinding
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import com.example.fitnesscalculator.R
import kotlin.math.log10

class HomeFragment : Fragment() {

    private lateinit var genderMaleCheckBox: CheckBox
    private lateinit var genderFemaleCheckBox: CheckBox
    private lateinit var heightEditText: EditText
    private lateinit var neckEditText: EditText
    private lateinit var waistEditText: EditText
    private lateinit var hipEditText: EditText
    private lateinit var calculateButton: Button
    private lateinit var bodyFatTextView: TextView

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        genderMaleCheckBox = binding.checkboxMale
        genderFemaleCheckBox = binding.checkboxFemale
        heightEditText = binding.editHeight
        neckEditText = binding.editNeck
        waistEditText = binding.editWaist
        hipEditText = binding.editHip
        calculateButton = binding.buttonCalculate
        bodyFatTextView = binding.textBodyFat

        calculateButton.setOnClickListener { calculateBodyFatPercentage() }

        genderMaleCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                genderFemaleCheckBox.isChecked = false
                hipEditText.visibility = View.GONE
            }
        }

        genderFemaleCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                genderMaleCheckBox.isChecked = false
                hipEditText.visibility = View.VISIBLE
            } else {
                hipEditText.visibility = View.GONE
            }
        }

        return root
    }

    private fun calculateBodyFatPercentage() {
        val height = heightEditText.text.toString().toDoubleOrNull()
        val neck = neckEditText.text.toString().toDoubleOrNull()
        val waist = waistEditText.text.toString().toDoubleOrNull()
        val hip = hipEditText.text.toString().toDoubleOrNull()

        if (height != null && neck != null && waist != null) {
            if (genderMaleCheckBox.isChecked) {
                val bodyFatPercentage =
                    495 / (1.0324 - 0.19077 * log10(waist - neck) + 0.15456 * log10(height)) - 450
                bodyFatTextView.text = getString(R.string.body_fat_percentage, bodyFatPercentage)
            } else if (genderFemaleCheckBox.isChecked) {
                if (hip != null) {
                    val bodyFatPercentage =
                        495 / (1.29579 - 0.35004 * log10(waist + hip - neck) + 0.22100 * log10(height)) - 450
                    bodyFatTextView.text = getString(R.string.body_fat_percentage, bodyFatPercentage)
                } else {
                    bodyFatTextView.text = getString(R.string.error_invalid_hip)
                }
            } else {
                bodyFatTextView.text = getString(R.string.error_select_gender)
            }
        } else {
            bodyFatTextView.text = getString(R.string.error_invalid_input)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}