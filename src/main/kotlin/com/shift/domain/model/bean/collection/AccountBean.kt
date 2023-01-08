package com.shift.domain.model.bean.collection

import com.shift.domain.model.entity.UserEntity
import lombok.AllArgsConstructor
import lombok.NoArgsConstructor

/**
 * @author saito
 *
 */
@AllArgsConstructor
@NoArgsConstructor
class AccountBean {

    var userId: String? = null

    var name: String? = null

    var nameKana: String? = null

    var isAdmin: Boolean = false


    /**
     * [Constractor] UserEntity
     *
     * セッションとして保持するためのユーザ情報をUserEntityから取得し、セットする<br>
     * ただし、nullのときは何もしない
     *
     * @param userEntity セッションとして保持したいユーザ
     * @return void
     */
    constructor(userEntity: UserEntity?) {

        if (userEntity != null) {
            userId = userEntity.id
            name = userEntity.name
            nameKana = userEntity.nameKana
            isAdmin = userEntity.adminFlgFormatTF()
        }
    }
}