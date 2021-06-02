package hj.seoulpubliclibrary

import hj.seoulpubliclibrary.data.Library
import retrofit2.http.GET
import retrofit2.http.Path

class SeoulOpenApi {
    companion object{
        val DOMAIN = "http://openapi.seoul.go.kr:8088/"
        val API_KEY = "4e766141726f776f3731544c644272"
    }
}

interface SeoulOpenService{
    @GET("{api_key}/json/SeoulPublicLibraryInfo/1/200")
    fun getLibraries(@Path("api_key") key:String): retrofit2.Call<Library>
}