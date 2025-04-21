package com.sismics.docs.core.model.jpa;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "T_CHAT")
public class Chat {
    @Id
    @Column(name = "CHA_ID_N", length = 36)
    private String id;

    @Column(name = "CHA_FROM_C", nullable = false, length = 36)
    private String fromUserId;

    @Column(name = "CHA_TO_C", nullable = false, length = 36)
    private String toUserId;

    @Column(name = "CHA_MSG_C", nullable = false, length = 1024)
    private String msg;

    /**
     * Send time
     */
    @Column(name = "CHA_TIME_D")
    private Date time;

    public String getId() {
        return id;
    }
    public Chat setId(String id) {
        this.id = id;
        return this;
    }
    public String getFromUserId() {
        return fromUserId;
    }
    public Chat setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
        return this;
    }
    public String getToUserId() {
        return toUserId;
    }
    public Chat setToUserId(String toUserId) {
        this.toUserId = toUserId;
        return this;
    }
    public String getMsg() {
        return msg;
    }
    public Chat setMsg(String msg) {
        this.msg = msg;
        return this;
    }
    public Date getTime() {
        return time;
    }
    public Chat setTime(Date time) {
        this.time = time;
        return this;
    }
}
