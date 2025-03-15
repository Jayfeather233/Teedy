package com.sismics.docs.core.model.jpa;

import com.google.common.base.MoreObjects;
import jakarta.persistence.*;

import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "T_REGISTER_REQUEST")
public class RegisterRequest implements Loggable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "RER_ID_C", nullable = false)
    private String req_id;

    @Column(name = "RER_USERNAME_C", nullable = false, unique = true)
    private String userName;

    @Column(name = "RER_PASSWORD_C", nullable = false)
    private String password;

    @Column(name = "RER_EMAIL_C", nullable = false, unique = true)
    private String email;

    @Column(name = "RER_DESC_C")
    private String descript;

    @Column(name = "RER_CREATEDATE_D", nullable = false)
    private Date createDate;

    // accepted
    @Column(name = "RER_DELETEDATE_D")
    private Date deleteDate;

    // ignored
    @Column(name = "RER_DISABLEDATE_D")
    private Date disableDate;

    public RegisterRequest() {}

    public RegisterRequest(String userName, String email, String desc, Date create_d) {
        this.userName = userName;
        this.email = email;
        this.descript = desc;
        this.createDate = create_d;
    }

    public String getReq_id() { return req_id; }
    public RegisterRequest setReq_id(String id) { this.req_id = id; return this; }

    public String getUserName() { return userName; }
    public RegisterRequest setUserName(String userName) { this.userName = userName; return this; }

    public String getPassword() { return password; }
    public RegisterRequest setPassword(String password) { this.password = password; return this; }

    public String getEmail() { return email; }
    public RegisterRequest setEmail(String email) { this.email = email; return this; }

    public String getDescript() { return descript;}
    public RegisterRequest setDescript(String descript) {
        this.descript = descript;
        return this;
    }



    public Date getCreateDate() {
        return createDate;
    }
    public RegisterRequest setCreateDate(Date createDate) {
        this.createDate = createDate;
        return this;
    }

    @Override
    public Date getDeleteDate() {
        return deleteDate;
    }
    public RegisterRequest setDeleteDate(Date deleteDate) {
        this.deleteDate = deleteDate;
        return this;
    }

    public Date getDisableDate() {
        return disableDate;
    }
    public RegisterRequest setDisableDate(Date disableDate) {
        this.disableDate = disableDate;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegisterRequest that = (RegisterRequest) o;
        return Objects.equals(req_id, that.req_id) &&
                Objects.equals(userName, that.userName) &&
                Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(req_id, userName, email);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("req_id", req_id)
                .add("username", userName)
                .add("email", email)
                .toString();
    }
    @Override
    public String toMessage() {
        return this.toString();
    }
}
