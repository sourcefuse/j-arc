workspace(name = "j-arc")
load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")
RULES_JVM_EXTERNAL_TAG = "4.1"
RULES_JVM_EXTERNAL_SHA = "f36441aa876c4f6427bfb2d1f2d723b48e9d930b62662bf723ddfb8fc80f0140"
SPRING_RULE_SHA = "9385652bb92d365675d1ca7c963672a8091dc5940a9e307104d3c92e7a789c8e"
SPRING_VERSION = "3.0.5"
http_archive(
    name = "rules_jvm_external",
    strip_prefix = "rules_jvm_external-%s" % RULES_JVM_EXTERNAL_TAG,
    sha256 = RULES_JVM_EXTERNAL_SHA,
    url = "https://github.com/bazelbuild/rules_jvm_external/archive/%s.zip" % RULES_JVM_EXTERNAL_TAG,
)
load("@rules_jvm_external//:defs.bzl", "maven_install")
maven_install(
    artifacts = [
        "org.springframework.boot:spring-boot:%s" % SPRING_VERSION,
        "org.springframework.boot:spring-boot-starter:%s" % SPRING_VERSION,
        "org.springframework.boot:spring-boot-starter-actuator:%s" % SPRING_VERSION,
        "org.springframework.boot:spring-boot-starter-data-jpa:%s" % SPRING_VERSION,
        "org.springframework.boot:spring-boot-starter-data-redis:%s" % SPRING_VERSION,
        "org.springframework.boot:spring-boot-starter-data-rest:%s" % SPRING_VERSION,
        "org.springframework.boot:spring-boot-starter-hateoas:%s" % SPRING_VERSION,
        "org.springframework.boot:spring-boot-starter-oauth2-client:%s" % SPRING_VERSION,
        "org.springframework.boot:spring-boot-starter-security:%s" % SPRING_VERSION,
        "org.springframework.boot:spring-boot-starter-web:%s" % SPRING_VERSION,
        "io.micrometer:micrometer-tracing-bridge-brave:1.0.3",
        "org.flywaydb:flyway-core:9.5.1" ,
        "org.projectlombok:lombok:1.18.26",
        "io.micrometer:micrometer-registry-prometheus:1.10.5",
        "org.postgresql:postgresql:42.5.4",
        "org.springframework.boot:spring-boot-starter-test:%s" % SPRING_VERSION,
        "org.springframework.restdocs:spring-restdocs-mockmvc:3.0.0",
        "org.springframework.security:spring-security-test:6.0.2",
        "org.asciidoctor:asciidoctor-maven-plugin:2.2.1",
        "org.springframework.restdocs:spring-restdocs-asciidoctor:3.0.0",
    ],
    fetch_sources = True,
    repositories = [
        "https://repo1.maven.org/maven2",
    ]
)

load("@maven//:defs.bzl", "pinned_maven_install")
pinned_maven_install()