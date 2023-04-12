load("@rules_jvm_external//:defs.bzl", "artifact")
package(default_visibility = ["//visibility:public"])

java_binary(
     name = "feature-toggle-service",
    srcs = glob(["src/main/java/**/*.java"]),
     main_class = "com.sourcefuse.jarc.services.featuretoggleservice.FeatureToggleServiceApplication",
     resources = [
         "src/main/resources/application.properties"
     ],
     deps = [
           artifact("org.springframework.boot:spring-boot"),
           artifact("org.springframework.boot:spring-boot-starter-actuator"),
           artifact("org.springframework.boot:spring-boot-starter-data-jpa" ),
           artifact("org.springframework.boot:spring-boot-starter-data-redis" ),
           artifact("org.springframework.boot:spring-boot-starter-data-rest" ),
           artifact("org.springframework.boot:spring-boot-starter-hateoas"),
           artifact("org.springframework.boot:spring-boot-starter-oauth2-client"),
           artifact("org.springframework.boot:spring-boot-starter-security"),
           artifact("org.springframework.boot:spring-boot-starter-web"),
           artifact("io.micrometer:micrometer-tracing-bridge-brave" ),
           artifact("org.flywaydb:flyway-core" ),
           artifact("org.postgresql:postgresql"),
           "//libs/core:core-lib"
    ],
    runtime_deps = [
        artifact("org.projectlombok:lombok"),
        artifact("io_micrometer:micrometer-registry-prometheus"),
    ]
 )
java_test(
     name = "test-feature-toggle-service",
    srcs = glob(["src/test/java/**/*.java"]),
     test_class = "com.sourcefuse.jarc.services.featuretoggleservice.FeatureToggleServiceApplicationTests",
     resources = [
         "src/main/resources/application.properties"
     ],
     deps = [
           artifact("org.springframework.boot:spring-boot"),
           artifact("org.springframework.boot:spring-boot-starter-actuator"),
           artifact("org.springframework.boot:spring-boot-starter-data-jpa" ),
           artifact("org.springframework.boot:spring-boot-starter-data-redis" ),
           artifact("org.springframework.boot:spring-boot-starter-data-rest" ),
           artifact("org.springframework.boot:spring-boot-starter-hateoas"),
           artifact("org.springframework.boot:spring-boot-starter-oauth2-client"),
           artifact("org.springframework.boot:spring-boot-starter-security"),
           artifact("org.springframework.boot:spring-boot-starter-web"),
           artifact("io.micrometer:micrometer-tracing-bridge-brave" ),
           artifact("org.flywaydb:flyway-core" ),
           artifact("org.postgresql:postgresql"),
           "//libs/core:core-lib",
           artifact("org.springframework.boot:spring-boot-starter-test"),
           artifact("org.springframework.restdocs:spring-restdocs-mockmvc"),
           artifact("org.springframework.security:spring-security-test"),
    ],
    runtime_deps = [
        artifact("org.projectlombok:lombok"),
        artifact("io_micrometer:micrometer-registry-prometheus"),
    ]
 )