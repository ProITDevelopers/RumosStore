package proitappsolutions.com.rumosstore.api;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import proitappsolutions.com.rumosstore.modelo.Autenticacao;
import proitappsolutions.com.rumosstore.modelo.Data;
import proitappsolutions.com.rumosstore.modelo.DataDados;
import proitappsolutions.com.rumosstore.modelo.DataUserApi;
import proitappsolutions.com.rumosstore.modelo.EmSessao;
import proitappsolutions.com.rumosstore.modelo.UsuarioApi;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiInterface {

    @POST("/v1/app/")
    Call<Void> registrarCliente(@Body UsuarioApi usuarioApi);

    @Multipart
    @PUT("/v1/app/image/{id}")
    Call<ResponseBody> enviarFoto(
            @Part("id") RequestBody id,
            @Part MultipartBody.Part imagem
    );

    @FormUrlEncoded
    @POST("/v1/app/login")
    Call<Data> autenticarCliente(
            @Field("email") String email,
            @Field("password") String password
    );

    @GET("https://console.proitappsolutions.com/v1/app/user/{id}")
    Call<DataUserApi> getUsuarioDados(@Path("id") String id);

    @FormUrlEncoded
    @PUT("/v1/app/{id}") //verificar rota
    Call<Void> atualizarDados(
            @Path("id") String id,
            @Field("provincia") String cidade,
            @Field("municipio") String municipio,
            @Field("rua") String rua,
            @Field("sexo") String sexo,
            @Field("dataNascimento") String dataNascimento,
            @Field("telefone") String telefone
    );

}
