import kotlinx.kover.gradle.plugin.dsl.AggregationType

group = "ru.otus"
version = "1.0-SNAPSHOT"

plugins {
    kotlin("jvm") version "2.1.20"
    kotlin("plugin.spring") version "2.1.20"
    id("org.springframework.boot") version "3.4.4"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.jetbrains.kotlinx.kover") version "0.7.5"
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.security:spring-security-crypto")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    implementation("org.postgresql:postgresql:42.7.3")
    implementation("org.liquibase:liquibase-core:4.31.1")

    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.assertj:assertj-core:3.27.3")
    testImplementation("io.mockk:mockk:1.13.16")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.10.0")
    testImplementation("org.mockito:mockito-core")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.2.1")
    testImplementation("com.h2database:h2")
}

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

kotlin {
    jvmToolchain(17)
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

koverReport {
    verify {
        rule("Min 90% lines coverage") {
            isEnabled = true
            bound {
                minValue = 90
                metric = kotlinx.kover.gradle.plugin.dsl.MetricType.LINE
                aggregation = AggregationType.COVERED_PERCENTAGE
            }
        }
        rule("Min 90% instructions coverage") {
            isEnabled = true
            bound {
                minValue = 90
                metric = kotlinx.kover.gradle.plugin.dsl.MetricType.INSTRUCTION
                aggregation = AggregationType.COVERED_PERCENTAGE
            }
        }
        rule("Min 90% branches coverage") {
            isEnabled = true
            bound {
                minValue = 90
                metric = kotlinx.kover.gradle.plugin.dsl.MetricType.BRANCH
                aggregation = AggregationType.COVERED_PERCENTAGE
            }
        }
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}
