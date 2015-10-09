package hu.mol.saa.views

import com.vaadin.data.Property
import com.vaadin.data.util.BeanItemContainer
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener
import com.vaadin.ui.*
import hu.mol.saa.CExtFilterTable
import hu.mol.saa.Server
import hu.mol.saa.repos.ServerRepo
import org.asi.ui.extfilteringtable.ExtFilterTable

/**
 * Created by tamasturcsek on 2015. 09. 24..
 */
class ServerView extends VerticalLayout implements View {
    private ServerRepo srvRepo
    private ExtFilterTable table = new CExtFilterTable(new BeanItemContainer(Server.class, srvRepo.findAll()))


    ServerView(ServerRepo srvRepo) {
        this.srvRepo = srvRepo
    }


    @Override
    void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        setSizeFull()
        setSpacing(true)

        table.setVisibleColumns(Server.NAME, Server.DESCRIPTION, Server.CREATEDBY, Server.CREATIONDATE,
                Server.LASTMODFIEDBY, Server.LASTMODIFICATIONDATE, Server.PLATFORMID, Server.ISTOMBSTONED)
        table.setColumnHeader(Server.NAME, "Server Name")
        table.setColumnHeader(Server.DESCRIPTION, "Description")
        table.setColumnHeader(Server.CREATEDBY, "Created by")
        table.setColumnHeader(Server.CREATIONDATE, "Creation Date")
        table.setColumnHeader(Server.LASTMODFIEDBY, "Last Modified by")
        table.setColumnHeader(Server.LASTMODIFICATIONDATE, "Modification Date")
        table.setColumnHeader(Server.PLATFORMID, "Platform ID")
        table.setColumnHeader(Server.ISTOMBSTONED, "isRetired")
        table.setFilterFieldVisible(Server.CREATIONDATE, false)
        table.setFilterFieldVisible(Server.LASTMODIFICATIONDATE, false)

        addComponent(table)
        setComponentAlignment(table,Alignment.MIDDLE_CENTER)
    }

}