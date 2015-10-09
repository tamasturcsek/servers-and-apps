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
@Table(name = "applications", schema = "dbo")
class Application {
    public static final String APPID = "applicationID"
    public static final String APPNAME = "applicationname"
    public static final String APPDESCRIPTION = "applicationdescription"
    public static final String CREATEDBY = "createdby"
    public static final String CREATIONDATE = "creationdate"
    public static final String LASTMODFIEDBY = "lastmodifiedby"
    public static final String LASTMODIFICATIONDATE = "lastmodificationdate"


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long applicationID

    String applicationname
    String applicationdescription
    String createdby
    Date creationdate
    String lastmodifiedby
    Date lastmodificationdate

    Application(String applicationname, String applicationdescription, String createdby, Date creationdate, String lastmodifiedby, Date lastmodificationdate) {
        this.applicationname = applicationname
        this.applicationdescription = applicationdescription
        this.createdby = createdby
        this.creationdate = creationdate
        this.lastmodifiedby = lastmodifiedby
        this.lastmodificationdate = lastmodificationdate
    }

    Application() {
    }


    @Override
    public String toString() {
        return applicationname
    }
}
