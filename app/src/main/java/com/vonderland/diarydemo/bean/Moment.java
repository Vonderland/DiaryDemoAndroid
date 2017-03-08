package com.vonderland.diarydemo.bean;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Vonderland on 2017/2/1.
 */

public class Moment extends RealmObject implements Serializable, Comparable<Moment> {
    @PrimaryKey
    private long id;
    private String title;
    private String location;
    private long eventTime;
    private long createTime;
    private long updateTime;
    private boolean isDeleted;

    @Ignore
    //以下字段用于区分 item 是否为 footer，不存入 Realm
    private boolean isFooter = false;
    @Ignore
    private boolean isEmptyView = false;
    @Ignore
    private boolean isNoMore = false;

    @Override
    public int compareTo(Moment o) {
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
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
