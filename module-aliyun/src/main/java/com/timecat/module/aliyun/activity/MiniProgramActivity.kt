package com.timecat.module.aliyun.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.alipay.android.phone.scancode.export.ScanCallback
import com.alipay.android.phone.scancode.export.ScanRequest
import com.alipay.android.phone.scancode.export.ScanService
import com.alipay.mobile.framework.LauncherApplicationAgent
import com.alipay.mobile.nebula.provider.H5ViewProvider
import com.alipay.mobile.nebula.provider.TinyOptionMenuViewProvider
import com.alipay.mobile.nebula.util.H5Utils
import com.alipay.mobile.nebula.view.H5NavMenuView
import com.alipay.mobile.nebula.view.H5PullHeaderView
import com.alipay.mobile.nebula.view.H5TitleView
import com.alipay.mobile.nebula.view.H5WebContentView
import com.mpaas.mas.adapter.api.MPLogger
import com.mpaas.nebula.adapter.api.MPNebula
import com.mpaas.nebula.adapter.api.MPTinyHelper
import com.timecat.component.alert.ToastUtil
import com.timecat.middle.setting.BaseSettingActivity
import com.timecat.module.aliyun.view.TinyNavigationBar
import com.timecat.module.aliyun.view.TinyOptionMenuView

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/8/14
 * @description null
 * @usage null
 */
class MiniProgramActivity :BaseSettingActivity() {
    override fun title(): String = "小程序"
    override fun addSettingItems(container: ViewGroup) {
        container.Next("官方示例") {
            MPNebula.startApp("0000000000000000")
        }
        container.Next("扫码调试") {
            // 预览调试需要设置白名单 ID
            setUserId()
            scanPreviewOrDebugQRCode()
        }
        container.Next("自定义标题栏") {
            initCustomTitle()
            MPNebula.startApp("0000000000000000")
        }
        container.Next("官方示例2") {
            startActivity(Intent(this, FastStartActivity::class.java))
        }
        checkPermission()
    }
    private fun initCustomTitle() {
        // 自定义标题栏
        MPNebula.setCustomViewProvider(object : H5ViewProvider {
            override fun createTitleView(context: Context): H5TitleView {
                // 返回自定义 title
                return TinyNavigationBar(context)
            }

            override fun createNavMenu(): H5NavMenuView? {
                return null
            }

            override fun createPullHeaderView(
                context: Context,
                viewGroup: ViewGroup
            ): H5PullHeaderView? {
                return null
            }

            override fun createWebContentView(context: Context): H5WebContentView? {
                return null
            }
        })
        // 自定义小程序右上角配置栏
        H5Utils.setProvider(TinyOptionMenuViewProvider::class.java.name,
            TinyOptionMenuViewProvider { context -> TinyOptionMenuView(context) })
    }
    private fun setUserId() {
        // 设置白名单 ID，这里的 ID 可以是任意字符串
        MPLogger.setUserId("123456")
    }

    private fun scanPreviewOrDebugQRCode() {
        val service = LauncherApplicationAgent.getInstance().microApplicationContext
            .findServiceByInterface<ScanService>(ScanService::class.java.name)
        val scanRequest = ScanRequest()
        scanRequest.setScanType(ScanRequest.ScanType.QRCODE)
        service.scan(this, scanRequest, ScanCallback { success, result ->
            if (result == null || !success) {
                showScanError()
                return@ScanCallback
            }
            val uri = result.data
            if (uri == null) {
                showScanError()
                return@ScanCallback
            }
            // 启动预览或调试小程序，第二个参数为小程序启动参数
            MPTinyHelper.getInstance().launchIdeQRCode(uri, Bundle())
        })
    }

    private fun showScanError() {
        runOnUiThread {
            ToastUtil.e("错误")
        }
    }
    protected fun checkPermission() {
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED)
            || (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED)
            || (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED)
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                0
            )
        }
    }

}