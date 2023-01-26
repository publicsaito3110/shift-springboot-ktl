package com.shift.domain.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.servlet.http.HttpSession


/**
 * @author saito
 *
 */
@Service
@Transactional
class HomeService: BaseService() {

    @Autowired
    private lateinit var httpSession: HttpSession


    /**
     * [Service] ログアウト機能 (/logout)
     *
     * @param
     * @return
     */
    fun logout(): Boolean {

        // セッションを完全に削除
        removeSession()
        return true
    }


    /**
     * セッション削除処理
     *
     * 保持しているセッションを完全に削除する
     *
     * @param
     * @return Boolean セッションの削除の判定<br>
     * true: セッションの削除に成功したとき<br>
     * false: セッションの削除に失敗したとき
     */
    private fun removeSession(): Boolean {

        // セッションを完全に削除
        httpSession.invalidate()
        return true
    }
}