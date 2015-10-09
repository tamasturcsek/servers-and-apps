package hu.mol.saa

import hu.mol.saa.ldap.RetrieveUserAttributes
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class SAAApplication {

    static void main(String[] args) {
        RetrieveUserAttributes retrieveUserAttributes = new RetrieveUserAttributes()
        retrieveUserAttributes.getUserBasicAttributes("testuser", retrieveUserAttributes.getLdapContext())
        SpringApplication.run SAAApplication, args
    }
}
