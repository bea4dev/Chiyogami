pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://papermc.io/repo/repository/maven-public/")
    }
}

rootProject.name = "Chiyogami"

include("Chiyogami-api", "Chiyogami-server")
