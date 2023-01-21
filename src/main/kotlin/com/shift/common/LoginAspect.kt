package com.shift.common

import com.shift.domain.model.bean.collection.AccountBean
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpSession


@Aspect
@Component
class LoginAspect {

    @Autowired
    private lateinit var httpSession: HttpSession

    @Autowired
    private lateinit var requestMappingHandler: RequestMappingHandlerMapping


    /**
     * セッション判定処理 (AOP)
     *
     * Controller実行前にセッション及びURI制限を行う<br>
     * セッションが未保持のとき、<br></br>
     * ・セッションの未保持が許容されていないURI: ログイン画面へ強制的に遷移<br>
     * ・セッションの未保持が許容されているURI: そのまま実行<br>
     * セッションが保持されているとき、<br>
     * ・ログイン関連のURI: 強制的にホーム画面へ遷移<br>
     * ・ログイン関連以外のURI: そのまま実行
     *
     * @param joinPoint ProceedingJoinPoint
     * @return Object
     */
    @Around("execution(* *..*Controller.*(..))")
    @Throws(Throwable::class)
    fun executeSession(joinPoint: ProceedingJoinPoint): Any? {

        val targetControllerObj: Any = joinPoint.target

        // ログインセッションを取得
        val accountBean: AccountBean? = httpSession.getAttribute(Const.SESSION_KEYWORD_ACCOUNT_BEAN) as AccountBean

        // 現在のURIを取得
        val requset: HttpServletRequest =
            (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes).request
        val nowUri: String = requset.requestURI

        // ログインセッションが存在しないとき
        if (accountBean == null) {

            // ログインセッションの未保持を許容するURI
            val sessionIgnoreUriArray: Array<String> = arrayOf(
                "/",
                "/login",
                "/login/auth",
                "/login/forgot-password",
                "/login/forgot-password/send",
                "/login/forgot-password/reset",
                "/login/forgot-password/reset/auth",
                "/login/forgot-password/reset/modify",
                "/login/forgot-id",
                "/login/forgot-id/send",
                "/login/error",
                "/logout"
            )

            //sessionIgnoreUriArrayに含まれていないとき
//            if (!Arrays.asList(sessionIgnoreUriArray).contains(nowUri)) {
//
//                //ログイン画面へ強制的に遷移
//                val modelAndView = ModelAndView()
//                modelAndView.viewName = "redirect:/login/error"
//                return modelAndView
//            }
//            return joinPoint.proceed()
//        }

            //ログインに関するURI
            val loginUriArray = arrayOf(
                "/login",
                "/login/auth",
                "/login/forgot-password",
                "/login/forgot-password/send",
                "/login/forgot-password/reset",
                "/login/forgot-password/reset/auth",
                "/login/forgot-password/reset/modify",
                "/login/forgot-id",
                "/login/forgot-id/send",
                "/login/error"
            )

            //ログインに関するURIにアクセスかつSessionが存在しているとき
//        if (Arrays.asList(loginUriArray).contains(nowUri) && accountBean != null) {
//
//            //ホーム画面へ強制的に遷移
//            val modelAndView = ModelAndView()
//            modelAndView.viewName = "redirect:/home"
//            return modelAndView
//        }
        }
            //Controllerの処理を実行し、オブジェクトを返す
            return joinPoint.proceed()
    }
}