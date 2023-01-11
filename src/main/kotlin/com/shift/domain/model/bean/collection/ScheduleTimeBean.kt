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


    /**
     * startHm時間フォーマット変換処理<br></br>
     * 時間フォーマット(HH:MM)に変換
     *
     * @param void
     * @return String 時間フォーマット(HH:MM)
     */
    fun startHmsFormatTime(): String? {
        return changeHmFormatTime(startHm)
    }


    /**
     * startHm時間取得処理<br></br>
     * startHmの時間(hour)を取得する
     *
     * @param void
     * @return String startHmの時間(hour)
     */
    fun startHmForHour(): String? {
        return startHm!!.substring(0, 2)
    }


    /**
     * startHm分取得処理<br></br>
     * startHmの分(minutes)を取得する
     *
     * @param void
     * @return String startHmの分(minutes)
     */
    fun startHmForMinutes(): String? {
        return startHm!!.substring(2, 4)
    }


    /**
     * endHm時間フォーマット変換処理<br></br>
     * 時間フォーマット(HH:MM)に変換
     *
     * @param void
     * @return String 時間フォーマット(HH:MM)
     */
    fun endHmsFormatTime(): String? {
        return changeHmFormatTime(endHm)
    }


    /**
     * endHm時間取得処理<br></br>
     * endHmの時間(hour)を取得する
     *
     * @param void
     * @return String endHmの時間(hour)
     */
    fun endHmForHour(): String? {
        return endHm!!.substring(0, 2)
    }


    /**
     * endHm分取得処理<br></br>
     * endHmの分(minutes)を取得する
     *
     * @param void
     * @return String endHmの分(minutes)
     */
    fun endHmForMinutes(): String? {
        return endHm!!.substring(2, 4)
    }


    /**
     * restHm時間フォーマット変換処理<br></br>
     * 時間フォーマット(HH:MM)に変換
     *
     * @param void
     * @return String 時間フォーマット(HH:MM)
     */
    fun restHmsFormatTime(): String? {
        return changeHmFormatTime(restHm)
    }


    /**
     * restHm時間取得処理<br></br>
     * restHmの時間(hour)を取得する
     *
     * @param void
     * @return String restHmの時間(hour)
     */
    fun restHmForHour(): String? {
        return restHm!!.substring(0, 2)
    }


    /**
     * restHm分取得処理<br></br>
     * restHmの分(minutes)を取得する
     *
     * @param void
     * @return String restHmの分(minutes)
     */
    fun restHmForMinutes(): String? {
        return restHm!!.substring(2, 4)
    }


    /**
     * [private 共通処理] 時間フォーマット変換処理<br></br>
     * 時間フォーマット(HH:MM)に変換
     *
     * @param hm 変換したい値
     * @return String 時間フォーマット(HH:MM)
     */
    private fun changeHmFormatTime(hm: String?): String? {

        //フォーマットをhh:mmに変換し、返す
        return hm!!.substring(0, 2) + ":" + hm!!.substring(2, 4)
    }
}