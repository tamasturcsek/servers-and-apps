package hu.mol.saa.repos

import hu.mol.saa.Server
import org.springframework.data.jpa.repository.JpaRepository

/**
 * Created by tamasturcsek on 2015. 09. 24..
 */
public interface ServerRepo extends JpaRepository<Server, Long> {
    public List<Server> findByIstombstoned(boolean istombstoned)
    public List<Server> findByName(String name)
    public List<Server> findByNameAndIstombstoned(String name, boolean isTombstoned)
}