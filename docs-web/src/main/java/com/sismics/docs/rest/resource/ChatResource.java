package com.sismics.docs.rest.resource;

import com.sismics.docs.core.dao.ChatDao;
import com.sismics.docs.core.dao.dto.ChatDto;
import com.sismics.rest.exception.ClientException;
import com.sismics.rest.exception.ForbiddenClientException;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObjectBuilder;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/chat")
public class ChatResource extends BaseResource{
    @PUT
    public Response SendChatMessage(
            @FormParam("userid") String toUserId,
            @FormParam("message") String message
    ) {
        if (!authenticate()) {
            throw new ForbiddenClientException();
        }
        ChatDao chatDao = new ChatDao();
        ChatDto chatDto = new ChatDto();
        chatDto.setFromUserId(principal.getId());
        chatDto.setToUserId(toUserId);
        chatDto.setMsg(message);
        if (!chatDao.createChat(chatDto)) {
            throw new ClientException("ChatNotSameGroup", "Message sending to user not in the same group");
        } else {
            return  Response.status(Response.Status.ACCEPTED).entity(createChatObjectBuilder(chatDto).build()).build();
        }
    }

    @GET
    public Response GetAllChatMessages(@DefaultValue("0") @QueryParam("offset") int offset, @DefaultValue("50") @QueryParam("limit") int limit) {
        if (!authenticate()) {
            throw new ForbiddenClientException();
        }
        ChatDao chatDao = new ChatDao();
        List<ChatDto> chats = chatDao.getAllChats(principal.getId(), limit, offset);
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (ChatDto chatDto : chats) {
            arrayBuilder.add(createChatObjectBuilder(chatDto));
        }
        return Response.ok().entity(arrayBuilder.build()).build();
    }

    @GET
    @Path("{id}")
    public Response GetChatMessage(@DefaultValue("0") @PathParam("id") String id, @QueryParam("offset") int offset, @DefaultValue("50") @QueryParam("limit") int limit) {
        if (!authenticate()) {
            throw new ForbiddenClientException();
        }
        ChatDao chatDao = new ChatDao();
        List<ChatDto> chats = chatDao.getOneChat(principal.getId(), id, limit, offset);
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (ChatDto chatDto : chats) {
            arrayBuilder.add(createChatObjectBuilder(chatDto));
        }
        return Response.ok().entity(arrayBuilder.build()).build();
    }


    private JsonObjectBuilder createChatObjectBuilder(ChatDto chatDto) {
        return Json.createObjectBuilder()
                .add("id", chatDto.getId())
                .add("fromUserId", chatDto.getFromUserId())
                .add("toUserId", chatDto.getToUserId())
                .add("msg", chatDto.getMsg())
                .add("create_time", chatDto.getCreatedAt().getTime());
    }
}
