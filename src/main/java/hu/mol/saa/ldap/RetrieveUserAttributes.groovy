package hu.mol.saa.ldap

import org.apache.tomcat.jni.User

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

public class RetrieveUserAttributes {
    private final String INITCTX = "com.sun.jndi.ldap.LdapCtxFactory";
    private final String SEC_AUTH = "Simple"
    private final String PRINCIPAL = "cn=John,ou=Users,o=IT,dc=QuizPortal";
    private final String PW = "password";
    private final String HOST = "ldap://localhost:389";

    public LdapContext getLdapContext() {
        LdapContext ctx = null;
        try {
            Hashtable<String, String> env = new Hashtable<String, String>();
            env.put(Context.INITIAL_CONTEXT_FACTORY, INITCTX);
            env.put(Context.SECURITY_AUTHENTICATION, SEC_AUTH);
            env.put(Context.SECURITY_PRINCIPAL, PRINCIPAL);
            env.put(Context.SECURITY_CREDENTIALS, PW);
            env.put(Context.PROVIDER_URL, HOST);
            ctx = new InitialLdapContext(env, null);
            System.out.println("Connection Successful.");
        } catch (NamingException nex) {
            System.out.println("LDAP Connection: FAILED");
            nex.printStackTrace();
        }
        return ctx;
    }

    public User getUserBasicAttributes(String username, LdapContext ctx) {
        User user = null;
        try {

            SearchControls constraints = new SearchControls();
            constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
            String[] attrIDs = { "distinguishedName"; "sn"; "givenname"; "mail"; "telephonenumber" };
            constraints.setReturningAttributes(attrIDs);
            //First input parameter is search bas, it can be "CN=Users,DC=YourDomain,DC=com"
            //Second Attribute can be uid=username
            NamingEnumeration answer = ctx.search("DC=YourDomain,DC=com", "sAMAccountName="
                    + username, constraints);
            if (answer.hasMore()) {
                Attributes attrs = ((SearchResult) answer.next()).getAttributes();
                System.out.println("distinguishedName " + attrs.get("distinguishedName"));
                System.out.println("givenname " + attrs.get("givenname"));
                System.out.println("sn " + attrs.get("sn"));
                System.out.println("mail " + attrs.get("mail"));
                System.out.println("telephonenumber " + attrs.get("telephonenumber"));
            } else {
                throw new Exception("Invalid User");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return user;
    }

    public boolean isUserExist(String username, String password) {
        LdapContext ctx = getLdapContext()
        try {
            SearchControls constraints = new SearchControls()
            constraints.setSearchScope(SearchControls.SUBTREE_SCOPE)
            String[] attrIDs = { "userPrincipalName"; "userPassword" }
            constraints.setReturningAttributes(attrIDs)
            NamingEnumeration answer = ctx.search("DC=YourDomain,DC=com", "userPrincipalName="
                    + username, constraints)
            if (answer.hasMore()) {
                Attributes attrs = ((SearchResult) answer.next()).getAttributes()
                System.out.println("userPrincipalName " + attrs.get("userPrincipalName"))
                System.out.println("userPassword " + attrs.get("userPassword"))
                if (attrs.get("userPassword")) {
                    throw new Exception("Invalid password");
                    return false
                }
            } else {
                throw new Exception("Invalid User");
                return false
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return true
    }

}
