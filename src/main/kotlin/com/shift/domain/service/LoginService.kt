package com.shift.domain.service

import com.shift.common.CommonUtil
import com.shift.common.Const
import com.shift.domain.model.bean.AccountBean
import com.shift.domain.model.bean.LoginAuthBean
import com.shift.domain.model.entity.UserEntity
import com.shift.domain.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import javax.servlet.http.HttpSession


/**
 * @author saito
 *
 */
@Service
@Transactional
class LoginService: BaseService() {

    @Autowired
    private lateinit var httpSession: HttpSession

    @Autowired
    private lateinit var userRepository: UserRepository

    private var errorMassage: String? = null


    /**
     * [Service] ログイン情報設定機能 (/login/auth)
     *
     * @param loginUser ログインしているユーザID
     * @return LoginAuthBean
     */
    open fun loginAuth(loginUser: String?): LoginAuthBean {

        // メールからログインユーザを取得する
        val userEntity: UserEntity? = selectUser(loginUser)
        // 取得したユーザがログイン可能ユーザかどうか判定する
        val isLogin: Boolean = isCheckLoginUser(userEntity)
        // ログインが認証されたとき、セッションをセットする
        if (isLogin) {
//            val dmUnreadCountDto: DmUnreadCountDto = selectUnreadMsgCountByLoginUser(loginUser) TODO DM未読処理
            generateSession(userEntity)
        }
}