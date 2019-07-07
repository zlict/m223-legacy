# DB

Der Entity Manager hat im WildFly einen Container Scope, wird also vom EJB Kontainer per Dependeny Injection instanziert.  

Die Transaktionen laufen als CMT (Container Managed Transactions).

- persistence-unit: `pu_api`
- jndi-name: `java:/jdbc/api`
- credetials: run `log` pipeline 






