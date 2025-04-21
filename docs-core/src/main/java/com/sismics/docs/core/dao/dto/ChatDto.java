package com.sismics.docs.core.dao.dto;

import java.util.Date;

public class ChatDto {
    private String id;
    private String fromUserId;
    private String toUserId;
    private String msg;
    private Date createdAt;

    public String getId() {
        return id;
    }
    public ChatDto setId(String id) {
        this.id = id;
        return this;
    }
    public String getFromUserId() {
        return fromUserId;
    }
    public ChatDto setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
        return this;
    }
    public String getToUserId() {
        return toUserId;
    }
    public ChatDto setToUserId(String toUserId) {
        this.toUserId = toUserId;
        return this;
    }
    public String getMsg() {
        return msg;
    }
    public ChatDto setMsg(String msg) {
        this.msg = msg;
        return this;
    }
    public Date getCreatedAt() {
        return createdAt;
    }
    public ChatDto setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        return this;
    }
}
