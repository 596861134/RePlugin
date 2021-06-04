package com.want.replugin

import com.tencent.tinker.loader.app.TinkerApplication
import com.tencent.tinker.loader.shareutil.ShareConstants

/**
 * Created by chengzf on 2021/5/18.
 */

class SimpleApplication : TinkerApplication(
    ShareConstants.TINKER_ENABLE_ALL, "com.want.replugin.TinkerApplicationLike",
    "com.tencent.tinker.loader.TinkerLoader", false) {

}