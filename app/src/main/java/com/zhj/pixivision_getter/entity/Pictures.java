package com.zhj.pixivision_getter.entity;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Administrator on 2016/10/25 0025.
 */
@Table(name = "picture")
public class Pictures {
    @Column(name = "id",isId = true)
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "author")
    private String author;
    @Column(name = "picUrl")
    private String picUrl;
    @Column(name = "pixivsionIndex")
    private int pixivsionIndex;
    @Column(name = "isdownLoad")
    private boolean isdownLoad;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isdownLoad() {
        return isdownLoad;
    }

    public void setIsdownLoad(boolean isdownLoad) {
        this.isdownLoad = isdownLoad;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getPixivsionIndex() {
        return pixivsionIndex;
    }

    public void setPixivsionIndex(int pixivsionIndex) {
        this.pixivsionIndex = pixivsionIndex;
    }

    @Override
    public String toString() {
        return "Pictures{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", picUrl='" + picUrl + '\'' +
                ", pixivsionIndex=" + pixivsionIndex +
                ", isdownLoad=" + isdownLoad +
                '}';
    }
}
