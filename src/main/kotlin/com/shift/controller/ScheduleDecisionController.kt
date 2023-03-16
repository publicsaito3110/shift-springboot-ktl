package com.shift.controller

import com.shift.common.Const
import com.shift.domain.model.bean.ScheduleDecisionBean
import com.shift.domain.service.HomeService
import com.shift.domain.service.ScheduleDecisionService
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
class ScheduleDecisionController : BaseController() {

    @Autowired
    private lateinit var homeService: HomeService

    @Autowired
    private lateinit var scheduleDecisionService: ScheduleDecisionService


    /**
     * [Controller] 確定スケジュール表示機能画面 (/schedule-decision)
     *
     * @param ym RequestParameter 年月
     * @param authentication Authentication ユーザ情報
     * @param model Model ThymeleafのUI
     * @return String Viewのパス
     */
    @RequestMapping("/schedule-decision")
    fun scheduleDecision(@RequestParam(value = "ym", required = false) ym: String?, authentication: Authentication?, model: Model): String? {

        // Service
        val scheduleDecisionBean: ScheduleDecisionBean = scheduleDecisionService.scheduleDecision(ym)
        model.addAttribute("bean", scheduleDecisionBean)
        model.addAttribute("htmlColorArray", Const.SCHEDULE_HTML_CLASS_DISPLAY_COLOR_ARRAY)
        model.addAttribute("htmlBgColorArray", Const.SCHEDULE_HTML_CLASS_DISPLAY_BG_COLOR_ARRAY)
        // View
        return "view/schedule-decision/schedule-decision"
    }
}