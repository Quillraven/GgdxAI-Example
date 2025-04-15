include("core", "lwjgl3")

pluginManagement {
    resolutionStrategy {
        eachPlugin {
            if (requested.id.namespace == "org.jetbrains.kotlin") {
                useVersion("2.1.10")
            }
        }
    }
}
