package hu.mol.saa.repos

import hu.mol.saa.VSAR
import org.springframework.data.jpa.repository.JpaRepository

/**
 * Created by tamasturcsek on 2015. 09. 24..
 */


interface VSARRepo extends JpaRepository<VSAR, Long> {
    public List<VSAR> findByServername(String server)
    //public List<VSAR> findByIstombstoned(boolean showOnlyActive)
    //public List<VSAR> findByServernameAndIstombstoned(String server, boolean showOnlyActive)
    //public List<VSAR> findByServernameAndApplicationNameAndIstombstoned(String server, String application, boolean showOnlyActive)
    public List<VSAR> findByServerID(long id)
    public List<VSAR> findByApplicationname(String app)
    public List<VSAR> findByServernameAndApplicationname(String server, String app)
}