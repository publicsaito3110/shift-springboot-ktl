package com.shift.common

/**
 * @author saito
 *
 */
public class Const {
    private constructor() {
    // インスタンス化を禁止
    }

    companion object {

        //-----------
        // Schedule
        //-----------
        const val SCHEDULE_RECORDABLE_MAX_DIVISION: Int = 7
        const val SCHEDULE_DAY_RECORDED: String = "1"
        val SCHEDULE_HTML_CLASS_DISPLAY_COLOR_ARRAY: Array<String> = arrayOf("teal", "orange", "pink", "yellow", "purple", "cyan", "gray")
        val SCHEDULE_HTML_CLASS_DISPLAY_BG_COLOR_ARRAY: Array<String> = arrayOf("bg-teal", "bg-orange", "bg-pink", "bg-yellow", "bg-purple", "bg-cyan", "bg-gray")
        val SCHEDULE_HTML_CLASS_DISPLAY_BTN_COLOR_ARRAY: Array<String> = arrayOf("btn-teal", "btn-orange", "btn-pink", "btn-yellow", "btn-purple", "btn-cyan", "btn-gray")

        //-----
        // DM
        //-----
        const val DM_READ_FLG: String = "1"

        //-------
        // その他
        //-------
        const val ROLE_USER_ADMIN: String = "ROLE_ADMIN"
        const val ROLE_USER_GENERAL: String = "ROLE_GENERAL"

        const val SESSION_KEYWORD_ACCOUNT_BEAN: String = "SESSION_KEY1_ACCOUNT_BEAN"

        //------------------
        // 正規表現 (pattern)
        //------------------

        // ユーザ権限
        const val PATTERN_ROLE_USER_ADMIN: String = "1"

        // User
        const val PATTERN_USER_DEL_FLG: String = "1"
    }

}