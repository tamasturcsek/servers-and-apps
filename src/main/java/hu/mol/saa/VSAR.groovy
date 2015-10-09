package hu.mol.saa

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

/**
 * Created by tamasturcsek on 2015. 09. 24..
 */
@Entity
@Table(name = "V_ServerApplication", schema = "dbo")
class VSAR {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id                                             //kell egy id kulcs a relations táblába és a view-ban is jelenjen meg

    public static final String SID = "serverID"
    public static final String AID = "applicationID"
    public static final String SNAME = "servername"
    public static final String ANAME = "applicationname"
    //public static final String ISTOMBSTONED = "istombstoned"

    long serverID
    long applicationID
    String servername
    String applicationname
    //boolean istombstoned

    VSAR() {
    }

    VSAR(int serverID, int applicationID, String servername, String applicationname/*, boolean istombstoned*/) {
        this.serverID = serverID
        this.applicationID = applicationID
        this.servername = servername
        this.applicationname = applicationname
        //this.istombstoned = istombstoned
    }

    VSAR(int serverID, int applicationID) {
        this.serverID = serverID
        this.applicationID = applicationID
    }


    @Override
    public String toString() {
        return applicationname + " @ " + servername
    }
}
