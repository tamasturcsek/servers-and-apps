package hu.mol.saa

import javax.persistence.*

@Entity
@Table(name = "authenticatedusers", schema = "dbo")
public class User {
    public static final String USERNAME = "username"
    public static final String PASSWORD = "password"


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id


    private String username
    private String password
    private boolean isAdmin

    public User(String username, String password, boolean isAdmin) {
        this.username = username
        this.password = password
        this.isAdmin = isAdmin
    }

    public User() {}


    public void setName(String name) {
        this.name = name
    }

    public void setPassword(String password) {
        this.password = password
    }

    void setMail(String mail) {
        this.mail = mail
    }

    public String getPassword() {
        return password
    }

    boolean isAdmin() {
        return isAdmin
    }

    @Override
    public String toString() {
        return username
    }
}
