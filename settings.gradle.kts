enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "Practicum-Android-Diploma"

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        mavenLocal()
        maven {
            setUrl("https://mvnrepository.com/artifact/org.sufficientlysecure/html-textview")
        }
    }
}

includeBuild("build-logic")

include(":app")
