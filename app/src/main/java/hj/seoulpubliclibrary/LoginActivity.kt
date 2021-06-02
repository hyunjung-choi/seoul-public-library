package hj.seoulpubliclibrary

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.nhn.android.naverlogin.OAuthLogin
import com.nhn.android.naverlogin.OAuthLoginHandler
import com.nhn.android.naverlogin.ui.OAuthLoginActivity
import hj.seoulpubliclibrary.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding

    lateinit var mOAuthLoginInstance: OAuthLogin
    lateinit var mContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val naver_client_id = getString(R.string.naver_client_id)
        val naver_client_secret = getString(R.string.naver_client_secret)
        val naver_client_name = getString(R.string.naver_client_name)

        mContext = this

        mOAuthLoginInstance = OAuthLogin.getInstance()
        mOAuthLoginInstance.init(
            mContext, naver_client_id, naver_client_secret, naver_client_name
        )

        binding.btnNaverLogin.setOnClickListener {
            val mOAuthLoginHandler: OAuthLoginHandler = object : OAuthLoginHandler(){
                override fun run(success: Boolean) {
                    // 로그인 성공 시
                    if(success){
                        var accessToken: String = mOAuthLoginInstance.getAccessToken(mContext)
                        var refreshToken: String = mOAuthLoginInstance.getRefreshToken(mContext)
                        var expiresAt: Long = mOAuthLoginInstance.getExpiresAt(mContext)
                        var tokenType: String = mOAuthLoginInstance.getTokenType(mContext)

                        Log.d("LoginData", "accessToken: $accessToken")
                        Log.d("LoginData", "refrechToken: $refreshToken")
                        Log.d("LoginData", "expiresAt: $expiresAt")
                        Log.d("LoginData", "tokenType: $tokenType")

                        Toast.makeText(
                            mContext
                            , "네이버 로그인 성공"
                            , Toast.LENGTH_SHORT
                        ).show()
                        goToMapsActivity()
                    }
                    else{
                        val errorCode: String = mOAuthLoginInstance.getLastErrorCode(mContext).code
                        val errorDesc = mOAuthLoginInstance.getLastErrorDesc(mContext)

                        Toast.makeText(
                            baseContext,
                            "errorCode: $errorCode , errorDesc: $errorDesc",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            mOAuthLoginInstance.startOauthLoginActivity(this, mOAuthLoginHandler)


        }

//        binding.btnNaverLogout.setOnClickListener {
//            mOAuthLoginInstance.logout(mContext)
//
//            Toast.makeText(
//                mContext
//                , "로그아웃 되었습니다."
//                , Toast.LENGTH_SHORT
//            ).show()
//        }

        binding.btnLogin.setOnClickListener {
            goToMapsActivity()
        }

    }

    val mOAuthLoginHandler: OAuthLoginHandler = object : OAuthLoginHandler(){
        override fun run(success: Boolean) {
            if(success){
                // 로그인 성공 시 실행할 코드
                Toast.makeText(mContext, "로그인 성공", Toast.LENGTH_SHORT).show()
            }
            else{
                val errorCode: String = mOAuthLoginInstance.getLastErrorCode(mContext).code
                val errorDesc = mOAuthLoginInstance.getLastErrorDesc(mContext)

                Toast.makeText(
                    baseContext, "errorCode: $errorCode , errorDesc: $errorDesc", Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun goToMapsActivity(){
        // MapsActivity로 이동
        var intent = Intent(mContext, MapsActivity::class.java)
        startActivity(intent)
        finish()
    }
}