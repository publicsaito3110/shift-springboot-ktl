package com.shift.domain.model.bean.collection

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
}