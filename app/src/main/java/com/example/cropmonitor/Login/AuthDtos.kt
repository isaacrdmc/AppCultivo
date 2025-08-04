import com.google.gson.annotations.SerializedName

// DTO para la petición de login
data class LoginRequestDto(
    @SerializedName("Correo")
    val correo: String,
    @SerializedName("Contrasena")
    val contrasena: String
)


// DTO para la respuesta de login exitosa
data class LoginResponseDto(
    @SerializedName("JwtToken") // <-- CAMBIO CLAVE AQUÍ: Capitalización de "JwtToken"
    val jwtToken: String,
    @SerializedName("UsuarioID") // <-- CAMBIO CLAVE AQUÍ: Capitalización de "UsuarioID"
    val usuarioID: Int,
    @SerializedName("Correo") // <-- CAMBIO CLAVE AQUÍ: Capitalización de "Correo"
    val correo: String,
    @SerializedName("Nombre") // <-- CAMBIO CLAVE AQUÍ: Capitalización de "Nombre"
    val nombre: String
)

// DTO para la petición de REGISTRO
data class RegisterRequestDto(
    @SerializedName("Nombre")
    val nombre: String,
    @SerializedName("Correo")
    val correo: String,
    @SerializedName("Contrasena")
    val contrasena: String
)

// DTO para manejar mensajes de error
data class ErrorResponse(
    val message: String
)