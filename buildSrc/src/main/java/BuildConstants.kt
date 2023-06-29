import org.gradle.api.JavaVersion

interface Project {
    val Namespace: String
}

object BuildConstants {
    const val PackageName = "com.helio.chatbot"
    const val TargetSdk = 33
    const val MinSdk = 28
    const val VersionCode = 3
    const val VersionName = "1.0"

    val JAVA_VERSION = JavaVersion.VERSION_17

    object Data: Project {
        override val Namespace: String = PackageName + ".data"
    }
    object Presentation: Project {
        override val Namespace: String = PackageName + ".presentation"
    }
    object Domain: Project {
        override val Namespace: String = PackageName + ".domain"
    }
    object Common: Project {
        override val Namespace: String = PackageName + ".common"
    }

}