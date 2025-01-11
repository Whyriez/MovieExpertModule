// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.android.dynamic.feature) apply false
    id("org.jlleitschuh.gradle.ktlint") version "11.0.0" apply false
    id("org.owasp.dependencycheck") version "8.0.0" apply false
}

subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        debug.set(true)
    }

    tasks.register("checkCodeStyle") {
        group = "Verification"
        description = "Check code style using ktlint"
        dependsOn("ktlintCheck")
    }

    tasks.register("formatCode") {
        group = "Formatting"
        description = "Fix code style using ktlint"
        dependsOn("ktlintFormat")
    }
}

subprojects {
    apply(plugin = "org.owasp.dependencycheck")

    configure<org.owasp.dependencycheck.gradle.extension.DependencyCheckExtension> {
        failBuildOnCVSS = 7F
        suppressionFile = "config/dependency-check/suppressions.xml"
    }

    tasks.register("checkVulnerabilities") {
        group = "Verification"
        description = "Check for vulnerabilities in dependencies"
        dependsOn("dependencyCheckAnalyze")
    }
}
