package com.sismics.docs.core.dao;

import com.sismics.docs.core.dao.criteria.GroupCriteria;
import com.sismics.docs.core.dao.dto.ChatDto;
import com.sismics.docs.core.dao.dto.GroupDto;
import com.sismics.docs.core.model.jpa.Chat;
import com.sismics.docs.core.util.jpa.SortCriteria;
import com.sismics.util.context.ThreadLocalContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ChatDao {
    public List<ChatDto> getAllChats(String userid, int limit, int offset) {
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        Query q = em.createQuery("select c from Chat c where c.fromUserId=:userid or c.toUserId=:userid order by c.time desc");
        q.setFirstResult(offset);
        q.setMaxResults(limit);
        q.setParameter("userid", userid);
        return getChatDtos(q);
    }
    public List<ChatDto> getOneChat(String userid, String toUserId, int limit, int offset) {
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        Query q = em.createQuery("select c from Chat c " +
                "where (c.fromUserId=:userid and c.toUserId=:touserid) or " +
                "(c.toUserId=:userid and c.fromUserId=:touserid) " +
                "order by c.time desc");
        q.setParameter("userid", userid);
        q.setParameter("touserid", toUserId);
        q.setFirstResult(offset);
        q.setMaxResults(limit);
        return getChatDtos(q);
    }

    @SuppressWarnings("unchecked")
    @NotNull
    private List<ChatDto> getChatDtos(Query q) {
        List<Chat> chats = q.getResultList();
        List<ChatDto> chatDtoList = new ArrayList<>();
        for (Chat chat : chats) {
            ChatDto chatDto = new ChatDto();
            chatDto.setId(chat.getId());
            chatDto.setFromUserId(chat.getFromUserId());
            chatDto.setToUserId(chat.getToUserId());
            chatDto.setMsg(chat.getMsg());
            chatDto.setCreatedAt(chat.getTime());
            chatDtoList.add(chatDto);
        }
        return chatDtoList;
    }

    public boolean createChat(ChatDto chatDto) {
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        GroupDao groupDao = new GroupDao();
        GroupCriteria groupCriteria = new GroupCriteria();
        groupCriteria.setUserId(chatDto.getFromUserId());
        SortCriteria sortCriteria = new SortCriteria(0, false);
        List<GroupDto> userFromGroups = groupDao.findByCriteria(groupCriteria, sortCriteria);
        groupCriteria.setUserId(chatDto.getToUserId());
        List<GroupDto> userToGroups = groupDao.findByCriteria(groupCriteria, sortCriteria);
        Set<String> userFromGroupsSet = new HashSet<>();
        for (GroupDto groupDto : userFromGroups) {
            userFromGroupsSet.add(groupDto.getId());
        }
        for (GroupDto groupDto : userToGroups) {
            if (userFromGroupsSet.contains(groupDto.getId())) {
                Chat chat = new Chat();
                chat.setId(UUID.randomUUID().toString());
                chat.setFromUserId(chatDto.getFromUserId());
                chat.setToUserId(chatDto.getToUserId());
                chat.setMsg(chatDto.getMsg());
                chat.setTime(chatDto.getCreatedAt() == null ? new Date() : chatDto.getCreatedAt());
                // TODO: check UUID conflict
                em.persist(chat);
                chatDto.setId(chat.getId());
                chatDto.setCreatedAt(chat.getTime());
                return true;
            }
        }
        return false;
    }
}
