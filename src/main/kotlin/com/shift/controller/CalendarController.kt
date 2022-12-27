package com.shift.controller

import com.shift.domain.model.bean.CalendarBean
import com.shift.domain.service.CalendarService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping

/**
 * @author saito
 *
 */
@Controller
class CalendarController : BaseController() {

    @Autowired
    private lateinit var calendarService: CalendarService


    /**
     * [Controller] カレンダー表示機能 (/calendar)
     *
     * @param model
     * @return String
     */
    @RequestMapping("/calendar")
    fun calendar(model: Model): String {

        // Service
        val calendarBean: CalendarBean = calendarService.calendar()
        model.addAttribute("bean", calendarBean)
        return "calendar"
    }
}