package app.web.pavelk.read2.service;

import app.web.pavelk.read2.schema.User;

public interface UserService {

    Long getUserId();

    User getUser();

    boolean isAuthenticated();

}
