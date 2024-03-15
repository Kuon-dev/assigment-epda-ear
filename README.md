# Assignment EPDA EAR

This is a Jakarta EE project built with Gradle.

## Prerequisite

- JDK 17
- Glassfish 7
- PostgreSQL
- PostgreSQL JDBC Driver

## Build Commands

1. Build 
```bash
./gradlew clean build
```

2. Start PostgreSQL 
```bash
pg_ctl start
```

3. Start Glassfish
```bash
asadmin start-domain
```

4. Deploy
```bash
asadmin deploy .\ear\build\libs\ear.ear
```

5. Redeploy
```bash
asadmin redeploy .\ear\build\libs\ear.ear
```

### NOTE:
Before step 4, make sure you hava a JDBC setup with PostgreSQL and it jas a JNDI name of `epdaDS` (case sensitive)
To do this, you need to visit http://localhost:4848, JDBC Connection Pool, and create a new connection
Make sure your PostgreSQL JDBC driver is in your classpath by adding it on your Glassfish folder at `/lib`
