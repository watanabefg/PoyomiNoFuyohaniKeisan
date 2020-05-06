package com.zubopoyo.fuyohani.ui.input

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.zubopoyo.fuyohani.R
import com.zubopoyo.fuyohani.database.AppDatabase
import com.zubopoyo.fuyohani.database.entity.Salary
import java.lang.reflect.Array.getInt
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class InputFragment : Fragment() {

    private lateinit var inputViewModel: InputViewModel
    private lateinit var inputPagerAdapter: InputPagerAdapter
    private lateinit var viewPager: ViewPager

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        inputViewModel = ViewModelProvider(this).get(InputViewModel::class.java)
        inputViewModel.allSalaries.observe(viewLifecycleOwner, Observer { salaries ->
            salaries?.let { inputPagerAdapter.setSalaries(it) }
        })

        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        /*
        val textView: TextView = root.findViewById(R.id.text_dashboard)

        inputViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
         */
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        inputPagerAdapter = InputPagerAdapter(childFragmentManager)
        viewPager = view.findViewById(R.id.pager)
        viewPager.adapter = inputPagerAdapter
        val tabLayout = view.findViewById<TabLayout>(R.id.tab_layout)
        tabLayout.setupWithViewPager(viewPager)
    }
}

/*
  DBからは入力がある月の分だけ、直近の月がなければそれも表示する
 */
class InputPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
    private var salaries = emptyList<Salary>() // Cached copy of salaries

    override fun getCount(): Int = salaries.size

    internal fun setSalaries(salaries: List<Salary>) {
        this.salaries = salaries
        notifyDataSetChanged()
    }

    override fun getPageTitle(position: Int): CharSequence {
        val salary = salaries[position]
        return "${salary.year}年${salary.month}月"
    }

    override fun getItem(position: Int): Fragment{
        val fragment = InputMonthFragment()
        val salary = salaries[position]
        fragment.arguments = Bundle().apply {
            // Our object is just an integer :-P
            putInt(ARG_ID, salary.id)
            putInt(ARG_YEAR, salary.year)
            putInt(ARG_MONTH, salary.month)
            putInt(ARG_SALARY, salary.salary)
            putInt(ARG_EXPENSES, salary.expenses)
            putInt(ARG_EXTRAORDINARYINCOME, salary.extraordinaryIncome)
            putString(ARG_MEMO, salary.memo)
        }
        return fragment
    }
}

private const val ARG_ID = "id"
private const val ARG_YEAR = "year"
private const val ARG_MONTH = "month"
private const val ARG_SALARY = "salary"
private const val ARG_EXPENSES = "expenses"
private const val ARG_EXTRAORDINARYINCOME = "extraordinaryIncome"
private const val ARG_MEMO = "memo"

// Instances of this class are fragments representing a single
// object in our collection.
class InputMonthFragment : Fragment() {
    private lateinit var inputViewModel: InputViewModel

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        inputViewModel = ViewModelProvider(this).get(InputViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_inputmonth, container, false)
        val saveButton : Button = root.findViewById(R.id.saveButton)
        saveButton.setOnClickListener {
            Toast.makeText(
                context,
                "保存しました",
                Toast.LENGTH_LONG
            ).show()

            val salary = root.findViewById<EditText>(R.id.editText).text.toString().toInt()
            val expenses = root.findViewById<EditText>(R.id.editText2).text.toString().toInt()
            val income = root.findViewById<EditText>(R.id.editText3).text.toString().toInt()
            val memo : String = root.findViewById<EditText>(R.id.memoText).text.toString()
            arguments?.takeIf { it.containsKey(ARG_SALARY) }?.apply {
                val updatedSalary = Salary(getInt(ARG_ID), getInt(ARG_YEAR), getInt(ARG_MONTH), salary, expenses, income, memo)
                inputViewModel.update(updatedSalary)
            }
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.takeIf { it.containsKey(ARG_SALARY) }?.apply {
            val salaryInput = view.findViewById<EditText>(R.id.editText)
            salaryInput.setText(getInt(ARG_SALARY).toString())
        }
        arguments?.takeIf { it.containsKey(ARG_EXPENSES) }?.apply {
            val expensesInput = view.findViewById<EditText>(R.id.editText2)
            expensesInput.setText(getInt(ARG_EXPENSES).toString())
        }
        arguments?.takeIf { it.containsKey(ARG_EXTRAORDINARYINCOME) }?.apply {
            val incomeInput = view.findViewById<EditText>(R.id.editText3)
            incomeInput.setText(getInt(ARG_EXTRAORDINARYINCOME).toString())
        }
        arguments?.takeIf { it.containsKey(ARG_MEMO) }?.apply {
            val memoInput = view.findViewById<EditText>(R.id.memoText)
            memoInput.setText(getString(ARG_MEMO))
        }
    }
}