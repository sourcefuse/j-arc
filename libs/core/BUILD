load("@rules_jvm_external//:defs.bzl", "artifact")
package(default_visibility = ["//visibility:public"])

java_binary(
     name = "core-lib",
    srcs = glob(["src/main/java/**/*.java"]),
     main_class = "com.sourcefuse.jarc.core.CoreApplication",
     resources = [
         "src/main/resources/application.properties"
     ],
     deps = [
        artifact("org.springframework.boot:spring-boot"),
        artifact("org.springframework.boot:spring-boot-starter-actuator"),
        artifact("org.springframework.boot:spring-boot-starter-data-jpa" ),
        artifact("org.springframework.boot:spring-boot-starter-data-redis" ),
        artifact("org.springframework.boot:spring-boot-starter-security"),
        artifact("io.micrometer:micrometer-tracing-bridge-brave" ),
    ],
    runtime_deps = [
        artifact("org.projectlombok:lombok"),
        artifact("io_micrometer:micrometer-registry-prometheus"),
    ]
 )
java_test(
     name = "test-core-lib",
    srcs = glob(["src/test/java/**/*.java"]),
     test_class = "com.sourcefuse.jarc.core.CoreApplicationTests",
     resources = [
         "src/main/resources/application.properties"
     ],
     deps = [
        artifact("org.springframework.boot:spring-boot"),
        artifact("org.springframework.boot:spring-boot-starter-actuator"),
        artifact("org.springframework.boot:spring-boot-starter-data-jpa" ),
        artifact("org.springframework.boot:spring-boot-starter-data-redis" ),
        artifact("org.springframework.boot:spring-boot-starter-security"),
        artifact("io.micrometer:micrometer-tracing-bridge-brave" ),
        artifact("org.springframework.boot:spring-boot-starter-test"),
        artifact("org.springframework.security:spring-security-test"),
    ],
    runtime_deps = [
        artifact("org.projectlombok:lombok"),
        artifact("io_micrometer:micrometer-registry-prometheus"),
    ]
 )