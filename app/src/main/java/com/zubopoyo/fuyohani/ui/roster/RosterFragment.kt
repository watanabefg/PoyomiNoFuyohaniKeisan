package com.zubopoyo.fuyohani.ui.roster

import android.app.AlertDialog
import android.app.usage.UsageEvents
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.events.calendar.utils.EventsCalendarUtil
import com.events.calendar.utils.EventsCalendarUtil.MULTIPLE_SELECTION
import com.events.calendar.views.EventsCalendar
import com.google.android.material.tabs.TabLayout
import com.zubopoyo.fuyohani.R
import com.zubopoyo.fuyohani.database.entity.Event
import com.zubopoyo.fuyohani.database.entity.Hourlypay
import kotlinx.android.synthetic.main.fragment_roster.*
import java.text.SimpleDateFormat
import java.time.YearMonth
import java.util.*
import java.util.stream.Collectors
import java.util.stream.Stream


class RosterFragment : Fragment(), EventsCalendar.Callback  {
    // lateinit var calendarView : CalendarView
    // private val threeMonthsCriteriaList : ArrayList<BaseCriteria> = arrayListOf()
    // private lateinit var workDayButton : Button
    // private lateinit var eventsCalendar : EventsCalendar
    private val events : MutableList<Calendar> = mutableListOf()
    private lateinit var rosterViewModel: RosterViewModel
    private lateinit var rosterPagerAdapter: RosterPagerAdapter
    private lateinit var viewPager: ViewPager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_roster, container, false)

        val today = Calendar.getInstance()
        val start = Calendar.getInstance()
        start.add(Calendar.YEAR, -1)
        val end = Calendar.getInstance()
        end.add(Calendar.YEAR, 2)

        /*
        eventsCalendar = root.findViewById<EventsCalendar>(R.id.eventsCalendar)
        eventsCalendar.setSelectionMode(MULTIPLE_SELECTION)
            .setToday(today)
            .setMonthRange(start, end)
            .setWeekStartDay(Calendar.SUNDAY, false)
            .setIsBoldTextOnSelectionEnabled(true)
            .setCallback(this)
            .build()

        workDayButton = root.findViewById<Button>(R.id.workdayButton)
         */

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rosterViewModel = ViewModelProvider(this).get(RosterViewModel::class.java)

        rosterPagerAdapter = RosterPagerAdapter(childFragmentManager)
        viewPager = view.findViewById(R.id.pager2)
        viewPager.adapter = rosterPagerAdapter
        val tabLayout = view.findViewById<TabLayout>(R.id.tab_layout2)
        tabLayout.setupWithViewPager(viewPager)
        tabLayout.getTabAt(rosterPagerAdapter.count-1)?.select()

        /*
        workDayButton.setOnClickListener {
            val dates = eventsCalendar.getDatesFromSelectedRange()
            dates.forEach{
                eventsCalendar.addEvent(it)
                // MEMO: category:0 = workDay
                val event = Event(it.get(Calendar.YEAR), it.get(Calendar.MONTH), it.get(Calendar.DATE), 0)
                rosterViewModel.insertEvent(event)
            }
        }
         */
    }

    override fun onDayLongPressed(selectedDate: Calendar?) {
        Log.e("LONG", "CLICKED")
    }

    override fun onDaySelected(selectedDate: Calendar?) {
        Log.e("SHORT", "CLICKED")
    }

    override fun onMonthChanged(monthStartDate: Calendar?) {
        Log.e("MON", "CHANGED")
    }

}

class RosterPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm)  {
    private val monthList : MutableList<Calendar> = mutableListOf()
    init {
        (-12 .. 1).forEach {
            val calendar = Calendar.getInstance()
            calendar.time = Date()
            calendar.add(Calendar.MONTH, it)
            monthList.add(calendar)
        }
    }

    /**
     * Return the number of views available.
     */
    override fun getCount(): Int = 13

    /**
     * Return the Fragment associated with a specified position.
     */
    override fun getItem(position: Int): Fragment {
        val fragment = RosterMonthFragment()
        val bundle = Bundle()
        bundle.apply {
            putInt(ARG_YEAR, monthList[position][Calendar.YEAR])
            putInt(ARG_MONTH, monthList[position][Calendar.MONTH]+1)
        }
        fragment.arguments = bundle
        return fragment
    }

    override fun getPageTitle(position: Int): CharSequence {
        return "${monthList[position][Calendar.YEAR]}年${monthList[position][Calendar.MONTH]+1}月"
    }

}

private const val ARG_YEAR = "year"
private const val ARG_MONTH = "month"


class RosterMonthFragment : Fragment() {
    private lateinit var rosterViewModel: RosterViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        rosterViewModel = ViewModelProvider(this).get(RosterViewModel::class.java)

        return inflater.inflate(R.layout.fragment_rostercontents, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var year = 0
        var month = 0

        rosterViewModel.allEvents.observe(viewLifecycleOwner, androidx.lifecycle.Observer {events ->
            val thisEvents = events?.filter {
                it.year == arguments?.getInt(ARG_YEAR) && it.month == arguments?.getInt(ARG_MONTH)
            }
            var timeSummary = 0f
            var feeSummary = 0
            thisEvents?.forEach {
                val resViewNameTime = "day" + it.day + "_time"
                val resViewNameFee = "day" + it.day + "_fee"
                val editTimeViewId = resources.getIdentifier(resViewNameTime, "id", context?.packageName)
                val editFeeViewId = resources.getIdentifier(resViewNameFee, "id", context?.packageName)
                val editTimeView = view.findViewById<EditText>(editTimeViewId)
                val editFeeView = view.findViewById<CheckBox>(editFeeViewId)
                editTimeView.setText(it.workinghours.toString())
                timeSummary += it.workinghours
                editFeeView.isChecked = it.fee == 1
                if (it.fee == 1){
                    feeSummary++
                }
            }
            view.findViewById<TextView>(R.id.feeDayNumber).text = feeSummary.toString()
            view.findViewById<TextView>(R.id.timeSummary).text = timeSummary.toString()

        })
        rosterViewModel.allHourlypay.observe(viewLifecycleOwner, androidx.lifecycle.Observer {hourlypay ->
            val thisHourlypay = hourlypay?.filter {
                it.year == arguments?.getInt(ARG_YEAR) && it.month == arguments?.getInt(ARG_MONTH)
            }
            thisHourlypay?.forEach {
                val editHourlypayView = view.findViewById<EditText>(R.id.hourlypay)
                val editTransportationView = view.findViewById<EditText>(R.id.transportation)
                val summaryView = view.findViewById<TextView>(R.id.summary)
                editHourlypayView.setText(it.hourlypay.toString())
                editTransportationView.setText(it.transportation.toString())

                var feeSummary = 0
                if (view.findViewById<TextView>(R.id.feeDayNumber).text.toString() != ""){
                    feeSummary = view.findViewById<TextView>(R.id.feeDayNumber).text.toString().toInt()
                }
                var timeSummary = 0f
                if (view.findViewById<TextView>(R.id.timeSummary).text.toString() != "") {
                    timeSummary = view.findViewById<TextView>(R.id.timeSummary).text.toString().toFloat()
                }
                summaryView.text = (feeSummary * it.transportation + timeSummary * it.hourlypay).toInt().toString()
            }
        })

        val savebutton = view.findViewById<Button>(R.id.saveButton2)
        savebutton.setOnClickListener {buttonView ->
            arguments?.takeIf { it.containsKey(ARG_YEAR) }?.apply {
                year = getInt(ARG_YEAR)
                month = getInt(ARG_MONTH)

                (1..31).forEach { day ->
                    val resViewNameTime = "day" + day + "_time"
                    val resViewNameFee = "day" + day + "_fee"
                    val editTimeViewId = resources.getIdentifier(resViewNameTime, "id", context?.packageName)
                    val editFeeViewId = resources.getIdentifier(resViewNameFee, "id", context?.packageName)
                    val editTimeView = view.findViewById<EditText>(editTimeViewId)
                    val editFeeView = view.findViewById<CheckBox>(editFeeViewId)

                    // year, month, day, workinghours, feeを入れる
                    if (editTimeView.text.toString() != "") {
                        val workinghours = editTimeView.text.toString().toFloat()
                        val fee = if (editFeeView.isChecked) 1 else 0
                        val event = Event(year, month, day, workinghours, fee)
                        rosterViewModel.insertEvent(event)
                    }
                }

                // 時給の保存
                val editHourlypayView = view.findViewById<EditText>(R.id.hourlypay)
                val editTransportationView = view.findViewById<EditText>(R.id.transportation)
                if (editHourlypayView.text.toString() != "" && editTransportationView.text.toString() != "") {
                    val hourlypay = Hourlypay(year, month, editHourlypayView.text.toString().toInt(), editTransportationView.text.toString().toInt())
                    rosterViewModel.insertHourlypay(hourlypay)
                }

                // Toast
                Toast.makeText(
                    context,
                    "保存しました",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        val deleteButton = view.findViewById<Button>(R.id.deleteButton)
        deleteButton.setOnClickListener {
            arguments?.takeIf { it.containsKey(ARG_YEAR) }?.apply {
                year = getInt(ARG_YEAR)
                month = getInt(ARG_MONTH)

                AlertDialog.Builder(activity) // FragmentではActivityを取得して生成
                    .setTitle("データを削除して良いですか？")
                    .setMessage("${year}年${month}月の勤務表データを削除します。")
                    .setPositiveButton("はい", { dialog, which ->
                        // Yesが押された時の挙動
                        rosterViewModel.deleteMonth(year, month)
                        // 表示もなくす
                        (1..31).forEach { day ->
                            val resViewNameTime = "day" + day + "_time"
                            val resViewNameFee = "day" + day + "_fee"
                            val editTimeViewId = resources.getIdentifier(resViewNameTime, "id", context?.packageName)
                            val editFeeViewId = resources.getIdentifier(resViewNameFee, "id", context?.packageName)
                            val editTimeView = view.findViewById<EditText>(editTimeViewId)
                            val editFeeView = view.findViewById<CheckBox>(editFeeViewId)
                            editTimeView.setText("")
                            editFeeView.isChecked = false
                        }
                        // Toast
                        Toast.makeText(
                            context,
                            "削除しました",
                            Toast.LENGTH_LONG
                        ).show()
                    })
                    .setNegativeButton("いいえ", { dialog, which ->
                        // Noが押された時の挙動
                    })
                    .show()


            }
        }
    }
}