package com.want.replugin

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.tencent.bugly.beta.Beta

class MainActivity : AppCompatActivity() {

    private val button:Button by lazy { findViewById(R.id.button) }
    private val button_update:Button by lazy { findViewById(R.id.button_update) }
    private val button_install1:Button by lazy { findViewById(R.id.button_install1) }
    private val button_install2:Button by lazy { findViewById(R.id.button_install2) }
    private val text: TextView by lazy { findViewById(R.id.text) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            val bugClass = BugClass()
            text.setTextColor(resources.getColor(R.color.design_default_color_primary))
//            text.setTextColor(resources.getColor(R.color.black))
            text.text = bugClass.bug(this.applicationContext)
        }
        button_update.setOnClickListener {
            Log.e("CrashReport", "onCreate: 检查更新")
            Beta.checkHotFix()
        }

        button_install1.setOnClickListener {
            val absolutePath = "/data/user/0/com.want.replugin/app_tmpPatch/tmpPatch.apk"
//            val absolutePath = "/sdcard/Android/data/com.want.replugin/app_tmpPatch/tmpPatch.apk"
            Log.e("CrashReport", "onCreate: $absolutePath")
            Beta.applyTinkerPatch(applicationContext, absolutePath)
        }
        button_install2.setOnClickListener {
//            val absolutePath = filesDir.absolutePath + "app_tmpPatch/tmpPatch.apk"
//            val absolutePath = "/data/user/0/com.want.replugin/app_tmpPatch/tmpPatch.apk"
            val absolutePath = "/sdcard/Android/data/com.want.replugin/app_tmpPatch/tmpPatch.apk"
            Log.e("CrashReport", "onCreate: $absolutePath")
            Beta.applyTinkerPatch(applicationContext, absolutePath)
        }
    }
}