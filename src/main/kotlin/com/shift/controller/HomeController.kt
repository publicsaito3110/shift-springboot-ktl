package com.shift.controller

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
     * @param authentication
     * @param model
     * @return String
     */
    @RequestMapping("/home")
    fun home(@RequestParam(value="ymd",required=false) ymd: String, authentication: Authentication, model: Model): String {

        // ログインユーザIDを取得
        val loginUser: String = authentication.name

        // Service
        homeService.home(ymd, loginUser)
        // View
        return "view/home"
    }
}