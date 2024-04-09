plugins {
    id("java")
}

group = "io.kalishak"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()

    flatDir {
        dirs("libs")
    }
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation("org.projectlombok:lombok:1.18.30")
    implementation("org.jetbrains:annotations:24.0.0")

    implementation("org.apache.logging.log4j:log4j-api:2.23.1")
    implementation("org.apache.logging.log4j:log4j-core:2.23.1")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.23.1")

    implementation("com.google.code.gson:gson:2.10.1")

    compileOnly("com.fazecast:jSerialComm:[2.0.0,3.0.0)")

    implementation(":jna-5.6.0")
    implementation(":AuraJava-1.0")
    implementation(":CueJava-1.21")
}

tasks.test {
    useJUnitPlatform()
}