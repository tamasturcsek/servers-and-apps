package hu.mol.saa

import com.vaadin.ui.Button
import com.vaadin.ui.themes.ValoTheme

/**
 * Created by tamasturcsek on 2015. 09. 24..
 */
class navigatorListener implements Button.ClickListener {
    String target
    Button btActive
    List<Button> others

    navigatorListener(String s, Button btActive, List<Button> others) {
        target = s
        this.btActive = btActive
        this.others = others
    }

    @Override
    void buttonClick(Button.ClickEvent clickEvent) {
        SAAUI.current.navigator.navigateTo(target)
        btActive.removeStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED)
        btActive.addStyleName(ValoTheme.BUTTON_FRIENDLY)
        for (Button b : others)
            b.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED)
    }
}
