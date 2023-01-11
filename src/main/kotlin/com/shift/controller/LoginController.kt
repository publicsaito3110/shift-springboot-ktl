package com.shift.controller

import com.shift.domain.service.LoginService
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
class LoginController : BaseController() {

    @Autowired
    private lateinit var loginService: LoginService


    /**
     * [Controller] ログイン画面 (/login)
     *
     * @param model
     * @return String
     */
    @RequestMapping("/login")
    fun login(model: Model): String {

        // View
        return "view/login/login"
    }


    /**
     * [Controller] ログイン認証機能 (/login/auth)
     *
     * @param model
     * @return String
     */
    @RequestMapping("/login/auth")
    fun loginAuth(authentication: Authentication, model: Model): String {

        // ログインしているユーザのIDを取得
        val loginUser: String = authentication.name

        // Service
        loginService.loginAuth(loginUser)
        // View
        return "redirect:/calendar"
    }
}