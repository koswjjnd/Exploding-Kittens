import com.github.spotbugs.snom.Confidence
import com.github.spotbugs.snom.Effort

plugins {
    application
    id("java")
    checkstyle
    id("com.github.spotbugs") version "6.0.25"
    jacoco
    id("info.solidsoft.pitest") version "1.15.0"
}

group = "nu.csse.sqe"
version = "1.0"

repositories {
    mavenCentral()
}

application {
    mainClass = "explodingkittens.Main"
}

dependencies {
    checkstyle("com.puppycrawl.tools:checkstyle:10.13.0")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.junit.platform:junit-platform-suite")

    // https://mvnrepository.com/artifact/org.easymock/easymock
    testImplementation("org.easymock:easymock:3.1")

    // cucumber
    testImplementation(platform("io.cucumber:cucumber-bom:7.20.1"))
    testImplementation("io.cucumber:cucumber-java")
    testImplementation("io.cucumber:cucumber-junit-platform-engine")
    testImplementation("io.cucumber:cucumber-picocontainer:7.20.1")

    // Mockito
    testImplementation("org.mockito:mockito-core:5.10.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.10.0")

    // Spotbugs annotations
    implementation("com.github.spotbugs:spotbugs-annotations:4.8.6")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(11)
    }
}

tasks.compileJava {
    options.release = 11
}

tasks.withType<Checkstyle>().configureEach {
    reports {
        xml.required = false
        html.required = true
        html.stylesheet = resources.text.fromFile("config/xsl/checkstyle-noframes-severity-sorted.xsl")
    }
}

checkstyle {
    isIgnoreFailures = false
    configFile = file("config/checkstyle/checkstyle.xml")
}
// Spotbugs README: https://github.com/spotbugs/spotbugs-gradle-plugin#readme
// SpotBugs Gradle Plugin: https://spotbugs.readthedocs.io/en/latest/gradle.html
spotbugs {
    ignoreFailures = false
    showStackTraces = true
    showProgress = true
    effort = Effort.DEFAULT
    reportLevel = Confidence.DEFAULT
    //omitVisitors = listOf("FindNonShortCircuit")
    reportsDir = file("spotbugs")
    //includeFilter = file("include.xml")
    excludeFilter = file("excludeFilter.xml")
    //onlyAnalyze = listOf("com.foobar.MyClass", "com.foobar.mypkg.*")
    maxHeapSize = "1g"
    extraArgs = listOf("-nested:false")
    //jvmArgs = listOf("-Duser.language=ja") // set user language to japanese
}

tasks.spotbugsMain {
    reports.create("html") {
        required = true
        outputLocation = layout.buildDirectory.file("reports/spotbugs/spotbugs.html")
        setStylesheet("fancy-hist.xsl")
    }
}

tasks.jacocoTestReport {
    reports {
        xml.required = false
        csv.required = false
        html.outputLocation = layout.buildDirectory.dir("reports/jacoco")
    }
}

tasks.build {
    dependsOn("pitest")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        showStandardStreams = true
        events("passed", "skipped", "failed")
    }
    finalizedBy(tasks.checkstyleMain)
    finalizedBy(tasks.jacocoTestReport)
    finalizedBy(tasks.pitest)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test) // tests are required to run before generating the report
}

pitest {
    targetClasses = setOf("explodingkittens.model.*", "explodingkittens.service.*")
    targetTests   = setOf("explodingkittens.model.*Test", "explodingkittens.service.*Test")
    junit5PluginVersion = "1.2.1"
    pitestVersion = "1.15.0"

    threads = 4
    outputFormats = setOf("HTML")
    timestampedReports = false
    testSourceSets.set(listOf(sourceSets.test.get()))
    mainSourceSets.set(listOf(sourceSets.main.get()))
    jvmArgs.set(listOf("-Xmx1024m"))
    useClasspathFile.set(true)
    fileExtensionsToFilter.addAll("xml")
    exportLineCoverage = true
    mutators.set(listOf(
        "CONDITIONALS_BOUNDARY",
        "INCREMENTS",
        "INVERT_NEGS",
        "MATH",
        "NEGATE_CONDITIONALS",
        "PRIMITIVE_RETURNS",
        "VOID_METHOD_CALLS"
    ))
    avoidCallsTo.set(listOf("java.util.logging", "org.apache.log4j", "org.slf4j", "org.apache.commons.logging"))
}

configurations {
    all {
        resolutionStrategy {
            force("com.google.guava:guava:33.0.0-jre")
            exclude(group = "com.google.collections", module = "google-collections")
        }
    }
}

val cucumberRuntime by configurations.creating {
    extendsFrom(configurations["testImplementation"])
}

task("cucumber") {
    dependsOn("assemble", "compileTestJava")
    doLast {
        javaexec {
            mainClass.set("io.cucumber.core.cli.Main")
            classpath = cucumberRuntime + sourceSets.main.get().output + sourceSets.test.get().output
            args = listOf("--plugin", "pretty",
                "--glue", "ui",
                "src/test/resources")                   // where the feature files are.
            // Configure jacoco agent for the test coverage.
            val jacocoAgent = zipTree(configurations.jacocoAgent.get().singleFile)
                .filter { it.name == "jacocoagent.jar" }
                .singleFile
            jvmArgs = listOf("-javaagent:$jacocoAgent=destfile=${layout.buildDirectory.file("results/jacoco/cucumber.exec").get()},append=false")
        }
    }
}