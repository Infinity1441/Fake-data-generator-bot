package dev.baxtigul.java_telegram_bots.config;

import dev.baxtigul.java_telegram_bots.domains.UserDomain;
import dev.baxtigul.java_telegram_bots.dto.Response;
import dev.baxtigul.java_telegram_bots.state.DefaultState;
import dev.baxtigul.java_telegram_bots.utils.factory.SendMessageFactory;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import static dev.baxtigul.java_telegram_bots.config.ThreadSafeBeansContainer.userService;
import static dev.baxtigul.java_telegram_bots.config.ThreadSafeBeansContainer.userState;

public class InitializerConfiguration {

    public static void init() {
        Response<List<UserDomain>> response = userService.get().getAllUsers();
        if ( !response.isSuccess() ) {
            // TODO: 05/02/23 log
            System.err.println(response.getDeveloperErrorMessage());
            System.exit(-1);
        } else {
            List<UserDomain> users = response.getBody();
            users.forEach((user) -> {
                userState.put(user.getChatID(), DefaultState.MAIN_STATE);
                CompletableFuture.runAsync(() -> TelegramBotConfiguration.get().execute(
                        SendMessageFactory.sendMessageWithMainMenu(user.getChatID(), "Feel free to use a bot\uD83D\uDE07", user.getLanguage()))
                );
            });
        }
    }
}
