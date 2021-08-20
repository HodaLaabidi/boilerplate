package fr.medespoir.data.const



enum class GenderPref {
    MAN , WOMAN
}
enum class DevisStatus(val status: String) {
   WAIT("En attente") , PROGRESS("En cours") , DONE("Accepté")
}

enum class ClaimStatus(val status: String) {
    WAIT("En attente") , PROGRESS("En cours") , DONE("Accepter")
}

fun String.toAuthType(): AuthType {
    return when (this) {
        AuthType.FACEBOOK.authValue -> AuthType.FACEBOOK
        AuthType.GOOGLE.authValue -> AuthType.GOOGLE
        else -> AuthType.EMAIL
    }
}

enum class AuthType(var authValue: String) {
    FACEBOOK("facebook.com"),
    GOOGLE("google.com"),
    EMAIL("")
}