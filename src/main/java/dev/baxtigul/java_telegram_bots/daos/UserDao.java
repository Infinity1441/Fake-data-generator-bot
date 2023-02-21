package dev.baxtigul.java_telegram_bots.daos;

import dev.baxtigul.java_telegram_bots.domains.UserDomain;
import lombok.NonNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class UserDao extends Dao {

    public void save(@NonNull UserDomain domain) throws SQLException {
        Connection connection = getConnection();
        PreparedStatement pst = connection.prepareStatement("insert into public.users(chatid,username,firstname,language) values(?,?,?,?);");
        pst.setLong(1, domain.getChatID());
        pst.setString(2, domain.getUsername());
        pst.setString(3, domain.getFirstName());
        pst.setString(4, domain.getLanguage());
        pst.execute();
    }

    public List<UserDomain> findAll() throws SQLException {
        Connection connection = getConnection();
        PreparedStatement pst = connection.prepareStatement("select chatid, username, firstname, createdat, language from public.users");
        ResultSet rs = pst.executeQuery();
        ArrayList<UserDomain> userDomains = new ArrayList<>();
        while ( rs.next() ) {
            userDomains.add(UserDomain.builder()
                    .chatID(rs.getLong("chatid"))
                    .username(rs.getString("username"))
                    .firstName(rs.getString("firstname"))
                    .createdAt(rs.getTimestamp("createdat").toLocalDateTime())
                    .language(rs.getString("language"))
                    .build());
        }
        return userDomains;
    }
}
