package com.shift.domain.repository

import com.shift.domain.model.entity.NewsEntity
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository


/**
 * @author saito
 *
 */
@Repository
interface NewsRepository: BaseRepository<NewsEntity, Int> {

    /**
     * [Repository] お知らせ検索処理
     *
     * お知らせを取得する<br>
     * ただし、取得されるお知らせの数は取得したい日付を含む過去limit件までとなる<br>
     * また、表示可能なお知らせがないときはEmptyとなる
     *
     * @param ymd 取得したい日付
     * @param limit 表示するお知らせの件数
     * @return List<NewsEntity> お知らせ
     **/
    @Query(value = "SELECT n.* FROM news n WHERE n.ymd <= :ymd ORDER BY n.ymd DESC LIMIT :limit", nativeQuery = true)
    fun selectNewsNow(ymd: String?, limit: Int?): List<NewsEntity>?
}