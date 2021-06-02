package hj.seoulpubliclibrary

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import hj.seoulpubliclibrary.data.Library

class BottomSheet: BottomSheetDialogFragment() {

    lateinit var libraryTag: Array<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        // Bundle 값 불러오기
        if(getArguments() != null){
            var libTag = getArguments()?.getStringArray("libTag")
            libraryTag = libTag!!
        }
        return inflater.inflate(R.layout.dialog_bottom_sheet, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val libName = libraryTag.get(0)
        val libUrl = libraryTag.get(1)
        val libAddr = libraryTag.get(2)
        val libTime = libraryTag.get(3)

        view?.findViewById<TextView>(R.id.tv_dialog_name)?.text = libName
        view?.findViewById<TextView>(R.id.tv_dialog_time)?.text = libTime
        view?.findViewById<TextView>(R.id.tv_dialog_addr)?.text = libAddr

        // 홈페이지로 이동 버튼
        view?.findViewById<Button>(R.id.button_bottom_sheet)?.setOnClickListener {
            if(libUrl != null){
                var url = libUrl!!

                if(!url.startsWith("http")){
                    url = "http://${url}"
                }
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }
            true
//                if(it.tag != null){
//                    var url = it.tag as String
//                    if(!url.startsWith("http")){
//                        url = "http://${url}"
//                    }
//                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
//                    startActivity(intent)
//                }
//                true
        }
    }
}