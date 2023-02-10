package com.shift.domain.model.bean.collection

import com.shift.common.CommonLogic
import com.shift.common.Const
import com.shift.domain.model.entity.NewsEntity
import lombok.AllArgsConstructor
import lombok.NoArgsConstructor

/**
 * @author saito
 *
 */
@AllArgsConstructor
@NoArgsConstructor
class NewsContentBean {

    var id: Int? = 0

    var ymd: String? = null

    var category: String? = null

    var title: String? = null

    var content: String? = null

    var srcPngNewIcon: String? = null


    /**
     * [Constractor] NewsEntity
     *
     * newsEntityと一致する値を取得し、セットする<br>
     * ただし、srcPngNewIconは更新されない
     *
     * @param newsEntity
     * @return NewsContentBean
     */
    constructor(newsEntity: NewsEntity) {
        this.id = newsEntity.id
        this.ymd = newsEntity.ymd
        this.category = newsEntity.category
        this.title = newsEntity.title
        this.content = newsEntity.content
    }


    /**
     * 日付変換処理
     *
     * ymdを日付フォーマット(MM/DD)に変換する
     *
     * @return 日付フォーマットに変換されたymd
     */
    fun ymdFormatDate(): String {
        return ymd!!.substring(4, 6) + "/" + ymd!!.substring(6, 8)
    }


    /**
     * カテゴリーアイコン変換処理
     *
     * categoryと一致するアイコンの画像パスを取得
     *
     * @return categoryと一致するアイコンの画像パス
     */
    fun categoryFormatPngSrc(): String {
        return Const.NEWS_CATEGORY_ICON_SRC_ARRAY[category!!.toInt() - 1]
    }


    /**
     * HTML対応文字列変換処理
     *
     * contentをHTMLの改行とスペース対応させた文字列に変換する
     *
     * @return HTML対応したcontent
     */
    fun contentFormatBreakLine(): String? {
        return CommonLogic().changeAfterBreakLine(content)
    }
}