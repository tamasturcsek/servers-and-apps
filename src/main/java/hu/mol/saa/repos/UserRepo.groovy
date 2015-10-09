package hu.mol.saa.repos

import hu.mol.saa.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long>{
    public List<User> findByUsername(String username)
    public List<User> findByPassword(String password)
    public List<User> findByUsernameAndPassword(String username, String password)
}
