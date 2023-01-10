package com.shift.domain.service

import com.shift.common.CommonUtil
import com.shift.common.Const
import com.shift.domain.model.bean.collection.AccountBean
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

        // ユーザIDからログインユーザを取得する
        val userEntity: UserEntity? = selectUser(loginUser)
        // 取得したユーザがログイン可能ユーザかどうか判定する
        val isLogin: Boolean = isCheckLoginUser(userEntity)
        // ログインが認証されたとき、セッションをセットする
        if (isLogin) {
//            val dmUnreadCountDto: DmUnreadCountDto = selectUnreadMsgCountByLoginUser(loginUser) TODO DM未読処理
            generateSession(userEntity)
        }

        // Beanにセット
        val loginAuthBean: LoginAuthBean = LoginAuthBean()
        loginAuthBean.isLogin = isLogin
        loginAuthBean.errorMassage = errorMassage
        return loginAuthBean
    }


    /**
     * セッション処理
     *
     * ログイン情報からログイン済みを識別するセッションをセットする<br>
     * ただし、isLoginがfalse(ログインが認証されていない)ときはセッションをセットしない
     *
     * @param userEntity Repositoryから取得したユーザ
     * @return Boolean セッションのセットの判定<br>
     * true: セッションのセットに成功したとき<br>
     * false: セッションのセットに失敗したとき
     */
    private fun generateSession(userEntity: UserEntity?): Boolean {

        // ユーザが存在しないとき
        if (userEntity == null) {
            return false
        }

        // セッションを完全に削除
        httpSession.invalidate()

        // アカウント情報をセッションをセット
        val accountBean: AccountBean = AccountBean(userEntity)
        httpSession.setAttribute(Const.SESSION_KEYWORD_ACCOUNT_BEAN, accountBean)
        return true
    }


    /**
     * ログイン可能ユーザ判定処理
     *
     * ログイン情報(userEntity)を基に取得したユーザーがログイン可能であるか判定する<br>
     * 一致するユーザーがいないまたは退職済みのときはログインが不可になる<br>
     * また、ログインが不可であるときはエラーメッセージをフィールド(errorMassage)にセットする
     *
     * @param userEntity Repositoryから取得したユーザ
     * @return boolean<br>
     * true: ログイン情報から一致するユーザーかつ退職済みでないユーザであるとき<br>
     * false: ログイン情報から一致するユーザがいないまたは退職済みであるとき
     */
    private fun isCheckLoginUser(userEntity: UserEntity?): Boolean {

        // ログイン情報からユーザーを取得できなかったとき
        if (userEntity == null) {

            // エラーメッセージをセットし、falseを返す
            errorMassage = "IDまたはパスワードが違います"
            return false
        }

        // 退職済みだったとき
        if (userEntity.delFlgFormatTF()) {

            // エラーメッセージをセットし、falseを返す
            errorMassage = "このユーザーは現在ログインできません"
            return false
        }
        return true
    }


    /**
     * [Repository] ユーザ検索処理
     *
     * userIdと一致するユーザIDのユーザを取得する<br>
     * ただし、一致するユーザーがいないときはnullとなる
     *
     * @param userId 取得したいユーザのユーザID
     * @return UserEntity? ユーザIDと一致したユーザ
     */
    private fun selectUser(userId: String?): UserEntity? {

        // loginUserと一致するユーザを検索
        val trimUserId: String = CommonUtil.changeEmptyByNull(userId)
        val userEntityOpt: Optional<UserEntity> = userRepository.findById(trimUserId)

        // ユーザが存在しないとき
        if (!userEntityOpt.isPresent) {
            return null
        }

        return userEntityOpt.get()
    }
}