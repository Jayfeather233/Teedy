package com.sismics.docs.rest.resource;

import com.sismics.docs.core.dao.RegisterRequestDAO;
import com.sismics.docs.core.model.jpa.RegisterRequest;
import com.sismics.docs.rest.constant.BaseFunction;
import com.sismics.rest.exception.ForbiddenClientException;
import com.sismics.rest.exception.ServerException;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObjectBuilder;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/register")
public class UserRegisterResource extends BaseResource {
    @GET
    @Path("/reglist")
    public Response GetRegisterList() {
        if (!authenticate()) {
            throw new ForbiddenClientException();
        }

        checkBaseFunction(BaseFunction.ADMIN);

        RegisterRequestDAO rrd = new RegisterRequestDAO();
        List<RegisterRequest> lrr = rrd.getRegisterRequest();
        JsonArrayBuilder ret = Json.createArrayBuilder();
        for(RegisterRequest rr : lrr) {
            ret.add(Json.createObjectBuilder()
                    .add("id", rr.getReq_id())
                    .add("username", rr.getUserName())
                    .add("email", rr.getEmail())
                    .add("descript", rr.getDescript())
                    .add("create_date", rr.getCreateDate().toString()));
        }

        return Response.ok().entity(ret.build()).build();
    }
    @GET
    @Path("/all")
    public Response GetAllRegisterList() {
        if (!authenticate()) {
            throw new ForbiddenClientException();
        }

        checkBaseFunction(BaseFunction.ADMIN);

        RegisterRequestDAO rrd = new RegisterRequestDAO();
        List<RegisterRequest> lrr = rrd.getAllRegisterRequest();
        JsonArrayBuilder ret = Json.createArrayBuilder();
        for(RegisterRequest rr : lrr) {
            ret.add(Json.createObjectBuilder()
                    .add("id", rr.getReq_id())
                    .add("username", rr.getUserName())
                    .add("email", rr.getEmail())
                    .add("descript", rr.getDescript())
                    .add("create_date", rr.getCreateDate() == null ? "null" : rr.getCreateDate().toString())
                    .add("delete_date", rr.getDeleteDate() == null ? "null" : rr.getDeleteDate().toString())
                    .add("disable_date", rr.getDisableDate() == null ? "null" : rr.getDisableDate().toString()));
        }

        return Response.ok().entity(ret.build()).build();
    }

    @POST
    @Path("/accept")
    public Response accept(@QueryParam("id") String id) {
        if (!authenticate()) {
            throw new ForbiddenClientException();
        }
        checkBaseFunction(BaseFunction.ADMIN);

        RegisterRequestDAO rrd = new RegisterRequestDAO();
        try {
            rrd.acceptRequest(id);
        } catch (Exception e) {
            throw new ServerException("Register Error", "failed to accept the register attempt", e);
        }
        JsonObjectBuilder response = Json.createObjectBuilder()
                .add("status", "ok");
        return Response.ok().entity(response.build()).build();
    }

    @POST
    @Path("/deny")
    public Response deny(@QueryParam("id") String id) {
        if (!authenticate()) {
            throw new ForbiddenClientException();
        }
        checkBaseFunction(BaseFunction.ADMIN);

        RegisterRequestDAO rrd = new RegisterRequestDAO();
        try {
            rrd.denyRequest(id);
        } catch (Exception e) {
            throw new ServerException("Register Error", "failed to accept the register attempt", e);
        }
        JsonObjectBuilder response = Json.createObjectBuilder()
                .add("status", "ok");
        return Response.ok().entity(response.build()).build();
    }

    @POST
    @Path("/create")
    public Response create(@FormParam("username") String username,
                           @FormParam("password") String password,
                           @FormParam("email") String email,
                           @FormParam("descript") String descript) {

        if (username == null || username.trim().isEmpty() ||
                password == null || password.trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Username and password are required.")
                    .build();
        }

        RegisterRequestDAO rrd = new RegisterRequestDAO();
        try {
            rrd.addRequest(username, password, email, descript);
        } catch (Exception e) {
            throw new ServerException("Register Error", "failed to add the register attempt" + e.getMessage(), e);
        }
        JsonObjectBuilder response = Json.createObjectBuilder()
                .add("status", "ok");
        return Response.ok().entity(response.build()).build();
    }
}
