package com.shift.domain.model.entity

import com.shift.common.CommonUtil
import com.shift.common.Const
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table


/**
 * @author saito
 *
 */
@Entity
@Table(name = "user")
class UserEntity: BaseEntity() {

    @Id
    @Column(name = "id")
    var id: String? = null

    @Column(name = "name")
    var name: String? = null

    @Column(name = "nameKana")
    var nameKana: String? = null

    @Column(name = "gender")
    var gender: String? = null

    @Column(name = "password")
    var password: String? = null

    @Column(name = "address")
    var address: String? = null

    @Column(name = "tel")
    var tel: String? = null

    @Column(name = "email")
    var email: String? = null

    @Column(name = "note")
    var note: String? = null

    @Column(name = "icon_kbn")
    var iconKbn: String? = null

    @Column(name = "admin_flg")
    var adminFlg: String? = null

    @Column(name = "del_flg")
    var delFlg: String? = null


    /**
     * ユーザー権限取得処理
     *
     * 取得したユーザの権限を判定し、権限情報を返す<br>
     * ただし、ユーザ権限情報は必ず"ROLE_**"になる
     *
     * @param
     * @return String ユーザーの権限情報
     */
    fun adminFlgFormatRole(): String {

        if (CommonUtil.isSuccessValidation(adminFlg, Const.PATTERN_ROLE_USER_ADMIN)) {

            // 管理者であるとき、管理者権限情報を返す
            return Const.ROLE_USER_ADMIN
        } else {

            // 一般ユーザーであるとき、一般ユーザー権限情報を返す
            return Const.ROLE_USER_GENERAL
        }
    }


    /**
     * 管理者権限判定処理
     *
     * 取得したユーザに管理者権限があるか判定する
     *
     * @param
     * @return Boolean 管理者権限の判定<br>
     * true: 管理者権限あり<br>
     * false: 管理者権限なし
     */
    fun adminFlgFormatTF(): Boolean {

        return CommonUtil.isSuccessValidation(adminFlg, Const.PATTERN_ROLE_USER_ADMIN)
    }


    /**
     * 退職済みユーザ判定処理
     *
     * 取得したユーザが退職済みユーザであるか判定する
     *
     * @param
     * @return Boolean 退職済みユーザの判定<br>
     * true: 退職済みユーザ<br>
     * false: 未退職済みユーザ
     */
    fun delFlgFormatTF(): Boolean {

        return CommonUtil.isSuccessValidation(delFlg, Const.PATTERN_USER_DEL_FLG)
    }
}