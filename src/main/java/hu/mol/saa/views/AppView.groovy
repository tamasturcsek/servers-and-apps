package hu.mol.saa.views

import com.vaadin.data.fieldgroup.PropertyId
import com.vaadin.data.util.BeanItemContainer
import com.vaadin.event.ShortcutAction
import com.vaadin.event.ShortcutListener
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener
import com.vaadin.server.VaadinSession
import com.vaadin.ui.*
import com.vaadin.ui.themes.ValoTheme
import hu.mol.saa.Application
import hu.mol.saa.CExtFilterTable
import hu.mol.saa.CFieldGroup
import hu.mol.saa.Logging
import hu.mol.saa.SAAUI
import hu.mol.saa.repos.ApplicationRepo
import hu.mol.saa.security.Authentication
import javafx.scene.input.KeyCode
import org.asi.ui.extfilteringtable.ExtFilterTable
import org.springframework.dao.EmptyResultDataAccessException
import org.vaadin.dialogs.ConfirmDialog

import java.lang.reflect.Array
import java.text.DateFormat
import java.text.SimpleDateFormat

/**
 * Created by tamasturcsek on 2015. 09. 24..
 */
class AppView extends CustomComponent implements View {
    HorizontalLayout root = new HorizontalLayout()
    private CFieldGroup<Application> fieldGroup = new CFieldGroup<>(Application.class)
    private Label updateLabel = new Label("Add new Application")
    private Label deleteLabel = new Label("Remove selected Application(s)")
    private ExtFilterTable table = new CExtFilterTable(new BeanItemContainer(Application.class, appRepo.findAll()))
    private Button btDelete
    private FormLayout newAppForm
    @PropertyId(Application.APPNAME)
    private TextField appNameTf
    @PropertyId(Application.APPDESCRIPTION)
    private TextArea appDescription
    private Button btAdd


    private ApplicationRepo appRepo
    private ComboBox appBox
    private String appName
    private CheckBox chkBox
    HorizontalLayout filters
    VerticalLayout update
    Panel updatePanel
    List<Application> selectedRecords = new ArrayList<>()


    AppView(ApplicationRepo appRepo) {
        this.appRepo = appRepo
        btDelete = new Button("Remove")
        btDelete.addClickListener(new Button.ClickListener() {
            @Override
            void buttonClick(Button.ClickEvent clickEvent) {
                ArrayList<Application> selectedRecords = table.getValue();
                if (selectedRecords.isEmpty()) {
                    Notification.show("Select application first!", Notification.Type.WARNING_MESSAGE)
                } else {
                    showConfirmDialog(selectedRecords)
                }
            }

        })
        newAppForm = new FormLayout()
        appNameTf = new TextField("App Name")
        appDescription = new TextArea("Description")
        btAdd = new Button("Add")
        btAdd.addClickListener(new Button.ClickListener() {
            @Override
            void buttonClick(Button.ClickEvent clickEvent) {
                addRecord()
            }
        })
        newAppForm.addComponent(appNameTf)
        newAppForm.addComponent(appDescription)
        newAppForm.addComponent(btAdd)
        appBox = new ComboBox("Filter", appRepo.findAll())
        chkBox = new CheckBox("Only active servers")
        filters = new HorizontalLayout()

        update = new VerticalLayout()

        updatePanel = new Panel("", update)
        updatePanel.addShortcutListener(new ShortcutListener("",ShortcutAction.KeyCode.ENTER,null) {
            @Override
            public void handleAction(Object sender, Object target) {
                addRecord()
            }
        })
    }


    @Override
    void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        setSizeFull()
        root.setSpacing(true)
        root.addComponent(table)
        root.setSizeFull()
        root.setExpandRatio(table, 1.0f)
        updatePanel.setSizeUndefined()
        root.addComponent(updatePanel)

        update.setSpacing(true)
        update.setMargin(true)


        updateLabel.setStyleName(ValoTheme.LABEL_LARGE)
        deleteLabel.setStyleName(ValoTheme.LABEL_LARGE)
        newAppForm.setComponentAlignment(btAdd, Alignment.MIDDLE_RIGHT)
        update.addComponent(updateLabel)
        update.addComponent(newAppForm)

        update.addComponent(deleteLabel)
        btDelete.setStyleName(ValoTheme.BUTTON_DANGER)
        update.addComponent(btDelete)
        root.setComponentAlignment(updatePanel, Alignment.MIDDLE_RIGHT)

        filters.setSpacing(true)
        filters.addComponent(appBox)

        table.setVisibleColumns(Application.APPNAME, Application.APPDESCRIPTION, Application.CREATEDBY,
                Application.CREATIONDATE/*, Application.LASTMODFIEDBY, Application.LASTMODIFICATIONDATE*/)
        table.setColumnHeader(Application.APPNAME, "App Name")
        table.setColumnHeader(Application.APPDESCRIPTION, "Description")
        table.setColumnHeader(Application.CREATEDBY, "Created by")
        table.setColumnHeader(Application.CREATIONDATE, "Creation Date")
        table.setFilterFieldVisible(Application.CREATIONDATE,false)
        /*table.setColumnHeader(Application.LASTMODFIEDBY, "Last Modified by")
        table.setColumnHeader(Application.LASTMODIFICATIONDATE, "Modification Date")*/


        fieldGroup.setItemDataSource(new Application())
        fieldGroup.bindMemberFields(this)
        refreshTable()

        setCompositionRoot(root)
    }

    def refreshTable() {
        def records = appRepo.findAll()
        def c = (BeanItemContainer) table.getContainerDataSource()
        c.removeAllItems()
        c.addAll(records)

    }

    void addRecord() {
        fieldGroup.commit();
        Application app = fieldGroup.itemDataSource.bean
        if (!app.applicationname.equals(null) && !app.applicationname.equals("")) {
            if ((appRepo.findByApplicationname(app.applicationname)).isEmpty()) {
                app.createdby = Authentication.getUsernameFromSession()
                app.lastmodifiedby = Authentication.getUsernameFromSession()
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                app.creationdate = new Date(dateFormat.format(date))
                app.lastmodificationdate = new Date(dateFormat.format(date))
                appRepo.save(app)
                fieldGroup.setItemDataSource(new Application())
                fieldGroup.bindMemberFields(this)
                Logging.addApp(VaadinSession.getCurrent().getAttribute("username"),app.toString())
            } else {
                Notification.show("Application already exist", Notification.Type.WARNING_MESSAGE)
            }
            refreshTable()
        } else {
            Notification.show("Cannot add new application", "Application name is required", Notification.Type.ERROR_MESSAGE)
        }
        appNameTf.clear()
        appDescription.clear()
    }

    void showConfirmDialog(ArrayList selectedRecords) {
        ConfirmDialog cd = ConfirmDialog.show(SAAUI.current, "Confirmation",
                "Are you sure you want to delete the following record(s)?", "YES", "NO", new ConfirmDialog.Listener() {

            public void onClose(ConfirmDialog dialog) {
                if (dialog.isConfirmed()) {
                    deleteRecords(selectedRecords)
                }
                table.setValue(null)
            }
        })
        cd.setHeight("220px")
        cd.setDraggable(false)
        cd.setClosable(false)
        VerticalLayout root = new VerticalLayout()
        root.setSizeFull()
        root.setMargin(true)
        HorizontalLayout buttons = new HorizontalLayout()
        String s = selectedRecords.get(0)
        for (int i = 1; i < selectedRecords.size(); ++i) {
            s = s + ", " + selectedRecords.get(i)
        }

        Label records = new Label(s)
        records.setSizeFull()
        buttons.addComponent(cd.getCancelButton())
        buttons.addComponent(cd.getOkButton())
        buttons.setSpacing(true)
        buttons.setSizeUndefined()
        root.addComponent(new Label(cd.getMessage()))
        root.addComponent(records)
        root.addComponent(buttons)
        root.setComponentAlignment(buttons, Alignment.BOTTOM_RIGHT)
        cd.setContent(root)
    }

    void deleteRecords(ArrayList selectedRecords) {
        for (Application o : selectedRecords)
            try {
                appRepo.delete(o.applicationID)
                Logging.removeApp(VaadinSession.getCurrent().getAttribute("username"),o.toString())
            } catch (org.springframework.dao.DataIntegrityViolationException e) {
                //vagy egy listába az összeset és 1 értesítés..
                Notification.show("Cannot delete " + o.applicationname, "The selected application is assigned to server", Notification.Type.ERROR_MESSAGE)
            } catch (EmptyResultDataAccessException e) {
            }
        refreshTable()
    }

}
