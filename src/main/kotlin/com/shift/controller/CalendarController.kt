package com.shift.controller

import com.shift.common.Const
import com.shift.domain.model.bean.CalendarBean
import com.shift.domain.service.CalendarService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

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
    fun calendar(@RequestParam(value="ym",required=false) ym: String?, authentication: Authentication, model: Model): String {

        // ログインしているユーザのIDを取得
        val loginUser: String = authentication.name

        // Service
        val calendarBean: CalendarBean = calendarService.calendar(ym, loginUser)
        model.addAttribute("bean", calendarBean)
        model.addAttribute("scheduleHtmlColorArray", Const.SCHEDULE_HTML_CLASS_DISPLAY_COLOR_ARRAY)
        model.addAttribute("scheduleHtmlBgColorArray", Const.SCHEDULE_HTML_CLASS_DISPLAY_BG_COLOR_ARRAY)
        // View
        return "view/calendar/calendar"
    }
}