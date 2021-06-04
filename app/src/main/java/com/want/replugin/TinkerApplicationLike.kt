package com.want.replugin

import android.annotation.TargetApi
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.multidex.MultiDex
import com.meituan.android.walle.WalleChannelReader
import com.tencent.bugly.Bugly
import com.tencent.bugly.beta.Beta
import com.tencent.bugly.beta.interfaces.BetaPatchListener
import com.tencent.tinker.entry.DefaultApplicationLike
import java.util.*


/**
 * Created by chengzf on 2021/5/18.
 */
class TinkerApplicationLike(
    application: Application, tinkerFlags: Int, tinkerLoadVerifyFlag: Boolean,
    applicationStartElapsedTime: Long, applicationStartMillisTime: Long, tinkerResultIntent: Intent
) : DefaultApplicationLike(
    application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime,
    applicationStartMillisTime, tinkerResultIntent) {

    override fun onBaseContextAttached(base: Context?) {
        super.onBaseContextAttached(base)
        //分包
        Log.e("CrashReport", "MultiDex: 初始化")
        MultiDex.install(base)
        Beta.installTinker(this)
    }

    override fun onCreate() {
        super.onCreate()
        initTinker()
    }

    private fun initTinker() {
        Log.e("CrashReport", "initTinker: Tinker开始初始化")
        // 设置是否开启热更新能力，默认为true
        Beta.enableHotfix = true
        // 检查是否有热更新
        Beta.autoCheckHotfix = true
        // 设置是否自动下载补丁，默认为true
        Beta.canAutoDownloadPatch = true
        // 设置是否自动合成补丁，默认为true
        Beta.canAutoPatch = true
        // 设置是否提示用户重启，默认为false
        Beta.canNotifyUserRestart = true
        // 补丁回调接口
        Beta.betaPatchListener = object : BetaPatchListener {
            override fun onPatchReceived(patchFile: String?) {
                Log.e("CrashReport", "onPatchReceived: 补丁下载地址$patchFile")
            }

            override fun onDownloadReceived(savedLength: Long, totalLength: Long) {
                val format = String.format(
                    Locale.getDefault(), "%s %d%%",
                    Beta.strNotificationDownloading,
                    (if (totalLength == 0L) 0 else savedLength * 100 / totalLength).toInt()
                )
                Log.e("CrashReport", "onDownloadReceived: $format")
            }

            override fun onDownloadSuccess(msg: String?) {
                Log.e("CrashReport", "onDownloadSuccess: 补丁下载成功,$msg")
            }

            override fun onDownloadFailure(msg: String?) {
                Log.e("CrashReport", "onDownloadFailure: 补丁下载失败,$msg")
            }

            override fun onApplySuccess(msg: String?) {
                Log.e("CrashReport", "onDownloadSuccess: 补丁应用成功,$msg")
            }

            override fun onApplyFailure(msg: String?) {
                Log.e("CrashReport", "onDownloadSuccess: 补丁应用失败,$msg")
            }

            override fun onPatchRollback() {
                Log.e("CrashReport", "onPatchRollback ")
            }

        }

        //安装了这个app的设备设置为启动测试机,这样可以在开发测试阶段,只下发测试补丁,不影响线上用户
        Bugly.setIsDevelopmentDevice(application,true)
        //塞入渠道信息
        val channel = WalleChannelReader.getChannel(application)
        Bugly.setAppChannel(application, channel)
        //初始化
        Bugly.init(application, "00159bf539", true)
    }

    override fun onTerminate() {
        super.onTerminate()
        Beta.unInit()
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    fun registerActivityLifecycleCallback(callbacks: ActivityLifecycleCallbacks?) {
        application.registerActivityLifecycleCallbacks(callbacks)
    }

}