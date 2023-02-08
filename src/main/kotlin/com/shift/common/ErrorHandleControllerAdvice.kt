package com.shift.common

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import org.springframework.web.servlet.ModelAndView
import javax.servlet.http.HttpServletResponse


/**
 * @author saito
 *
 */
@ControllerAdvice
class ErrorHandleControllerAdvice {

    private var logger: Logger = LoggerFactory.getLogger(ErrorHandleControllerAdvice::class.java)


    /**
     * エラーハンドリング処理
     *
     * 例外発生情報(Exception)を取得してエラーログをlogファイルに出力後、エラー画面へ遷移させる<br>
     * また、Exceptionのスーパークラスも含む<br>
     * ただし、HttpStatusは500になる
     *
     * @param e Exception
     * @return ModelAndView
     */
    @ExceptionHandler(Exception::class)
    fun exceptionHandler(e: Exception): ModelAndView? {

        // Exceptionクラスを取得
        val exceptionClassName: String = e.toString()

        // Exception発生個所を取得
        val stackTraceArray = e.stackTrace
        val errorClassName = stackTraceArray[0].toString()

        // HttpStatusを500に設定し、Statusコード,Statusメッセージを取得
        val response: HttpServletResponse? = (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes?)!!.response
        response!!.status = HttpStatus.INTERNAL_SERVER_ERROR.value()
        val httpStatus = response.status.toString()
        val httpStatusMessage = HttpStatus.INTERNAL_SERVER_ERROR.name

        // エラーログをlogファイルに出力
        logger.info("↓↓ エラーログ ↓↓")
        logger.info("Exception: $exceptionClassName")
        logger.info("Error : $errorClassName")

        // ModelAndViewにエラー情報をセットし、error.htmlへ遷移
        val modelAndView = ModelAndView()
        modelAndView.addObject("exceptionClassName", exceptionClassName)
        modelAndView.addObject("errorClassName", errorClassName)
        modelAndView.addObject("httpStatus", httpStatus)
        modelAndView.addObject("httpStatusMessage", httpStatusMessage)
        modelAndView.addObject("errorMassage", "予期せぬエラーが発生しました")
        modelAndView.viewName = "error"
        return modelAndView
    }
}