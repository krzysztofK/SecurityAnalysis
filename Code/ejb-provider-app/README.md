Należy zmienić konfiguracje w pliku standalone.xml:

1. W sekcji <security-realms> ... </security-realms> dodać:

<security-realm name="MyRealm">
  <authentication>
    <jaas name="ejb-ldap"/>
  </authentication>
</security-realm>

2. Dodać domene bezpieczeństwa:

<security-domain name="ejb-ldap" cache-type="default">
  <authentication>
    <login-module code="Remoting" flag="optional">
      <module-option name="password-stacking" value="useFirstPass"/>
    </login-module>
    <login-module code="org.jboss.security.auth.spi.LdapLoginModule" flag="required">
      <module-option name="java.naming.factory.initial" value="com.sun.jndi.ldap.LdapCtxFactory"/>
      <module-option name="java.naming.provider.url" value="ldap://192.168.181.156:10389"/>
      <module-option name="java.naming.security.authentication" value="simple"/>
      <module-option name="principalDNPrefix" value="uid="/>
      <module-option name="principalDNSuffix" value=",ou=people,dc=example,dc=com"/>
      <module-option name="rolesCtxDN" value="ou=roles,dc=example,dc=com"/>
      <module-option name="uidAttributeID" value="cn"/>
      <module-option name="matchOnUserDN" value="false"/>
      <module-option name="roleAttributeID" value="member"/>
      <module-option name="roleAttributeIsDN" value="true"/>
      <module-option name="roleNameAttributeID" value="cn"/>
      <module-option name="password-stacking" value="useFirstPass"/>
    </login-module>
  </authentication>
</security-domain>

3. W sekcji <profile> ... </profile>

Zamienić: 

<subsystem xmlns="urn:jboss:domain:remoting:1.1">
  <connector name="remoting-connector" socket-binding="remoting" security-realm="ApplicationRealm"/>
</subsystem>

na :

<subsystem xmlns="urn:jboss:domain:remoting:1.1">
  <connector name="remoting-connector" socket-binding="remoting" security-realm="MyRealm"/>
</subsystem>
