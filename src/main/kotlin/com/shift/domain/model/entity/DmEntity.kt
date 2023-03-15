package com.shift.domain.model.entity

import javax.persistence.*


/**
 * @author saito
 *
 */
@Entity
@Table(name = "dm")
class DmEntity: BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Int? = null

    @Column(name = "send_user")
    var sendUser: String? = null

    @Column(name = "receive_user")
    var receiveUser: String? = null

    @Column(name = "msg")
    var msg: String? = null

    @Column(name = "msg_date")
    var msgDate: String? = null

    @Column(name = "read_flg")
    var readFlg: String? = null
}