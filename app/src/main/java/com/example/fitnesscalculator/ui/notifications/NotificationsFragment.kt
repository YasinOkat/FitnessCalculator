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
            "Sedentary (no exercise)",
            "Light (1-3 day of exercise)",
            "Moderate (3-5 day of exercise)",
            "Active (5-7 day of exercise) ",
            "Very Active (5-7 day of intense exercise)"
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
        val weight = weightEditText.text.toString().toDoubleOrNull() ?: 0.0
        val bodyFatRatio = bodyFatEditText.text.toString().toDoubleOrNull() ?: 0.0
        val activityLevel = activityLevelSpinner.selectedItem.toString()

        val bmr = calculateBMR(weight, bodyFatRatio)
        val dailyCalorieNeeds = calculateDailyCalorieNeeds(bmr, activityLevel)

        bmrTextView.text = "BMR: %.2f kcal".format(bmr)
        dailyCalorieNeedsTextView.text = "Calories to maintain weight: %.2f kcal".format(dailyCalorieNeeds)
    }

    private fun calculateBMR(weight: Double, bodyFatRatio: Double): Double {
        val leanBodyMass = weight * (100 - bodyFatRatio) / 100
        return 370 + (21.6 * leanBodyMass)
    }

    private fun calculateDailyCalorieNeeds(bmr: Double, activityLevel: String): Double {
        return when (activityLevel) {
            "Sedentary (no exercise)" -> bmr * 1.2
            "Light (1-3 day of exercise)" -> bmr * 1.375
            "Moderate (3-5 day of exercise)" -> bmr * 1.55
            "Active (5-7 day of exercise)" -> bmr * 1.725
            "Very Active (5-7 day of intense exercise)" -> bmr * 1.9
            else -> bmr
        }
    }
}
