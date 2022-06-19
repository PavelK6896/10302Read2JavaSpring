package app.web.pavelk.read2.service;

import app.web.pavelk.read2.schema.User;

/**
 * Service for working with user.
 */
public interface UserService {

    /**
     * Get current user id.
     *
     * @return id or null
     */
    Long getUserId();

    /**
     * Get user from cash map or db.
     *
     * @return user
     */
    User getUser();

    /**
     * Get user from db.
     *
     * @return user
     */
    User getCurrentUserFromDB();

    /**
     * Check authenticated.
     *
     * @return status
     */
    boolean isAuthenticated();

}
