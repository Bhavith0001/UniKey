package com.unikey.android.ui.calendar

import android.util.Log
import com.unikey.android.datesOfEvent
import com.unikey.android.objects.Event
import com.unikey.android.objects.EventDate
import java.text.SimpleDateFormat
import java.util.*

class CalendarHelper {

    private lateinit var calendar: Calendar
    private lateinit var dateFormat: SimpleDateFormat
    private lateinit var monthYear: String


    private fun currentMonthYear(): String {
        val formatter = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        val calendar1 = Calendar.getInstance()
        return formatter.format(calendar1.time)
    }

    fun formatDate(pattern: String?): String {
        dateFormat = SimpleDateFormat(pattern, Locale.getDefault())
        return SimpleDateFormat(pattern, Locale.getDefault()).format(calendar.time)
    }

    private fun getCurrentDate(): Int {
        val calendar1 = Calendar.getInstance()
        return calendar1[Calendar.DAY_OF_MONTH]
    }

    fun daysInMonthArray(eventsDateMonth: List<String>): ArrayList<EventDate> {

        monthYear = formatDate("MMMM yyyy")

        val daysInMonthArray = ArrayList<EventDate>()
        calendar[Calendar.DAY_OF_MONTH] = 1
        val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        val dayOfWeek = calendar[Calendar.DAY_OF_WEEK]


        for (i in 2..42) {
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek) {
                daysInMonthArray.add(EventDate("", false, isTodays = false))
            } else {
                val date = (i - dayOfWeek)
                if (containsEvent(eventsDateMonth,date)) {
                    daysInMonthArray.add(EventDate(date.toString(), true, isTodaysDate(date)))
                } else {
                    daysInMonthArray.add(EventDate((i - dayOfWeek).toString(), false, isTodaysDate(date)))
                }
            }
        }
        Log.d("TAG", "Month Year: ${"$monthYear $daysInMonth"}")
        Log.d("TAG", "daysInMonthArray: $daysInMonthArray")
        Log.d("TAG", "\n")
        return daysInMonthArray
    }

    private fun isTodaysDate(date: Int): Boolean{
        return monthYear == currentMonthYear() && date.toString() == getCurrentDate().toString()
    }

    private fun containsEvent(eventsDateMonth: List<String>,date: Int): Boolean {
        val dateMonthYear = "$date $monthYear"
        return eventsDateMonth.contains(dateMonthYear)
    }

    fun previousMonth() {
        calendar.add(Calendar.MONTH, -1)
    }

    fun nextMonth() {
        calendar.add(Calendar.MONTH, 1)
    }

    companion object {
        fun newInstance(): CalendarHelper {
            val instance = CalendarHelper()
            instance.calendar = Calendar.getInstance()
            instance.dateFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
            instance.monthYear = instance.dateFormat.format(instance.calendar.time)
            return instance
        }
    }

}