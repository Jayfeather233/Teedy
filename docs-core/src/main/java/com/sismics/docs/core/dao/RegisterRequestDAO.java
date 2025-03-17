package com.sismics.docs.core.dao;

import com.sismics.docs.core.constant.AuditLogType;
import com.sismics.docs.core.model.jpa.RegisterRequest;
import com.sismics.docs.core.model.jpa.User;
import com.sismics.docs.core.util.AuditLogUtil;
import com.sismics.util.context.ThreadLocalContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class RegisterRequestDAO {
    private static final Logger log = LoggerFactory.getLogger(RegisterRequestDAO.class);

    private void insertUser(RegisterRequest rr) throws Exception {
        UserDao ud = new UserDao();
        if (ud.findUser(rr.getUserName(), rr.getEmail())) {
            throw new IllegalArgumentException("Username or email duplicated");
        }
        User u = new User();
        u.setCreateDate(new Date());
        u.setEmail(rr.getEmail());
        u.setUsername(rr.getUserName());
        u.setPassword(rr.getPassword());
        u.setRoleId("user");
        u.setStorageQuota(1024L*1024*1024);

        try {
            ud.create(u, rr.getReq_id());
        } catch (Exception e) {
            log.error("Error creating user", e);
            throw e;
        }
    }

    public List<RegisterRequest> getRegisterRequest() {
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        // Get the user
        Query q = em.createQuery("select u from RegisterRequest u where u.deleteDate is null and u.disableDate is null");

        @SuppressWarnings("unchecked")
        List<RegisterRequest> results = q.getResultList();
        return results;
    }

    public List<RegisterRequest> getAllRegisterRequest() {
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        // Get the user
        Query q = em.createQuery("select u from RegisterRequest u");

        @SuppressWarnings("unchecked")
        List<RegisterRequest> results = q.getResultList();
        return results;
    }

    public void acceptRequest(String id) throws Exception {
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        // Get the user
        Query q = em.createQuery("select u from RegisterRequest u where u.id = :id and u.deleteDate is null and u.disableDate is null");
        q.setParameter("id", id);

        try {
            RegisterRequest rr = (RegisterRequest) q.getSingleResult();
            rr.setDeleteDate(new Date());
            insertUser(rr);
        } catch (Exception e) {
            log.error("Error registering user", e);
            throw e;
        }
    }

    public void denyRequest(String id){
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        // Get the user
        Query q = em.createQuery("select u from RegisterRequest u where u.id = :id and u.deleteDate is null and u.disableDate is null");
        q.setParameter("id", id);

        RegisterRequest rr = (RegisterRequest) q.getSingleResult();
        rr.setDisableDate(new Date());
    }

    public void addRequest(String userName, String password, String email, String desc) {
        UserDao ud = new UserDao();
        if (ud.findUser(userName, email)) {
            throw new IllegalArgumentException("Username or email duplicated");
        }
        RegisterRequest rr = new RegisterRequest(userName, email, desc, new Date());
        rr.setPassword(password);
//        rr.setReq_id(UUID.randomUUID().toString());

        EntityManager em = ThreadLocalContext.get().getEntityManager();
        em.persist(rr);

        AuditLogUtil.create(rr, AuditLogType.CREATE, rr.getReq_id());
    }
}
