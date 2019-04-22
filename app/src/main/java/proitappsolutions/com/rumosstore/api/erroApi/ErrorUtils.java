package proitappsolutions.com.rumosstore.api.erroApi;

import java.io.IOException;
import java.lang.annotation.Annotation;
import okhttp3.ResponseBody;
import proitappsolutions.com.rumosstore.api.ApiClient;
import retrofit2.Converter;
import retrofit2.Response;

public class ErrorUtils {

    public static ErrorResponce parseError (Response<?> response){
        Converter<ResponseBody, ErrorResponce> conversorDeErro = ApiClient.apiClient()
                .responseBodyConverter(ErrorResponce.class , new Annotation[0]);
        ErrorResponce errorResponce = null;
        try{
            if (response.errorBody() != null) {
                errorResponce = conversorDeErro.convert(response.errorBody());
            }
        }catch (IOException e){
            return new ErrorResponce();
        }finally {
            return errorResponce;
        }
    }
}