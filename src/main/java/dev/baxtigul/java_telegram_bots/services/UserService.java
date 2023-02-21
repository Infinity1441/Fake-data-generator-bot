package dev.baxtigul.java_telegram_bots.services;

import dev.baxtigul.java_telegram_bots.daos.UserDao;
import dev.baxtigul.java_telegram_bots.domains.UserDomain;
import dev.baxtigul.java_telegram_bots.dto.Response;
import dev.baxtigul.java_telegram_bots.utils.BaseUtils;
import dev.baxtigul.java_telegram_bots.utils.PasswordEncoderUtils;
import lombok.NonNull;

import java.sql.SQLException;
import java.util.List;

public class UserService {

    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public Response<Void> create(@NonNull UserDomain domain) {
        try {
            userDao.save(domain);
            return new Response<>(null);
        } catch (SQLException e) {
            // TODO: 05/02/23 log
            e.printStackTrace();
            return new Response<>("Something is wrong try later again", BaseUtils.getStackStraceAsString(e));
        }
    }

    public Response<List<UserDomain>> getAllUsers() {
        try {
            return new Response<>(userDao.findAll());
        } catch (SQLException e) {
            // TODO: 05/02/23 log
            e.printStackTrace();
            return new Response<>(e.getMessage(), BaseUtils.getStackStraceAsString(e));
        }
    }
}
