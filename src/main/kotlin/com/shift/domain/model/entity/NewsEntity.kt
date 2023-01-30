package com.shift.domain.model.entity

import javax.persistence.*


/**
 * @author saito
 *
 */
@Entity
@Table(name = "news")
class NewsEntity: BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Int? = null

    @Column(name = "ymd")
    var ymd: String? = null

    @Column(name = "category")
    var category: String? = null

    @Column(name = "title")
    var title: String? = null

    @Column(name = "content")
    var content: String? = null
}