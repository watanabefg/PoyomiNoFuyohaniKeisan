package com.zubopoyo.fuyohani.ui.roster

import android.app.usage.UsageEvents
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.events.calendar.utils.EventsCalendarUtil
import com.events.calendar.utils.EventsCalendarUtil.MULTIPLE_SELECTION
import com.events.calendar.views.EventsCalendar
import com.zubopoyo.fuyohani.R
import kotlinx.android.synthetic.main.fragment_roster.*
import java.util.*


class RosterFragment : Fragment(), EventsCalendar.Callback  {
    //lateinit var calendarView : CalendarView
    //private val threeMonthsCriteriaList : ArrayList<BaseCriteria> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_roster, container, false)

        /*
        calendarView = root.findViewById(R.id.calendar_view)
        root.findViewById<Button>(R.id.workdayButton).setOnClickListener {
            calendarView.selectedDays.forEach {
                calendarView.selectionManager.toggleDay(it)
            }
        }
        */

        /*
        val compactCalendarView : CompactCalendarView = root.findViewById(R.id.compactcalendar_view);
        compactCalendarView.setLocale(TimeZone.getTimeZone("Asia/Tokyo"), Locale.JAPANESE)
        */

        val today = Calendar.getInstance()
        val start = Calendar.getInstance()
        start.add(Calendar.YEAR, -1)
        val end = Calendar.getInstance()
        end.add(Calendar.YEAR, 2)

        val eventsCalendar = root.findViewById<EventsCalendar>(R.id.eventsCalendar)
        eventsCalendar.setSelectionMode(MULTIPLE_SELECTION)
            .setToday(today)
            .setMonthRange(start, end)
            .setWeekStartDay(Calendar.SUNDAY, false)
            .setIsBoldTextOnSelectionEnabled(true)
            .setCallback(this)
            .build()

        val calendar = Calendar.getInstance()
        calendar.clear()
        calendar.set(2020, 4, 8, 9, 45, 10)
        eventsCalendar.addEvent(calendar)
        /*
        val ev1 = Event(
            Color.GREEN,
            calendar.timeInMillis,
            "Some extra data that I want to store."
        )*/

        val calendar2 = Calendar.getInstance()
        calendar2.clear()
        calendar2.set(2020,4,7,9,10,0)
        eventsCalendar.addEvent(calendar2)

        /*
        val ev2 =
            Event(
                Color.GREEN,
                calendar2.timeInMillis
            )
        val ev : List<Event> = listOf(ev1, ev2)
        //compactCalendarView.addEvents(ev)
        calendarView
         */



        // Query for events on Sun, 07 Jun 2015 GMT.
        // Time is not relevant when querying for events, since events are returned by day.
        // So you can pass in any arbitary DateTime and you will receive all events for that day.
        // Query for events on Sun, 07 Jun 2015 GMT.
        // Time is not relevant when querying for events, since events are returned by day.
        // So you can pass in any arbitary DateTime and you will receive all events for that day.
        //val events: List<Event> =
        //    compactCalendarView.getEvents(1588736076071L) // can also take a Date object
        //val events = compactCalendarView.getEventsForMonth(Date())

        // events has size 2 with the 2 events inserted previously
        //Log.d("testCalendar", "Events: $events")

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