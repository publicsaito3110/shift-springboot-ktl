package com.shift.domain.model.bean

import com.shift.domain.model.bean.collection.NewsContentBean
import lombok.AllArgsConstructor
import lombok.NoArgsConstructor

/**
 * @author saito
 *
 */
@AllArgsConstructor
@NoArgsConstructor
class CmnNewsBean {

    var newsList: List<NewsContentBean>? = null
}