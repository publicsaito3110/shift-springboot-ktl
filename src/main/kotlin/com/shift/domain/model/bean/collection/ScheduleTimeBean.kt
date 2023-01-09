package com.shift.domain.model.bean.collection

import lombok.AllArgsConstructor
import lombok.NoArgsConstructor

/**
 * @author saito
 *
 */
@AllArgsConstructor
@NoArgsConstructor
class ScheduleTimeBean {

    var name: String? = null

    var startHm: String? = null

    var endHm: String? = null

    var restHm: String? = null


    /**
     * [Constractor] AllArgs
     *
     * スケジュール時間区分を保持するための値を取得し、セットする
     *
     * @param name セッションとして保持したいユーザ
     * @param startHm セッションとして保持したいユーザ
     * @param endHm セッションとして保持したいユーザ
     * @param restHm セッションとして保持したいユーザ
     * @return void
     */
    constructor(name: String?, startHm: String?, endHm: String?, restHm: String?) {
        this.name = name
        this.startHm = startHm
        this.endHm = endHm
        this.restHm = restHm
    }
}