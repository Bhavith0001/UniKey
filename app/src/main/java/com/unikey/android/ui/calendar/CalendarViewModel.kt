package com.unikey.android.ui.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unikey.android.database.cloud.CloudStorage
import com.unikey.android.objects.Event
import com.unikey.android.objects.EventDate
import kotlinx.coroutines.launch

class CalendarViewModel : ViewModel() {

    private var  calenderHelper: CalendarHelper = CalendarHelper.newInstance()

    val dataStore = CloudStorage()

    val eventsList = dataStore.eventsList

    init {
//        eventsList.value = emptyList()
    }

    fun nextMonth(){
        calenderHelper.nextMonth()
    }

    fun previousMonth(){
        calenderHelper.previousMonth()
    }

    fun getSelectedMonthYear(): String {
        return calenderHelper.formatDate("MMMM yyyy")
    }

    fun getEventsList(date: String): List<Event>{
        val eventList = mutableListOf<Event>()
            for(event in eventsList.value!!){
                if (event.date.toString().equals(date, true) && event.monthYear.equals(getSelectedMonthYear(), true))
                    eventList.add(event)
                else
                    continue
            }
        return eventList
    }

    fun getDaysArray(eventsList: List<Event>): ArrayList<EventDate>{
        val tempList: MutableList<String> = mutableListOf()
        for (event in eventsList){
            tempList.add("${event.date} ${event.monthYear}")
        }
        return calenderHelper.daysInMonthArray(tempList)
    }

    fun getMonthEvents(): List<Event> {
        val eventList = mutableListOf<Event>()
        for(event in eventsList.value!!){
            if (event.monthYear == getSelectedMonthYear()){
                eventList.add(event)
            }
        }
        return eventList
    }

    init {
        getEvents()
    }

    private fun getEvents(){
        viewModelScope.launch {
            dataStore.getEvents()
        }

    }


}