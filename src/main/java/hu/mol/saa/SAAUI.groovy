package hu.mol.saa

import com.vaadin.annotations.Theme
import com.vaadin.annotations.VaadinServletConfiguration
import com.vaadin.annotations.Widgetset
import com.vaadin.navigator.Navigator
import com.vaadin.server.VaadinRequest
import com.vaadin.server.VaadinSession
import com.vaadin.shared.ui.MarginInfo
import com.vaadin.ui.*
import com.vaadin.ui.themes.ValoTheme
import hu.mol.saa.repos.ApplicationRepo
import hu.mol.saa.repos.SARRepo
import hu.mol.saa.repos.ServerRepo
import hu.mol.saa.repos.UserRepo
import hu.mol.saa.repos.VSARRepo
import hu.mol.saa.security.Authentication
import hu.mol.saa.views.AppView
import hu.mol.saa.views.NewRelationView
import hu.mol.saa.views.SARView
import hu.mol.saa.views.ServerView
import org.springframework.beans.factory.annotation.Autowired
import org.vaadin.spring.annotation.VaadinUI

/**
 * Created by tamasturcsek on 2015. 09. 24..
 */
@VaadinUI
@Theme("metrotheme")
@Widgetset("hu.mol.saa.Widgetset")
class SAAUI extends UI {
    @Autowired
    private UserRepo userRepo
    @Autowired
    private ApplicationRepo appRepo
    @Autowired
    private ServerRepo srvRepo
    @Autowired
    private VSARRepo vsarRepo
    @Autowired
    private SARRepo sarRepo

    final UIEventBus eventbus = new UIEventBus()
    private Authentication authentication

    VerticalLayout root = new VerticalLayout()
    HorizontalLayout menu = new HorizontalLayout()
    VerticalLayout container = new VerticalLayout()
    Navigator navigator

    private Button btSrvWApp = new Button("Servers with apps")
    private Button btSrv = new Button("Servers")
    private Button btApp = new Button("Apps")
    private Button btLogout = new Button("Logout")
    private Button btAdmin = new Button("Admin Users")


    public static final String SRVWAPPVIEW = ""
    public static final String SERVERVIEW = "servers"
    public static final String APPVIEW = "apps"
    public static final String ADMINVIEW = "admin"

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Budapest"));
        Locale huLocale = new Locale("hu", "HU");
        setLocale(huLocale);
        VaadinSession.getCurrent().setLocale(huLocale);
        getPage().setTitle("MOL Servers and Applications")

        navigator = new Navigator(this, container)
        // Create a navigator to control the views
        // Create and register the views
        navigator.addView(SRVWAPPVIEW, new SARView(sarRepo, vsarRepo, srvRepo, appRepo));
        navigator.addView(SERVERVIEW, new ServerView(srvRepo));
        navigator.addView(APPVIEW, new AppView(appRepo))

        UIEventBus.register(this);
        authentication = new Authentication(userRepo)
        initialize()

    }

    public void initialize() {
        setSizeFull()
        if (!authentication.isAuthenticated()) {
            setupLoginScreen()
        } else {
            setupMainScreen()
        }
    }

    void setupMainScreen() {

        root.addComponent(menu)
        menu.setSpacing(true)
        menu.setMargin(new MarginInfo(false, true, false, true))

        root.addComponent(container)
        container.setSizeFull()
        container.setMargin(true)
        root.setExpandRatio(container, 1.0f)

        btSrvWApp.addStyleName(ValoTheme.BUTTON_LARGE)
        btSrvWApp.addClickListener(new navigatorListener(SRVWAPPVIEW, btSrvWApp, [btApp, btSrv]))
        btSrvWApp.setSizeFull()
        btSrv.addStyleName(ValoTheme.BUTTON_LARGE)
        btSrv.addClickListener(new navigatorListener(SERVERVIEW, btSrv, [btSrvWApp, btApp]))
        btSrv.setSizeFull()
        btApp.addStyleName(ValoTheme.BUTTON_LARGE)
        btApp.addClickListener(new navigatorListener(APPVIEW, btApp, [btSrvWApp, btSrv]))
        btApp.setSizeFull()
        btAdmin.addStyleName(ValoTheme.BUTTON_TINY)
        btAdmin.addClickListener(new Button.ClickListener() {
            @Override
            void buttonClick(Button.ClickEvent clickEvent) {
                navigator.addView(ADMINVIEW, new NewRelationView())
                navigator.navigateTo(ADMINVIEW)
                setActiveButton(null, [btSrvWApp, btSrv, btApp])
            }
        })
        btAdmin.setSizeUndefined()
        btLogout.addClickListener(new Button.ClickListener() {
            @Override
            void buttonClick(Button.ClickEvent clickEvent) {
                authentication.logout()
                initialize()
            }
        })
        btLogout.setSizeUndefined()
        btLogout.addStyleName(ValoTheme.BUTTON_SMALL)
        btLogout.addStyleName(ValoTheme.BUTTON_PRIMARY)


        menu.addComponent(btSrvWApp)
        menu.setExpandRatio(btSrvWApp, 1.0f)
        menu.addComponent(btSrv)
        menu.setExpandRatio(btSrv, 1.0f)
        menu.addComponent(btApp)
        menu.setExpandRatio(btApp, 1.0f)
        if (authentication.getUser().isAdmin()) {
            menu.addComponent(btAdmin)
            menu.setComponentAlignment(btAdmin, Alignment.MIDDLE_RIGHT)
        }
        menu.addComponent(btLogout)
        menu.setComponentAlignment(btLogout, Alignment.MIDDLE_RIGHT)

        menu.setSizeUndefined()
        menu.setWidth("100%")


        if (navigator.getState().equals(SRVWAPPVIEW)) {
            setActiveButton(btSrvWApp, [btSrv, btApp])
        } else if (navigator.getState().equals(SERVERVIEW)) {
            setActiveButton(btSrv, [btSrvWApp, btApp])
        } else if (navigator.getState().equals(APPVIEW)) {
            setActiveButton(btApp, [btSrv, btSrvWApp])
        } else if (navigator.getState().equals(ADMINVIEW)){
            setActiveButton(null, [btSrvWApp, btSrv, btApp])
        }

        root.setSizeFull()
        root.setMargin(true)
        root.setSpacing(true)
        setContent(root)
    }

    public void initMainScreen() {
        setActiveButton(btSrvWApp, [btSrv, btApp])
        navigator.navigateTo(SAAUI.SRVWAPPVIEW)
        initialize()
    }

    public void logout() {
        menu.removeComponent(btAdmin)
        navigator.removeView(ADMINVIEW)
    }

    void setupLoginScreen() {
        setContent(new LoginScreen(authentication));
    }

    def setActiveButton(Button button, ArrayList<Button> buttons) {
        if (button != null)
            button.addStyleName(ValoTheme.BUTTON_FRIENDLY)
        for (Button b : buttons)
            b.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED)
    }

}
