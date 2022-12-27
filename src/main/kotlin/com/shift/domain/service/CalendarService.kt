package com.shift.domain.service

import com.shift.domain.model.bean.CalendarBean
import com.shift.domain.repository.ScheduleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * @author saito
 *
 */
@Service
@Transactional
class CalendarService {

    @Autowired
    private lateinit var scheduleRepository: ScheduleRepository


    /**
     * [Service] カレンダー表示機能 (/calendar)
     *
     * @param
     * @return CalendarBean
     */
    fun calendar(): CalendarBean {

        // Beanにセット
        val calendarBean: CalendarBean = CalendarBean()
        calendarBean.year = 2022
        return calendarBean
    }
}