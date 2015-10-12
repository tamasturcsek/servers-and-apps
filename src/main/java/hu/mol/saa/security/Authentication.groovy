package hu.mol.saa.security

import com.vaadin.server.themeutils.SASSAddonImportFileCreator
import com.vaadin.ui.Notification
import hu.mol.saa.Logging
import hu.mol.saa.SAAUI
import hu.mol.saa.UIEventBus
import hu.mol.saa.User
import com.vaadin.server.VaadinSession
import hu.mol.saa.repos.UserRepo;

public class Authentication {
    UserRepo userRepo

    public Authentication(UserRepo userRepo) {
        this.userRepo = userRepo
    }

    public boolean login(String username, String password) {
        def u = userRepo.findByUsername(username)
        if (!u.isEmpty()) {
            if ((u.get(0).password).equals(password)) {
                VaadinSession.getCurrent().setAttribute("username", username)
                UIEventBus.post(new LoginEvent(username))
                Logging.successLogon(username)
                SAAUI.getCurrent().initMainScreen()
                return true
            } else {
                Notification.show("Failed to login", "Password is wrong", Notification.Type.ERROR_MESSAGE)
                Logging.failedLogon(username)
            }
        } else {
            Notification.show("Failed to login", "User does not exist", Notification.Type.ERROR_MESSAGE)
            Logging.failedLogon(username)
        }
        return false
    }

    public void logout() {
        if (isAuthenticated()) {
            String username = VaadinSession.getCurrent().getAttribute("username")
            VaadinSession.getCurrent().setAttribute("username", null);
            SAAUI.getCurrent().navigator.navigateTo(SAAUI.SRVWAPPVIEW)
            SAAUI.getCurrent().logout()
            Logging.successLogoff(username)
        }
        UIEventBus.post(new LoginEvent(null));
    }

    public boolean isAuthenticated() {
        return getUsernameFromSession() != null;
    }

    public static String getUsernameFromSession() {
        return (String) VaadinSession.getCurrent().getAttribute("username");
    }

    public User getUser() {
        return (userRepo.findByUsername(getUsernameFromSession())).get(0);
    }
}