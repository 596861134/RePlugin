此版本为集成bugly+tinker+walle版本

参考：
walle：
https://tech.meituan.com/2017/01/13/android-apk-v2-signature-scheme.html
https://github.com/Meituan-Dianping/walle

bugly+tinker：
https://bugly.qq.com/docs/user-guide/instruction-manual-android-hotfix-demo/?v=20170512172046#_3


集成完成之后首先需要打基准包:
1.执行AS右侧Gradle->project->Tasks->package->assembleReleaseChannels(生成release版本的多渠道基准包)
2.执行上面的操作后会在app->build下面生成bakApk(里面是基准包)，在app->build->outputs下面生成channels(里面是渠道包)
3.修复完bug后开始生成patch修复包
4.执行AS右侧Gradle->project->Tasks->tinker-support->buildTinkerPatchRelease(生成release版本的patch修复包)
5.执行完上面的操作后会在app->build->outputs下面生成patch(里面是修复包)
6.一般我们会选择patch->release->patch_signed_7zip.apk上传到后台服务器
7.等待下发成功，apk会自动下载并合入patch包重启


注：
1.多渠道包也只在bakApk下生成一个基准包，生成patch修复包的时候也是依据这一个基准包来生成所有渠道包的patch包
2.demo中使用了2中接入方式，改造和非改造Application方案
3.非改造方案需要在'attachBaseContext'方法中调用'Beta.installTinker()'，且'enableProxyApplication = true'
4.改造方案需要在'onBaseContextAttached'方法中调用'Beta.installTinker(this)'，且'enableProxyApplication = false'
5.非改造方案1.9.14.8和以后的版本官方已经放弃维护


//获得基准包的tinkerId
String oldTinkerId = TinkerManager.getTinkerId();
//获得补丁包的tinkerId
String newTinkerId = TinkerManager.getNewTinkerId();

//清除补丁 清除补丁之后，就会回退基线版本状态。
Beta.cleanTinkerPatch();