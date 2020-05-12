package com.zubopoyo.fuyohani.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.zubopoyo.fuyohani.R
import com.zubopoyo.fuyohani.database.entity.Config
import com.zubopoyo.fuyohani.database.entity.Salary
import com.zubopoyo.fuyohani.ui.input.*
import org.w3c.dom.Text

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var homePagerAdapter: HomePagerAdapter
    private lateinit var viewPager: ViewPager

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        homeViewModel.allSalaries.observe(viewLifecycleOwner, Observer { salaries ->
            salaries?.let {
                var tmpYear = 0
                val years = mutableListOf<Int>()
                it.forEach {
                    if (tmpYear != it.year) {
                        tmpYear = it.year
                        years.add(it.year)
                    }
                }
                homePagerAdapter.setSalaries(it)
                homePagerAdapter.setYears(years)
            }
        })
        homeViewModel.config.observe(viewLifecycleOwner, Observer { configs ->
            configs?.let {
                homePagerAdapter.setConfigs(it)
            }
        })

        val root = inflater.inflate(R.layout.fragment_home, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        homePagerAdapter = HomePagerAdapter(childFragmentManager)
        viewPager = view.findViewById(R.id.pager)
        viewPager.adapter = homePagerAdapter
        val tabLayout = view.findViewById<TabLayout>(R.id.tab_layout)
        tabLayout.setupWithViewPager(viewPager)
    }

}

/*
  DBからは入力がある月の分だけ、直近の月がなければそれも表示する
 */
class HomePagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
    private var salaries = emptyList<Salary>() // Cached copy of salaries
    private var years = mutableListOf<Int>()
    private lateinit var config : Config

    override fun getItem(position: Int): Fragment {
        val fragment = HomeMonthFragment()
        val thisSalaries = salaries.filter {
            it.year == years[position]
        }
        val bundle = Bundle()
        var totalSalary = 0
        var totalExpenses = 0
        var totalExtraordinaryIncome = 0
        var totalBalance: Int
        thisSalaries.forEach {
            totalSalary += it.salary
            totalExpenses += it.expenses
            totalExtraordinaryIncome += it.extraordinaryIncome
            bundle.apply {
                putInt(ARG_SALARY + "${it.month}", it.salary)
                putInt(ARG_EXPENSES + "${it.month}", it.expenses)
                putInt(ARG_EXTRAORDINARYINCOME + "${it.month}", it.extraordinaryIncome)
            }
        }
        totalBalance = config.cappedAmount - totalSalary - totalExtraordinaryIncome
        bundle.apply {
            putInt(ARG_TOTAL, totalSalary + totalExpenses + totalExtraordinaryIncome)
            putInt(ARG_TOTALEXPENSES, totalExpenses)
            putInt(ARG_TOTALINCOME, totalSalary + totalExtraordinaryIncome)
            putInt(ARG_TOTALBALANCE, totalBalance)
        }
        fragment.arguments = bundle

        return fragment
    }

    internal fun setConfigs(config: Config) {
        this.config = config
        notifyDataSetChanged()
    }

    internal fun setSalaries(salaries: List<Salary>) {
        this.salaries = salaries
        notifyDataSetChanged()
    }

    internal fun setYears(years: MutableList<Int>) {
        this.years = years
        notifyDataSetChanged()
    }

    override fun getPageTitle(position: Int): CharSequence {
        return "${years[position]}年"
    }

    override fun getCount(): Int {
        return years.size
    }
}

private const val ARG_SALARY = "salary"
private const val ARG_EXPENSES = "expenses"
private const val ARG_EXTRAORDINARYINCOME = "extraordinaryIncome"
private const val ARG_TOTAL = "total"
private const val ARG_TOTALEXPENSES = "totalexpenses"
private const val ARG_TOTALINCOME = "totalincome"
private const val ARG_TOTALBALANCE = "totalbalance"

// Instances of this class are fragments representing a single
// object in our collection.
class HomeMonthFragment : Fragment() {
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        return inflater.inflate(R.layout.fragment_homecontents, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.takeIf { it.containsKey(ARG_TOTAL) }?.apply {
            view.findViewById<TextView>(R.id.total).text = getInt(ARG_TOTAL).toString()
        }
        arguments?.takeIf { it.containsKey(ARG_TOTALEXPENSES) }?.apply {
            view.findViewById<TextView>(R.id.totalExpenses).text = getInt(ARG_TOTALEXPENSES).toString()
        }
        arguments?.takeIf { it.containsKey(ARG_TOTALINCOME) }?.apply {
            view.findViewById<TextView>(R.id.totalIncome).text = getInt(ARG_TOTALINCOME).toString()
        }
        arguments?.takeIf { it.containsKey(ARG_TOTALBALANCE) }?.apply {
            view.findViewById<TextView>(R.id.totalBalance).text = getInt(ARG_TOTALBALANCE).toString()
        }
        repeat((1..12).count()) {month ->
            arguments?.takeIf { it.containsKey(ARG_SALARY + month) }?.apply {
                val resViewNameSalary = "month" + month + "Salary"
                val viewId = resources.getIdentifier(resViewNameSalary, "id", context?.packageName)
                val monthSalary = view.findViewById<TextView>(viewId)
                monthSalary.text = getInt(ARG_SALARY + month).toString()

                val resViewNameExpenses = "month" + month + "Expenses"
                val viewIdExpenses = resources.getIdentifier(resViewNameExpenses, "id", context?.packageName)
                val monthExpenses = view.findViewById<TextView>(viewIdExpenses)
                monthExpenses.text = getInt(ARG_EXPENSES + month).toString()

                val resViewNameExtraordinaryIncome = "month" + month + "ExtraordinaryIncome"
                val viewIdExtraordinaryIncome = resources.getIdentifier(resViewNameExtraordinaryIncome, "id", context?.packageName)
                val monthExtraordinaryIncome = view.findViewById<TextView>(viewIdExtraordinaryIncome)
                monthExtraordinaryIncome.text = getInt(ARG_EXTRAORDINARYINCOME + month).toString()
            }
        }
    }

}