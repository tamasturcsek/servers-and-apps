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
@Table(name = "serverapplicationrelations", schema = "dbo")
class SAR {
    public static final String SID = "serverID"
    public static final String AID = "applicationID"

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id
    //kell egy id kulcs a relations táblába és a view-ban is jelenjen meg


    int serverID
    int applicationID

    SAR() {
    }

    SAR(long serverID, long applicationID) {
        this.serverID = serverID
        this.applicationID = applicationID
    }


    @Override
    public String toString() {
        return serverID + "\t" + applicationID
    }
}
