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
}