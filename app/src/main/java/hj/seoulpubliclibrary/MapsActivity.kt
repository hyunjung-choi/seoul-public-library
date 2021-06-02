package hj.seoulpubliclibrary

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import hj.seoulpubliclibrary.data.Library
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        loadLibrarlies()

        // Bottom Sheet으로 데이터 전달 -> Bundle
        mMap.setOnInfoWindowClickListener {
            val bottomSheet = BottomSheet()
            var bundle = Bundle()
            bundle.putStringArray("libTag", it.tag as Array<String>)
            bottomSheet.setArguments(bundle)
            bottomSheet.show(supportFragmentManager, bottomSheet.tag)
        }

    }

    fun loadLibrarlies(){
        var retrofit = Retrofit.Builder()
            .baseUrl(SeoulOpenApi.DOMAIN)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val seoulOpenService = retrofit.create(SeoulOpenService::class.java)

        seoulOpenService
            .getLibraries(SeoulOpenApi.API_KEY)
            .enqueue(object : Callback<Library>{
                override fun onResponse(call: Call<Library>, response: Response<Library>) {
                    showLibraries(response.body())
                    response.body()
                }

                override fun onFailure(call: Call<Library>, t: Throwable) {
                    Toast.makeText(baseContext,"데이터를 가져올 수 없습니다.",Toast.LENGTH_SHORT).show()
                }
            })
    }

    fun showLibraries(result: Library?){
        result?.let{
            // 마커가 표시된 모든 영역을 보고 싶을 때 사용하는 변수
            val latLngBounds = LatLngBounds.Builder()
            for(library in it.SeoulPublicLibraryInfo.row){

                val position = LatLng(library.XCNTS.toDouble(), library.YDNTS.toDouble())

                val marker = MarkerOptions()
                        .position(position)
                        .title(library.LBRRY_NAME)

                if(library.HMPG_URL != null){
                    var libraryTag = arrayOf(library.LBRRY_NAME, library.HMPG_URL, library.ADRES, library.OP_TIME)

                    var obj = mMap.addMarker(marker)
                    obj.tag = libraryTag
                }
                else{
                    var libraryTag = arrayOf(library.LBRRY_NAME, "", library.ADRES, library.OP_TIME)

                    var obj = mMap.addMarker(marker)
                    obj.tag = libraryTag
                }

                latLngBounds.include(position)
            }
            val bounds = latLngBounds.build()
            val padding = 0
            val camera = CameraUpdateFactory.newLatLngBounds(bounds, padding)
            mMap.moveCamera(camera)
        }
    }
}