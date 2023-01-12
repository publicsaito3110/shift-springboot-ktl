package com.shift.controller

import com.shift.domain.service.LogoutService
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
class LogoutController : BaseController() {

    @Autowired
    private lateinit var logoutService: LogoutService


    /**
     * [Controller] ログアウト機能 (/logout)
     *
     * @param authentication
     * @param model
     * @return String
     */
    @RequestMapping("/logout")
    fun logout(authentication: Authentication, model: Model): String {

        // Service
        logoutService.logout()
        // View
        return "redirect:/login"
    }
}