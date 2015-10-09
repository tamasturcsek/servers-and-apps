package hu.mol.saa

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table
import java.util.Date

/**
 * Created by tamasturcsek on 2015. 09. 24..
 */
@Entity
@Table(name = "allservers", schema = "dbo")
class Server {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long serverid

    public static final String SERVERID = "Serverid"
    public static final String NAME = "name"
    public static final String CREATEDBY = "createdby"
    public static final String CREATIONDATE = "creationdate"
    public static final String LASTMODFIEDBY = "lastmodifiedby"
    public static final String LASTMODIFICATIONDATE = "lastmodificationdate"
    public static final String DESCRIPTION = "description"
    public static final String PLATFORMID = "platformID"
    public static final String ISTOMBSTONED = "istombstoned"


    String name
    String createdby
    Date creationdate
    String lastmodifiedby
    Date lastmodificationdate
    String description
    String platformID
    boolean istombstoned

    Server(String name, String createdby, Date creationdate, String lastmodifiedby, Date lastmodificationdate, String description, String platformID, boolean istombstoned) {
        this.name = name
        this.createdby = createdby
        this.creationdate = creationdate
        this.lastmodifiedby = lastmodifiedby
        this.lastmodificationdate = lastmodificationdate
        this.description = description
        this.platformID = platformID
        this.istombstoned = istombstoned
    }

    Server() {
    }


    @Override
    public String toString() {
        return name
    }
}
