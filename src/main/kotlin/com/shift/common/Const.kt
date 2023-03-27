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

        //-----
        // DM
        //-----
        const val DM_READ_FLG: String = "1"

        //-----------
        // NEWS
        //-----------
        const val NEWS_HOME_DISPLAY_NEW_ICON_LIMIT_DAY: Long = 14
        const val NEWS_HOME_DISPLAY_LIMIT: Int = 5
        const val NEWS_HOME_SRC_NEW_ICON: String = "/img/icon/new-icon.png"
        val NEWS_CATEGORY_ICON_SRC_ARRAY: Array<String> = arrayOf("/img/icon/category-icon1.png", "/img/icon/category-icon2.png", "/img/icon/category-icon3.png")


        //-----------
        // Schedule
        //-----------
        const val SCHEDULE_HOME_CALENDAR_DISPLAY_LIMIT_DAY: Long = 7
        const val SCHEDULE_RECORDABLE_MAX_DIVISION: Int = 7
        const val SCHEDULE_DAY_RECORDED: String = "1"
        val SCHEDULE_HTML_CLASS_DISPLAY_COLOR_ARRAY: Array<String> = arrayOf("teal", "orange", "pink", "yellow", "purple", "cyan", "gray")
        val SCHEDULE_HTML_CLASS_DISPLAY_BG_COLOR_ARRAY: Array<String> = arrayOf("bg-teal", "bg-orange", "bg-pink", "bg-yellow", "bg-purple", "bg-cyan", "bg-gray")
        val SCHEDULE_HTML_CLASS_DISPLAY_BTN_COLOR_ARRAY: Array<String> = arrayOf("btn-teal", "btn-orange", "btn-pink", "btn-yellow", "btn-purple", "btn-cyan", "btn-gray")

        //-------
        // User
        //-------
        const val USER_DEL_FLG: String = "1"

        //-------
        // その他
        //-------

        // 役職
        const val ROLE_USER_ADMIN: String = "ROLE_ADMIN"
        const val ROLE_USER_GENERAL: String = "ROLE_GENERAL"

        // 文字コード
        const val CHARACTER_CODE_BREAK_LINE_ALL: String = "\r\n|\r|\n"
        const val CHARACTER_SPACE: String = " "
        const val CHARACTER_PERCENT: String = "%"
        const val CHARACTER_TAB: String = "	"

        // HTMLタグ
        const val HTML_SPACE: String = "&nbsp;";
        const val HTML_BR: String = "<br>";

        // セッション
        const val SESSION_KEYWORD_ACCOUNT_BEAN: String = "SESSION_KEY1_ACCOUNT_BEAN"
        const val SESSION_KEYWORD_DM_UNREAD: String = "SESSION_KEY2_DM_UNREAD"

        //------------------
        // 正規表現 (pattern)
        //------------------

        // ユーザ権限
        const val PATTERN_ROLE_USER_ADMIN: String = "1"

        // User
        const val PATTERN_USER_DEL_FLG: String = "1"
    }

}