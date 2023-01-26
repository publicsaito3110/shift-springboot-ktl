package com.shift.controller

import com.shift.domain.service.HomeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping

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
     * @param authentication
     * @param model
     * @return String
     */
    @RequestMapping("/home")
    fun home(authentication: Authentication, model: Model): String {

        // Service
        homeService.logout()
        // View
        return "redirect:/login"
    }
}