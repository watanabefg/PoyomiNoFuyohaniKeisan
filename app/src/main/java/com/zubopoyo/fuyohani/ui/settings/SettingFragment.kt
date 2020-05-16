package com.zubopoyo.fuyohani.ui.settings

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.zubopoyo.fuyohani.R
import com.zubopoyo.fuyohani.database.entity.Config
import java.text.SimpleDateFormat
import java.util.*

class SettingFragment : Fragment() {

    private lateinit var settingViewModel: SettingViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_settings, container, false)
        val selectedValues = resources.obtainTypedArray(R.array.price_entry_values)
        val priceSetting = root.findViewById<Spinner>(R.id.price_setting)
        val myAdapter = priceSetting.adapter

        settingViewModel =
                ViewModelProviders.of(this).get(SettingViewModel::class.java)
        settingViewModel.config.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it != null) {
                (0..(myAdapter.count - 1)).forEach { key ->
                    if (selectedValues.getInt(
                            key,
                            0
                        ) == it.cappedAmount && it.applicableYear == SimpleDateFormat("yyyy").format(
                            Date()
                        ).toInt()
                    ) {
                        priceSetting.setSelection(myAdapter.getItemId(key).toInt())
                    }
                }
            }
        })

        val button = root.findViewById<Button>(R.id.button)
        button.setOnClickListener {
            val cappedAmount = selectedValues.getInt(priceSetting.selectedItemPosition, -1)
            // 現在の年でDBに保存
            val year = SimpleDateFormat("yyyy").format(Date()).toInt()
            val config = Config(year, cappedAmount)
            settingViewModel.insert(config)
            // Toast
            Toast.makeText(
                context,
                "保存しました",
                Toast.LENGTH_LONG
            ).show()
        }

        return root
    }
}