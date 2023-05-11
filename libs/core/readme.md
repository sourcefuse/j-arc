# Core Library

Core library contains following Models, entity listeners and repositories
### Models
* SoftDeleteEntity - has fields such as (deleted, deletedBy, deletedOn) related to soft delete functionality 
* BaseEntity - extends SoftDeleteEntity
* UserModifiableEntity - extends BaseEntity

## Installation

## Usage
```
// YourApplication.java
package your.package;

....

@SpringBootApplication
// to include core library and your package
@ComponentScan({ "your.package", "com.sourcefuse.jarc.core" })
// to change base repository class to  core library softDeletesRepository from core
@EnableJpaRepositories(repositoryBaseClass = SoftDeletesRepositoryImpl.class)
public class AuthenticationAuditApplication {
    ...
}
```

```
// YourRepository.java
package your.package.repositories;

...

public interface YourRepository extends SoftDeletesRepository<YourModel, Integer> {

}

```


```
// YourModel.java
package your.package.models;

...

// add entity listner if you want to log all activities such as 
// update/save/delete for YourModel log will be saved in audit_logs table 
// which resides in schema logs
@EntityListeners(AuditLogEntityListener.class)

public class YourModel extends UserModifiableEntity {
   ...
}
```