package hu.mol.saa

import com.vaadin.event.ShortcutAction
import com.vaadin.server.FontAwesome
import com.vaadin.server.Resource
import com.vaadin.server.Responsive
import com.vaadin.server.ThemeResource
import com.vaadin.shared.ui.MarginInfo
import com.vaadin.ui.*
import com.vaadin.ui.themes.ValoTheme
import hu.mol.saa.SAAUI
import hu.mol.saa.repos.UserRepo
import hu.mol.saa.security.Authentication

/**
 * Created by Tamás on 2015. 09. 15..
 */

class LoginScreen extends CustomComponent {
    private VerticalLayout root
    private TextField userLoginField = new TextField("User")
    private PasswordField passwordLoginField = new PasswordField("Password")
    private Button btLogin = new Button("OK")
    private Authentication authentication
    // Serve the image from the theme
    Resource res = new ThemeResource("mol.png");


// Display the image without caption
    Image image = new Image(null, res);


    public LoginScreen(Authentication authentication) {
        this.authentication = authentication
        setSizeFull();
        Component loginForm = buildLoginForm();
        root = new VerticalLayout();
        root.setSizeFull();
        root.addComponent(loginForm);
        root.setComponentAlignment(loginForm, Alignment.MIDDLE_CENTER);
        setCompositionRoot(root);
        focus()


    }

    private Component buildLoginForm() {
        final VerticalLayout loginPanel = new VerticalLayout()
        HorizontalLayout hl = new HorizontalLayout()
        VerticalLayout vl = new VerticalLayout()
        def label1 = new Label("MOL MOMInfo")
        def label2 = new Label("Servers and Applications")
        label1.addStyleName(ValoTheme.LABEL_H2)
        vl.addComponent(label1)
        vl.addComponent(label2)
        hl.addComponent(vl)
        hl.addComponent(image)
        hl.setComponentAlignment(vl,Alignment.MIDDLE_CENTER)
        hl.setComponentAlignment(image,Alignment.TOP_RIGHT)
        hl.setSizeFull()
        hl.setMargin(new MarginInfo(false, true, false, true))
        image.setHeight("110px")
        image.setWidth("110px")
        loginPanel.addComponent(hl)
        loginPanel.setSizeUndefined()
        loginPanel.setSpacing(true)
        loginPanel.setMargin(true)
        Responsive.makeResponsive(loginPanel)
        loginPanel.addComponent(buildFields())
        Panel panel = new Panel(null, loginPanel)
        panel.setSizeUndefined()

        return panel
    }

    private Component buildFields() {
        def fields = new HorizontalLayout();
        fields.setSpacing(true)
        fields.setMargin(new MarginInfo(false, true, false, true))
        userLoginField = new TextField("User");
        userLoginField.setIcon(FontAwesome.USER)
        userLoginField.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON)
        userLoginField.setNullRepresentation("")
        passwordLoginField.setIcon(FontAwesome.LOCK)
        passwordLoginField.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON)
        passwordLoginField.setNullRepresentation("")
        btLogin.addStyleName(ValoTheme.BUTTON_PRIMARY);
        btLogin.setClickShortcut(ShortcutAction.KeyCode.ENTER)
        userLoginField.focus();
        btLogin.addClickListener({ e ->
            authentication.login(userLoginField.getValue(), passwordLoginField.getValue())
            userLoginField.clear()
            passwordLoginField.clear()
        });

        fields.addComponents(userLoginField, passwordLoginField, btLogin);
        fields.setComponentAlignment(btLogin, Alignment.BOTTOM_RIGHT);

        return fields;
    }

    @Override
    protected void focus() {
        userLoginField.focus()
    }

}
