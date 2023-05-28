package com.example.plantogether.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.plantogether.activity.EventInfoActivity
import com.example.plantogether.databinding.FragmentCalendarBinding
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.format.TitleFormatter
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Locale

class CalendarFragment : Fragment() {

    private var binding: FragmentCalendarBinding?=null

    private var materialCalendarView: MaterialCalendarView?= null

    private var selectedDate: LocalDate?= null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCalendarBinding.inflate(layoutInflater, container, false)
        return binding!!.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        materialCalendarView = binding!!.calendarView
        binding!!.apply {
            val today = CalendarDay.today()

            calendarView.apply {
                // 휴무일 지정을 위한 Decorator 설정
                //addDecorator(DayDisableDecorator(disabledDates, today))
                // 요일을 지정하귀 위해 {"월", "화", ..., "일"} 배열을 추가한다.
                setWeekDayLabels(arrayOf("월", "화", "수", "목", "금", "토", "일"))
                // 달력 상단에 `월 년` 포맷을 수정하기 위해 TitleFormatter 설정
                //setTitleFormatter(MyTitleFormatter())

                //DateFormatTitleFormatter()

                setOnDateChangedListener { widget, date, selected ->
                    //Toast.makeText(context, "$date", Toast.LENGTH_SHORT).show()
                    // 여기에다가 dialog 구현해주시면 될 것 같아요. 지금 EditInfo로 넘어가요
                    val intent = Intent(activity, EventInfoActivity::class.java)
                    intent.putExtra("date", "${date.year}년 ${date.month}월 ${date.day}일")
                    startActivity(intent)
                }
            }
        }
    }


   class MyTitleFormatter : TitleFormatter {
        override fun format(day: CalendarDay?): CharSequence {
            val simpleDateFormat =
                SimpleDateFormat("yyyy년 MM월", Locale.US) //"February 2016" format

            return simpleDateFormat.format(Calendar.getInstance().getTime())
        }

    }

    class DayDisableDecorator : DayViewDecorator {
        private var dates = HashSet<CalendarDay>()
        private var today: CalendarDay

        constructor(dates: HashSet<CalendarDay>, today: CalendarDay) {
            this.dates = dates
            this.today = today
        }

        override fun shouldDecorate(day: CalendarDay): Boolean {
            // 휴무일 || 이전 날짜
            return dates.contains(day) || day.isBefore(today)
        }

        override fun decorate(view: DayViewFacade?) {
            view?.let { it.setDaysDisabled(true) }
        }
    }


}