package proitappsolutions.com.rumosstore.api;

import proitappsolutions.com.rumosstore.modelo.UsuarioApi;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiInterface {

    @POST("/v1/app/")
    Call<Void> registrarCliente(@Body UsuarioApi usuarioApi);

    /*
    @Multipart
    @POST("/cja")
    Call<ResponseBody> registrarUsuario(
            @Part("nomeProprio") RequestBody nomeProprio,
            @Part("apelido") RequestBody apelido,
            @Part("dataNascimento") RequestBody dataNascimento,
            @Part("bi") RequestBody bi,
            @Part("genero") RequestBody genero,
            @Part("telemovel") RequestBody telemovel,
            @Part("cidade") RequestBody cidade,
            @Part("rua") RequestBody rua,
            @Part("email") RequestBody email,
            @Part MultipartBody.Part imagem
    ); **/




}
