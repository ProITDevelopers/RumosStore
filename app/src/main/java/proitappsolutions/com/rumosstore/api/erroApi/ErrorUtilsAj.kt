package proitappsolutions.com.rumosstore.api.erroApi

import java.io.IOException

import okhttp3.ResponseBody
import proitappsolutions.com.rumosstore.api.ApiClient
import retrofit2.Converter
import retrofit2.Response

object ErrorUtilsAj {

    fun parseError(response: Response<*>): ErrorResponce? {
        val conversorDeErro = ApiClient.apiClient()
                .responseBodyConverter<ErrorResponce>(ErrorResponce::class.java, arrayOfNulls(0))
        var errorResponce: ErrorResponce? = null
        try {
            if (response.errorBody() != null) {
                errorResponce = conversorDeErro.convert(response.errorBody()!!)
            }
        } catch (e: IOException) {
            return ErrorResponce()
        } finally {
            return errorResponce
        }
    }
}