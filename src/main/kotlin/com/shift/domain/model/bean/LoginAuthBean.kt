package com.shift.domain.model.bean

import lombok.AllArgsConstructor
import lombok.NoArgsConstructor

/**
 * @author saito
 *
 */
@AllArgsConstructor
@NoArgsConstructor
class LoginAuthBean {

    var isLogin: Boolean? = false

    var errorMassage: String? = null
}