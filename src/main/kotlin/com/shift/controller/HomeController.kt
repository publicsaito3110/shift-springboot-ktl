package com.shift.controller

import com.shift.common.Const
import com.shift.domain.model.bean.HomeBean
import com.shift.domain.service.HomeService
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
class HomeController : BaseController() {

    @Autowired
    private lateinit var homeService: HomeService


    /**
     * [Controller] ホーム画面表示機能 (/home)
     *
     * @param ymd 1週間カレンダーに表示したい日付 (Not required)
     * @param authentication Authentication
     * @param model Model
     * @return String
     */
    @RequestMapping("/home")
    fun home(@RequestParam(value="ymd",required=false) ymd: String?, authentication: Authentication, model: Model): String {

        // ログインユーザIDを取得
        val loginUser: String = authentication.name

        // Service
        val bean: HomeBean = homeService.home(ymd, loginUser)
        model.addAttribute("bean", bean)
        model.addAttribute("scheduleHtmlBgColorArray", Const.SCHEDULE_HTML_CLASS_DISPLAY_BG_COLOR_ARRAY)
        // View
        return "view/home/home"
    }
}