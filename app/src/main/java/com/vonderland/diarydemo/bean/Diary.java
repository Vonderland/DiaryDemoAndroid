package com.vonderland.diarydemo.bean;

import java.io.Serializable;
import java.util.Comparator;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Vonderland on 2017/2/1.
 */

public class Diary extends RealmObject implements Serializable, Comparable<Diary> {
    @PrimaryKey
    private long id;
    private long uid;
    private String title;
    private String description;
    private String url;
    private boolean isDeleted;
    private boolean isPrivate;
    private long eventTime;
    private long createTime;
    private long updateTime;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    @Ignore
    //以下字段用于区分 item 是否为 footer，不存入 Realm
    private boolean isFooter = false;
    @Ignore
    private boolean isEmptyView = false;
    @Ignore
    private boolean isNoMore = false;

    @Override
    public int compareTo(Diary o) {
        if (this.eventTime != o.eventTime) {
            if (this.eventTime > o.eventTime) {
                return -1;
            } else {
                return 1;
            }
        } else {
            if (this.updateTime > o.updateTime) {
                return -1;
            } else {
                return 1;
            }
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public long getEventTime() {
        return eventTime;
    }

    public void setEventTime(long eventTime) {
        this.eventTime = eventTime;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public boolean isFooter() {
        return isFooter;
    }

    public void setFooter(boolean footer) {
        isFooter = footer;
    }

    public boolean isEmptyView() {
        return isEmptyView;
    }

    public void setEmptyView(boolean emptyView) {
        isEmptyView = emptyView;
    }

    public boolean isNoMore() {
        return isNoMore;
    }

    public void setNoMore(boolean noMore) {
        isNoMore = noMore;
    }
}
