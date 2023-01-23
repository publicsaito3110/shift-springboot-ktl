package com.shift.common

import com.shift.domain.model.bean.collection.AccountBean
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpSession


@Aspect
@Component
class LoginAspect {

    @Autowired
    private lateinit var httpSession: HttpSession


    /**
     * セッション判定処理 (AOP)
     *
     * Controller実行前にセッション及びURI制限を行う<br>
     * セッションが未保持のとき、<br>
     * ・セッションの未保持が許容されていないURI: ログイン画面へ強制的に遷移<br>
     * ・セッションの未保持が許容されているURI: そのまま実行<br>
     * セッションが保持されているとき、<br>
     * ・ログイン関連のURI: 強制的にホーム画面へ遷移<br>
     * ・ログイン関連以外のURI: そのまま実行
     *
     * @param joinPoint ProceedingJoinPoint
     * @return Object Controller実行時の戻り値
     */
    @Around("execution(* *..*Controller.*(..))")
    @Throws(Throwable::class)
    fun executeSession(joinPoint: ProceedingJoinPoint): Any? {

        // リクエストを受け取ったControllerを取得
        val controllerObj: Any = joinPoint.target

        // エラーハンドリングするControllerのとき、Controllerの処理を実行
        if (controllerObj is BasicErrorController) {
            return joinPoint.proceed()
        }

        // セッションを取得
        val accountBean: AccountBean? = httpSession.getAttribute(Const.SESSION_KEYWORD_ACCOUNT_BEAN) as AccountBean

        // 現在のURIを取得
        val request: HttpServletRequest = (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes?)!!.request
        val nowUri: String = request.requestURI

        // セッションが存在しないとき
        if (accountBean == null) {

            // セッションの未保持を許容するURI
            val patternSessionIgnoreUri: String = "/|/logout|/login|^/login/.*$"

            // 現在のURIがセッション未保持を許容するURIに含まれていないとき、ログイン画面へ強制的に遷移
            if (!CommonUtil.isSuccessValidation(nowUri, patternSessionIgnoreUri)) {
                return "redirect:/login/error"
            }
            return joinPoint.proceed()
        }

        // ログインに関するURI
        val patternLoginUri: String = "/login|^/login/.*\$"

        // ログインに関するURIにアクセスかつSessionが存在しているとき
        if (CommonUtil.isSuccessValidation(nowUri, patternLoginUri)) {

            // ホーム画面へ強制的に遷移
            return "redirect:/home"
        }

        // Controllerの処理を実行
        return joinPoint.proceed()
    }
}