
package com.github.shimal.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;



public class ADAuthenticator {



    //~ --- [INSTANCE FIELDS] ------------------------------------------------------------------------------------------

    LdapContext    ctxGC             = null;
    Hashtable      env               = new Hashtable();
    List<String>   groups            = new ArrayList<String>();
    String         groupsSearchBase  = "DC=kahin,DC=khn";
    private String distinguishedName;


    private String domain;
    private String ldapHost;
    private String searchBase;



    //~ --- [CONSTRUCTORS] ---------------------------------------------------------------------------------------------

    public ADAuthenticator() {

        this.domain     = "kahin.khn";
        this.ldapHost   = "ldap://192.168.1.205:389";
        this.searchBase = "dc=kahin,dc=khn";
    }



    public ADAuthenticator(String domain, String host, String dn) {

        this.domain     = domain;
        this.ldapHost   = host;
        this.searchBase = dn;
    }



    //~ --- [METHODS] --------------------------------------------------------------------------------------------------

    public static String binarySidToStringSid(byte[] SID) {

        String strSID = "";

        // convert the SID into string format

        long version;
        long authority;
        long count;
        long rid;

        strSID    = "S";
        version   = SID[0];
        strSID    = strSID + "-" + Long.toString(version);
        authority = SID[4];

        for (int i = 0; i < 4; i++) {
            authority <<= 8;
            authority += SID[4 + i] & 0xFF;
        }

        strSID =  strSID + "-" + Long.toString(authority);
        count  =  SID[2];
        count  <<= 8;
        count  += SID[1] & 0xFF;

        for (int j = 0; j < count; j++) {

            rid = SID[11 + (j * 4)] & 0xFF;

            for (int k = 1; k < 4; k++) {

                rid <<= 8;

                rid += SID[11 - k + (j * 4)] & 0xFF;

            }

            strSID = strSID + "-" + Long.toString(rid);

        }

        return strSID;

    }



    //~ --- [METHODS] --------------------------------------------------------------------------------------------------

    public boolean authenticate(String user, String pass) {

        boolean  result       = false;
        String[] returnedAtts = { "distinguishedName" };

        // String returnedAtts[] = {"sn", "givenName", "mail","memberof","sAMAccountName","distinguishedName"};
        String searchFilter = "(&(objectClass=user)(sAMAccountName=" + user + "))";

        // Create the search controls
        SearchControls searchCtls = new SearchControls();
        searchCtls.setReturningAttributes(returnedAtts);

        // Specify the search scope
        searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);

        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, ldapHost);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, user + "@" + domain);
        env.put(Context.SECURITY_CREDENTIALS, pass);
        env.put("java.naming.ldap.attributes.binary", "tokenGroups");


        try {
            ctxGC = new InitialLdapContext(env, null);

            // Search objects in GC using filters
            NamingEnumeration answer = ctxGC.search(searchBase, searchFilter, searchCtls);

            while (answer.hasMoreElements()) {
                SearchResult sr    = (SearchResult) answer.next();
                Attributes   attrs = sr.getAttributes();
                Map          amap  = null;

                if (attrs != null) {
                    amap = new HashMap();

                    NamingEnumeration ne = attrs.getAll();

                    while (ne.hasMore()) {
                        Attribute attr = (Attribute) ne.next();

                        // amap.put(attr.getID(), attr.get().toString());
                        distinguishedName = attr.get().toString();
                        result            = true;
                    }

                    ne.close();
                }

            }
        } catch (NamingException ex) {

        } finally {

        }

        return result;
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    public String getDistinguishedName() {

        return distinguishedName;
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    public String getDomain() {

        return domain;
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    public List<String> getGroups() {

        try {

            // Create the initial directory context
            // LdapContext ctx = new InitialLdapContext(env, null);

            // Create the search controls
            SearchControls userSearchCtls = new SearchControls();

            // Specify the search scope
            userSearchCtls.setSearchScope(SearchControls.OBJECT_SCOPE);

            // specify the LDAP search filter to find the user in question
            String userSearchFilter = "(objectClass=user)";

            // paceholder for an LDAP filter that will store SIDs of the groups the user belongs to
            StringBuilder groupsSearchFilter = new StringBuilder();
            groupsSearchFilter.append("(|");

            // Specify the Base for the search
            String userSearchBase = distinguishedName;

            // Specify the attributes to return
            String[] userReturnedAtts = { "tokenGroups" };
            userSearchCtls.setReturningAttributes(userReturnedAtts);

            // Search for objects using the filter
            NamingEnumeration userAnswer = ctxGC.search(userSearchBase, userSearchFilter, userSearchCtls);

            // Loop through the search results
            while (userAnswer.hasMoreElements()) {

                try {
                    SearchResult sr    = (SearchResult) userAnswer.next();
                    Attributes   attrs = sr.getAttributes();

                    if (attrs != null) {

                        try {

                            for (NamingEnumeration ae = attrs.getAll(); ae.hasMore();) {
                                Attribute attr = (Attribute) ae.next();

                                for (NamingEnumeration e = attr.getAll(); e.hasMore();) {

                                    byte[] sid = (byte[]) e.next();
                                    groupsSearchFilter.append("(objectSid=" + binarySidToStringSid(sid) + ")");

                                }

                                groupsSearchFilter.append(")");
                            }

                        } catch (NamingException e) {
                            System.err.println("Problem listing membership: " + e);
                        }
                    }
                } catch (NamingException ex) {
                    // Logger.getLogger(yeni.class.getName()).log(Level.SEVERE, null, ex);
                }
            }


            // Search for groups the user belongs to in order to get their names
            // Create the search controls
            SearchControls groupsSearchCtls = new SearchControls();

            // Specify the search scope
            groupsSearchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);

            // Specify the Base for the search


            // Specify the attributes to return
            String[] groupsReturnedAtts = { "sAMAccountName" };
            groupsSearchCtls.setReturningAttributes(groupsReturnedAtts);

            // Search for objects using the filter
            NamingEnumeration groupsAnswer = ctxGC.search(groupsSearchBase, groupsSearchFilter.toString(),
                    groupsSearchCtls);

            // Loop through the search results

            while (groupsAnswer.hasMoreElements()) {

                try {
                    SearchResult sr    = (SearchResult) groupsAnswer.next();
                    Attributes   attrs = sr.getAttributes();

                    if (attrs != null) {

                        // System.out.println(attrs.get("sAMAccountName").get());
                        groups.add(attrs.get("sAMAccountName").get().toString());
                    }
                } catch (NamingException ex) {
                    // Logger.getLogger(yeni.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            ctxGC.close();

        } catch (NamingException e) {
            System.err.println("Problem searching directory: " + e);
        }

        return groups;
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    public String getGroupsSearchBase() {

        return groupsSearchBase;
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    public String getLdapHost() {

        return ldapHost;
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    public String getSearchBase() {

        return searchBase;
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    public void setDistinguishedName(String distinguishedName) {

        this.distinguishedName = distinguishedName;
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    public void setDomain(String domain) {

        this.domain = domain;
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    public void setGroupsSearchBase(String groupsSearchBase) {

        this.groupsSearchBase = groupsSearchBase;
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    public void setLdapHost(String ldapHost) {

        this.ldapHost = ldapHost;
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    public void setSearchBase(String searchBase) {

        this.searchBase = searchBase;
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    public String userName(String user, String pass) {

        String   userName     = "";
        String[] returnedAtts = { "givenName", "sn" };

        // String returnedAtts[] = {"sn", "givenName", "mail","memberof","sAMAccountName","distinguishedName"};
        String searchFilter = "(&(objectClass=user)(sAMAccountName=" + user + "))";

        // Create the search controls
        SearchControls searchCtls = new SearchControls();
        searchCtls.setReturningAttributes(returnedAtts);

        // Specify the search scope
        searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);

        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, ldapHost);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, user + "@" + domain);
        env.put(Context.SECURITY_CREDENTIALS, pass);
        env.put("java.naming.ldap.attributes.binary", "tokenGroups");


        try {
            ctxGC = new InitialLdapContext(env, null);

            // Search objects in GC using filters
            NamingEnumeration answer = ctxGC.search(searchBase, searchFilter, searchCtls);

            while (answer.hasMoreElements()) {
                SearchResult sr    = (SearchResult) answer.next();
                Attributes   attrs = sr.getAttributes();
                Map          amap  = null;

                if (attrs != null) {
                    amap = new HashMap();

                    NamingEnumeration ne = attrs.getAll();

                    while (ne.hasMore()) {
                        Attribute attr = (Attribute) ne.next();

                        // amap.put(attr.getID(), attr.get().toString());
                        userName += attr.get().toString() + " ";

                    }

                    ne.close();
                }

            }
        } catch (NamingException ex) {

        } finally {

        }

        return userName;
    }
}
