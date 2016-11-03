package com.zhj.pixivision_getter.entity;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Administrator on 2016/10/20 0020.
 */
@Table(name = "CurrentIndex")
public class CurrentIndex {
    @Column(name = "id",isId = true)
    private int id;
    @Column(name = "currentIndex")
    private int currentIndex;


}
