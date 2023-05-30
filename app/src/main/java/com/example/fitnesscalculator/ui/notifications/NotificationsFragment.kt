package com.example.fitnesscalculator.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.fitnesscalculator.R
import com.example.fitnesscalculator.databinding.FragmentNotificationsBinding

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    private lateinit var weightEditText: EditText
    private lateinit var bodyFatEditText: EditText
    private lateinit var activityLevelSpinner: Spinner
    private lateinit var calculateButton: Button
    private lateinit var bmrTextView: TextView
    private lateinit var dailyCalorieNeedsTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        weightEditText = binding.editWeight
        bodyFatEditText = binding.editBodyFat
        activityLevelSpinner = binding.spinnerActivityLevel
        calculateButton = binding.buttonCalculate
        bmrTextView = binding.textBMR
        dailyCalorieNeedsTextView = binding.textDailyCalorieNeeds

        setupActivityLevelSpinner()

        calculateButton.setOnClickListener {
            calculateBMRAndCalorieNeeds()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupActivityLevelSpinner() {
        val activityLevels = arrayOf(
            getString(R.string.sedentary),
            getString(R.string.light),
            getString(R.string.moderate),
            getString(R.string.active),
            getString(R.string.very_active)
        )

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            activityLevels
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        activityLevelSpinner.adapter = adapter
    }

    private fun calculateBMRAndCalorieNeeds() {
        val weightText = weightEditText.text.toString().trim()
        val bodyFatText = bodyFatEditText.text.toString().trim()

        if (weightText.isEmpty() || bodyFatText.isEmpty()) {
            bmrTextView.text = getString(R.string.enter_values)
            dailyCalorieNeedsTextView.text = ""
            return
        }

        val weight = weightText.toDoubleOrNull() ?: 0.0
        val bodyFatRatio = bodyFatText.toDoubleOrNull() ?: 0.0
        val activityLevel = activityLevelSpinner.selectedItem.toString()

        val bmr = calculateBMR(weight, bodyFatRatio)
        val dailyCalorieNeeds = calculateDailyCalorieNeeds(bmr, activityLevel)

        bmrTextView.text = getString(R.string.bmr_label).format(bmr)
        dailyCalorieNeedsTextView.text = getString(R.string.calorie_needs_label).format(dailyCalorieNeeds)
    }


    private fun calculateBMR(weight: Double, bodyFatRatio: Double): Double {
        val leanBodyMass = weight * (100 - bodyFatRatio) / 100
        return 370 + (21.6 * leanBodyMass)
    }

    private fun calculateDailyCalorieNeeds(bmr: Double, activityLevel: String): Double {
        return when (activityLevel) {
            getString(R.string.sedentary) -> bmr * 1.2
            getString(R.string.light) -> bmr * 1.375
            getString(R.string.moderate) -> bmr * 1.55
            getString(R.string.active) -> bmr * 1.725
            getString(R.string.very_active) -> bmr * 1.9
            else -> bmr
        }
    }
}
