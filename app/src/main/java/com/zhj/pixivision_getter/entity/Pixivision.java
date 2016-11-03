package com.zhj.pixivision_getter.entity;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.util.Date;

/**
 * Created by Administrator on 2016/10/6 0006.
 */
@Table(name = "pixivion")
public class Pixivision {
    @Column(name = "id",isId = true)
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "index")
    private int index;
    @Column(name = "descrip")
    private String description;
    @Column(name = "localIndex")
    private int localIndex;//本地的排序，用于查找分页
    @Column(name = " date")
    private Date date;
    @Column(name = "type")
    private String type;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLocalIndex() {
        return localIndex;
    }

    public void setLocalIndex(int localIndex) {
        this.localIndex = localIndex;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Pixivision{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", index=" + index +
                ", description='" + description + '\'' +
                ", localIndex=" + localIndex +
                ", date=" + date +
                ", type='" + type + '\'' +
                '}';
    }
}
