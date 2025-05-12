import java.util.Properties

include(":core_ui")


include(":spotify_fake")


pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "AndroidApp"
include(":app")
include(":spotifyclient")
include(":spotify_fake")
include(":core_ui")


// Load credentials from spotify-secret.properties
run {
    val properties = Properties()
    val propertiesFile = file("local.properties")

    if (propertiesFile.exists()) {
        logger.lifecycle("@@ LOADING PROPERTIES FROM local.properties")
        properties.load(propertiesFile.inputStream())

        val clientId = properties["CLIENT_ID"] ?: error("CLIENT_ID missing")
        val clientSecret = properties["CLIENT_SECRET"] ?: error("CLIENT_SECRET missing")

        gradle.extra.also {
            it.set("spotifyClientId", clientId.toString())
            it.set("spotifyClientSecret", clientSecret.toString())
        }
    } else {
        logger.error("No local.properties file found in project! This file must contain CLIENT_ID and CLIENT_SECRET.")
    }
}

