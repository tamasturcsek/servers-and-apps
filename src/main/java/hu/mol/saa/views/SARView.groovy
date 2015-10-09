package hu.mol.saa.views

import com.vaadin.data.fieldgroup.PropertyId
import com.vaadin.data.util.BeanItemContainer
import com.vaadin.event.ShortcutAction
import com.vaadin.event.ShortcutListener
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener
import com.vaadin.ui.*
import com.vaadin.ui.themes.ValoTheme
import hu.mol.saa.Application
import hu.mol.saa.CExtFilterTable
import hu.mol.saa.CFieldGroup
import hu.mol.saa.SAAUI
import hu.mol.saa.SAR
import hu.mol.saa.Server
import hu.mol.saa.VSAR
import hu.mol.saa.repos.ApplicationRepo
import hu.mol.saa.repos.SARRepo
import hu.mol.saa.repos.ServerRepo
import hu.mol.saa.repos.VSARRepo
import org.asi.ui.extfilteringtable.ExtFilterTable
import org.springframework.dao.EmptyResultDataAccessException
import org.vaadin.dialogs.ConfirmDialog

/**
 * Created by tamasturcsek on 2015. 09. 24..
 */
class SARView extends VerticalLayout implements View {
    private CFieldGroup<SAR> fieldGroup = new CFieldGroup<>(SAR.class)
    private Label updateLabel = new Label("Add new Relation")
    private Label deleteLabel = new Label("Remove selected Relation(s)")
    private ExtFilterTable table = new CExtFilterTable(new BeanItemContainer(VSAR.class, vsarRepo.findAll()))
    private Button btDelete;

    private VSARRepo vsarRepo
    private ServerRepo srvRepo
    private ApplicationRepo appRepo
    private SARRepo sarRepo

    // @PropertyId(VSAR.SNAME)
    private ComboBox serversBox
    private String serverName

    //@PropertyId(VSAR.ANAME)
    private ComboBox appsBox
    private String appName
    FormLayout filters
    HorizontalLayout root
    VerticalLayout query
    VerticalLayout update
    Panel updatePanel
    private Button btAdd

    SARView(SARRepo sarRepo, VSARRepo vsarRepo, ServerRepo srvRepo, ApplicationRepo appRepo) {
        this.sarRepo = sarRepo
        this.vsarRepo = vsarRepo
        this.appRepo = appRepo
        this.srvRepo = srvRepo
        btDelete = new Button("Remove")
        btDelete.addStyleName(ValoTheme.BUTTON_DANGER)
        btDelete.addClickListener(new Button.ClickListener() {
            @Override
            void buttonClick(Button.ClickEvent clickEvent) {
                ArrayList<VSAR> selectedRecords = table.getValue();
                if (selectedRecords.isEmpty()) {
                    Notification.show("Select relation first!", Notification.Type.WARNING_MESSAGE)
                } else {
                    showConfirmDialog(selectedRecords)
                }
            }
        })
        serversBox = new ComboBox("Servers", new BeanItemContainer(Server.class, srvRepo.findByIstombstoned(false)))
        // kell a tombstoned is?!
        appsBox = new ComboBox("Applications", new BeanItemContainer(Application.class, appRepo.findAll()))
        filters = new FormLayout()
        root = new HorizontalLayout()
        query = new VerticalLayout()
        update = new VerticalLayout()
        updatePanel = new Panel("", update)
        updatePanel = new Panel("", update)
        updatePanel.addShortcutListener(new ShortcutListener("",ShortcutAction.KeyCode.ENTER,null) {
            @Override
            public void handleAction(Object sender, Object target) {
                addRecord()
            }
        })
        btAdd = new Button("Add")
        btAdd.addClickListener(new Button.ClickListener() {
            @Override
            void buttonClick(Button.ClickEvent clickEvent) {
                addRecord()
            }
        })
    }


    @Override
    void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        setSizeFull()
        root.setSizeFull()
        addComponent(root)

        updatePanel.setSizeUndefined()


        root.setSpacing(true)
        root.addComponent(query)
        root.addComponent(updatePanel)
        root.setExpandRatio(query, 1.0f)
        update.setSpacing(true)
        query.setSpacing(true)
        query.setSizeFull()

        updateLabel.setStyleName(ValoTheme.LABEL_LARGE)
        deleteLabel.setStyleName(ValoTheme.LABEL_LARGE)
        update.addComponent(updateLabel)
        update.addComponent(filters)
        update.addComponent(deleteLabel)
        update.addComponent(btDelete)
        update.setMargin(true)
        root.setComponentAlignment(updatePanel, Alignment.MIDDLE_CENTER)


        filters.addComponent(serversBox)
        filters.addComponent(appsBox)
        filters.addComponent(btAdd)
        filters.setComponentAlignment(btAdd, Alignment.BOTTOM_CENTER)


        table.setVisibleColumns(VSAR.SNAME, VSAR.ANAME)
        table.setColumnHeader(VSAR.SNAME, "Server Name")
        table.setColumnHeader(VSAR.ANAME, "Application Name")

        query.addComponent(table)
        query.setComponentAlignment(table, Alignment.MIDDLE_CENTER)
        fieldGroup.setItemDataSource(new SAR())
        fieldGroup.bindMemberFields(this)
        refreshCBoxes()
        refreshTable()
    }

    def refreshTable() {
        def records
        if (serverName == null && appName == null) {
            records = vsarRepo.findAll()
        } else if (serverName != null && appName == null) {
            records = vsarRepo.findByServername(serverName)
        } else if (serverName == null && appName != null) {
            records = vsarRepo.findByApplicationname(appName)
        } else {
            records = vsarRepo.findByServernameAndApplicationname(serverName, appName)
        }

        def c = (BeanItemContainer) table.getContainerDataSource()
        c.removeAllItems()
        c.addAll(records)

    }

    def refreshCBoxes() {
        def c = (BeanItemContainer) serversBox.getContainerDataSource()
        c.removeAllItems()
        c.addAll(srvRepo.findByIstombstoned(false))
        def c2 = (BeanItemContainer) appsBox.getContainerDataSource()
        c2.removeAllItems()
        c2.addAll(appRepo.findAll())
    }

    void addRecord() {
        fieldGroup.commit();
        SAR s = fieldGroup.itemDataSource.bean
        if (serversBox.getValue() != null) {
            if (appsBox.getValue() != null) {
                def applicationID = ((appRepo.findByApplicationname((appsBox.getValue()).applicationname)).applicationID).get(0)
                def serverID = ((srvRepo.findByName((serversBox.getValue()).name)).serverid).get(0)
                s.applicationID = applicationID
                s.serverID = serverID
                if ((sarRepo.findByApplicationIDAndServerID(s.applicationID, s.serverID)).isEmpty()) {
                    sarRepo.save(s)
                    fieldGroup.setItemDataSource(new SAR())
                    fieldGroup.bindMemberFields(this)
                } else {
                    Notification.show("Relation already exist", Notification.Type.WARNING_MESSAGE)
                }
                refreshTable()
                appsBox.clear()
                serversBox.clear()
            } else
                Notification.show("Select application first!", Notification.Type.WARNING_MESSAGE)
        } else
            Notification.show("Select server first!", Notification.Type.WARNING_MESSAGE)

    }

    void showConfirmDialog(ArrayList<VSAR> selectedRecords) {
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
        for (VSAR o : selectedRecords)
            try {
                sarRepo.delete(o.id)
            } catch (EmptyResultDataAccessException e) {}
        refreshTable()
    }

}
