package hu.mol.saa.repos

import hu.mol.saa.Application
import org.springframework.data.jpa.repository.JpaRepository

/**
 * Created by tamasturcsek on 2015. 09. 24..
 */
public interface ApplicationRepo extends JpaRepository<Application, Long> {
    public List<Application> findByApplicationname(String applicationname)
}
