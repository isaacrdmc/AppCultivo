import com.google.gson.annotations.SerializedName

// DTO para la petición de login
data class LoginRequestDto(
    @SerializedName("correo")
    val correo: String,
    @SerializedName("contrasena")
    val contrasena: String
)

// DTO para la respuesta de login exitosa
data class LoginResponseDto(
    @SerializedName("jwtToken")
    val jwtToken: String,
    @SerializedName("usuarioID")
    val usuarioID: Int,
    @SerializedName("correo")
    val correo: String,
    @SerializedName("nombre")
    val nombre: String
)

// DTO para la petición de REGISTRO
data class RegisterRequestDto(
    @SerializedName("nombre")
    val nombre: String,
    @SerializedName("correo")
    val correo: String,
    @SerializedName("contrasena")
    val contrasena: String
)

// DTO para manejar mensajes de error
data class ErrorResponse(
    val message: String
)