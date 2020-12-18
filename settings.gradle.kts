include("core", "lwjgl3")

pluginManagement {
    resolutionStrategy {
        eachPlugin {
            if (requested.id.namespace == "org.jetbrains.kotlin") {
                useVersion("1.4.21")
            }
        }
    }
}
