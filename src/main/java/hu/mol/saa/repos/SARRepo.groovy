package hu.mol.saa.repos

import hu.mol.saa.SAR
import org.springframework.data.jpa.repository.JpaRepository

/**
 * Created by tamasturcsek on 2015. 10. 01..
 */
interface SARRepo extends JpaRepository<SAR, Long> {
    public List<SAR> findByServerID(long id)
    public List<SAR> findByApplicationID(long id)
    public List<SAR> findByApplicationIDAndServerID(int id, int id2)
 }