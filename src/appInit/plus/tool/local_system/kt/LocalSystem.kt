@Suppress(
    "FunctionName",
    "ConstPropertyName",
)
object LocalSystem {

    private const val _SEPARATOR = "/"

    const val ENV_USER_CONFIG_DIR = "XDG_CONFIG_HOME"
    private const val USER_CONFIG_DIR_DEFAULT = ".config"

    const val ENV_USER_RUNTIME_DIR = "XDG_RUNTIME_DIR"
    private const val USERS_RUNTIME_DIR_DEFAULT = "/run/user"

    const val PROPERTY_TEMP_DIR = "java.io.tmpdir"
    const val TEMP_DIR_DEFAULT = "/tmp"

    val tempDir: String by lazy {
        try {
            System.getProperty(PROPERTY_TEMP_DIR).takeIf { it.isNotBlank() } ?: TEMP_DIR_DEFAULT
        } catch (_: Exception) {
            TEMP_DIR_DEFAULT
        }
    }

    private fun _userId(): String = try {
        with(ExecProcess) { "id --user".exec().waitOutput().toString().trim() }
    } catch (_: Exception) {
        ""
    }

    val userRuntimeDir: String by lazy {
        val result = try {
            System.getenv(ENV_USER_RUNTIME_DIR).takeIf { it.isNotBlank() } ?: ""
        } catch (_: Exception) {
            ""
        }
        if (result.isEmpty()) {
            return@lazy try {
                USERS_RUNTIME_DIR_DEFAULT + _SEPARATOR + _userId()
            } catch (_: Exception) {
                TODO("unable to detect :'userRuntimeDir'")
            }
        }
        return@lazy result
    }

    val userId: String by lazy { userRuntimeDir.split(_SEPARATOR).last() }

    val userConfigDir: String by lazy {
        val result = try {
            System.getenv(ENV_USER_CONFIG_DIR).takeIf { it.isNotBlank() } ?: ""
        } catch (_: Exception) {
            ""
        }
        if (result.isEmpty()) {
            return@lazy try {
                UserHomePlace.file(USER_CONFIG_DIR_DEFAULT).path
            } catch (_: Exception) {
                TODO("unable to detect :'userConfigDir'")
            }
        }
        return@lazy result
    }

}