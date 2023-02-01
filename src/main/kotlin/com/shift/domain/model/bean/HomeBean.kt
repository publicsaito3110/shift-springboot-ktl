package com.shift.domain.model.bean

import com.shift.domain.model.bean.collection.HomeDayScheduleBean
import com.shift.domain.model.bean.collection.NewsContentBean
import com.shift.domain.model.entity.UserEntity
import lombok.AllArgsConstructor
import lombok.NoArgsConstructor


/**
 * @author saito
 *
 */
@AllArgsConstructor
@NoArgsConstructor
class HomeBean {

    var beforeWeekYmd: String? = null

    var afterWeekYmd: String? = null

    var userEntity: UserEntity? = null

    var newsList: List<NewsContentBean>? = null

    var dayScheduleList: List<HomeDayScheduleBean>? = null
}