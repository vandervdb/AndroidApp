// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.serialization) apply false
}

tasks.register("checkCatalogConsistency") {
    group = "verification"
    description = "Checking whether all versions in libs.versions.toml are used."

    doLast {
        println("✅ Checking Version Catalog Consistency...")

        val tomlFile = file("./gradle/libs.versions.toml")
        if (!tomlFile.exists()) {
            throw GradleException("📛 Cannot find libs.versions.toml !")
        }

        val content = tomlFile.readText()

        val declaredVersions =
            Regex("""^\s*([a-zA-Z0-9_-]+)\s*=\s*["'][^"']+["']""", RegexOption.MULTILINE)
                .findAll(content.substringAfter("[versions]").substringBefore("["))
                .map { it.groupValues[1] }
                .toSet()

        val usedVersionKeys = Regex("""version(?:\.ref)?\s*=\s*["']([a-zA-Z0-9_-]+)["']""")
            .findAll(content)
            .map { it.groupValues[1] }
            .toSet()

        val unused = declaredVersions - usedVersionKeys

        if (unused.isNotEmpty()) {
            println("⚠️  Some versions are not applied in catalog:")
            unused.forEach { println("- $it") }
            throw GradleException("❌ Invalid version catalog :Detected ${unused.size} version(s) unused .")
        } else {
            println("✅ Version Catalog is clean ! 🎉")
        }
    }
}

tasks.register("checkVersionHardcodedUsages") {
    group = "verification"
    description =
        "Checkin whether if every dependecy in dependencies { } block use GradleVersion Catalog (libs.*)."

    doLast {
        println("🔍 Looking for hard coded dependencies in  dependencies { } block...")

        val gradleFiles = project.rootDir
            .walkTopDown()
            .filter { it.name == "build.gradle.kts" || it.name == "build.gradle" }
            .toList()

        val dependencyBlockRegex = Regex("""(?s)dependencies\s*\{.*?}""")
        val hardcodedDependencyRegex = Regex("""["'].*:.*:.*["']""")

        val badUsages = gradleFiles.flatMap { file ->
            val content = file.readText()
            val dependenciesBlocks = dependencyBlockRegex.findAll(content)

            dependenciesBlocks.flatMap { matchResult ->
                val block = matchResult.value.lines()
                block.withIndex()
                    .filter { (_, line) -> hardcodedDependencyRegex.containsMatchIn(line) && "libs." !in line }
                    .map { (index, line) ->
                        "${file.path} [dependencies block] line ${index + 1} → $line"
                    }
            }
        }

        if (badUsages.isNotEmpty()) {
            println("⚠️  Hard coded dependencies detected :")
            badUsages.forEach { println(it) }
            throw GradleException("❌ One or more depencency des not use Grale Version Catalog.")
        } else {
            println("✅ Every dependncy in  dependencies { } use Gradle ersion Catalog.")
        }
    }
}



