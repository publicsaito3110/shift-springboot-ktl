package com.shift.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping

/**
 * @author saito
 *
 */
@Controller
class LoginController : BaseController() {


    /**
     * [Controller] ログイン画面 (/login)
     *
     * @param model
     * @return String
     */
    @RequestMapping("/login")
    fun login(model: Model): String {
        // View
        return "login/login"
    }


    /**
     * [Controller] ログイン認証機能 (/login/auth)
     *
     * @param model
     * @return String
     */
    @RequestMapping("/login/auth")
    fun loginAuth(model: Model): String {
        // View
        return "redirect:/calendar"
    }
}