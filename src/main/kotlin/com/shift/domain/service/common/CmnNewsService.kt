package com.shift.domain.service.common

import com.shift.common.CommonLogic
import com.shift.common.CommonUtil
import com.shift.common.Const
import com.shift.domain.model.bean.CmnNewsBean
import com.shift.domain.model.bean.collection.NewsContentBean
import com.shift.domain.model.entity.NewsEntity
import com.shift.domain.repository.NewsRepository
import com.shift.domain.service.BaseService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate


/**
 * @author saito
 *
 */
@Service
@Transactional
class CmnNewsService: BaseService() {

    @Autowired
    private lateinit var newsRepository: NewsRepository


    /**
     * [共通 Service] 表示可能お知らせ取得処理
     *
     * @return CmnNewsBean
     */
    fun generateDisplayNews(): CmnNewsBean {

        // 現在の日付から表示可能なお知らせを取得
        val newsDbList: List<NewsEntity> = selectNewsForNow()
        // 取得したお知らせを表示用に変換
        val newsList: List<NewsContentBean> = changeDisplayNews(newsDbList)

        // Beanにセット
        val cmnNewsBean = CmnNewsBean()
        cmnNewsBean.newsList = newsList
        return cmnNewsBean
    }


    /**
     * 表示用お知らせ変換処理
     *
     * Repositoryから取得したお知らせを表示用のお知らせに変換する<br>
     * ただし、Repositoryからお知らせが取得できない(Empty)ときはEmptyが返される
     *
     * @param newsDbList Repositoryから取得したお知らせ
     * @return List<NewsContentBean> 表示用に変換されたお知らせ
     **/
    private fun changeDisplayNews(newsDbList: List<NewsEntity>): List<NewsContentBean> {

        // 表示できるお知らせがないとき
        if (newsDbList.isEmpty()) {
            return ArrayList<NewsContentBean>()
        }

        // 現在の日付のLocalDateを取得
        val nowLd = LocalDate.now()

        // 現在の日付からお知らせにnew-iconを表示する下限の日付を取得
        val limitDateLd = nowLd.minusDays(Const.NEWS_HOME_DISPLAY_NEW_ICON_LIMIT_DAY)

        // お知らせを格納するための変数
        val newsList: MutableList<NewsContentBean> = mutableListOf()
        val commonLogic = CommonLogic()

        // 取得したお知らせだけ判定
        for (newsEntity in newsDbList) {

            // お知らせの表示開始日付が表示可能の日付より後のとき
            val newsDateLd = commonLogic.getLocalDate(newsEntity.ymd)
            if (newsDateLd!!.isAfter(limitDateLd)) {

                // newアイコンの表示を設定
                val newsContentBeanBean = NewsContentBean(newsEntity)
                newsContentBeanBean.srcPngNewIcon = Const.NEWS_HOME_SRC_NEW_ICON
                newsList.add(newsContentBeanBean)
                continue
            }
            newsList.add(NewsContentBean(newsEntity))
        }
        return newsList
    }


    /**
     * [Repository] お知らせ検索処理
     *
     * ホーム画面に表示するニュースを取得する<br>
     * 取得するお知らせは現在日含む上限数までとなる<br>
     * ただし、表示可能なお知らせがないときはEmptyとなる
     *
     * @return List<NewsEntity> お知らせ
     **/
    private fun selectNewsForNow(): List<NewsEntity> {

        // 現在の日付から表示可能なお知らせを取得
        val nowYmd: String = CommonUtil.getNowYmd()
        return newsRepository.selectNewsNow(nowYmd, Const.NEWS_HOME_DISPLAY_LIMIT)
    }
}