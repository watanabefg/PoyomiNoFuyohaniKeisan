package com.zubopoyo.fuyohani.ui.roster

import android.app.usage.UsageEvents
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import com.applikeysolutions.cosmocalendar.selection.criteria.BaseCriteria
import com.applikeysolutions.cosmocalendar.view.CalendarView
import com.zubopoyo.fuyohani.R
import java.util.*
import kotlin.collections.ArrayList


class RosterFragment : Fragment(), RadioGroup.OnCheckedChangeListener  {
    lateinit var calendarView : CalendarView
    private val threeMonthsCriteriaList : ArrayList<BaseCriteria> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_roster, container, false)
        calendarView = root.findViewById(R.id.calendar_view)
        root.findViewById<Button>(R.id.workdayButton).setOnClickListener {
            calendarView.selectedDays.forEach {
                calendarView.selectionManager.toggleDay(it)
            }
        }

        /*
        threeMonthsCriteriaList.add(CurrentMonthCriteria())
        threeMonthsCriteriaList.add(NextMonthCriteria())
        threeMonthsCriteriaList.add(PreviousMonthCriteria())

        if (calendarView.selectionManager is MultipleSelectionManager) {
            (calendarView.selectionManager as MultipleSelectionManager).addCriteriaList(
                threeMonthsCriteriaList
            )
        }
        calendarView.update()
        */

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        TODO("Not yet implemented")
    }

}