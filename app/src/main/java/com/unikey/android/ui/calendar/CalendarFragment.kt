package com.unikey.android.ui.calendar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.transition.MaterialSharedAxis
import com.unikey.android.databinding.FragmentCalendarBinding
import com.unikey.android.setTargetSharedAxisAnim
import com.unikey.android.setUp

class CalendarFragment : Fragment() {

    private lateinit var binding: FragmentCalendarBinding
    private lateinit var eventsAdapter: CalendarEventsAdapter

    private val viewModel: CalendarViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTargetSharedAxisAnim(MaterialSharedAxis.X)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        eventsAdapter = CalendarEventsAdapter()
        binding.calenderEventsList.adapter = eventsAdapter

        setMonthView()

        binding.previousMonth.setOnClickListener{
            viewModel.previousMonth()
            setMonthView()
        }

        binding.nextMonth.setOnClickListener {
            viewModel.nextMonth()
            setMonthView()
        }

        binding.appBar.toolBar.setUp(this, "Calendar")

    }

    private fun setMonthView() {

        binding.monthYearTV.text = viewModel.getSelectedMonthYear()
//        val daysInMonth = viewModel.getDaysArray()

        val adapter = CalendarAdapter{ date ->
            val eventList = viewModel.getEventsList(date)
            eventsAdapter.submitList(eventList)
        }

        binding.calenderView.adapter = adapter
//        adapter.submitList(daysInMonth)

        viewModel.eventsList.observe(viewLifecycleOwner){
            val daysInMonth = viewModel.getDaysArray(it)
            adapter.submitList(daysInMonth)
            eventsAdapter.submitList(viewModel.getMonthEvents())
        }

    }

//    fun createEventDates(eventsList: List<Event>): Boolean{
//        val tempList: MutableList<EventDate> = mutableListOf()
//        for (event in eventsList){
//            tempList.add(EventDate())
//        }
//    }


}