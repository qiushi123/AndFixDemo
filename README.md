# AndFixDemo
android热修复，打补丁，不用发版本就能实时的解决一些bug

#背景 
  当一个App发布之后，突然发现了一个严重bug需要进行紧急修复，这时候公司各方就会忙得焦头烂额：重新打包App、测试、
	向各个应用市场和渠道换包、提示用户升级、用户下载、覆盖安装。有时候仅仅是为了修改了一行代码，
	也要付出巨大的成本进行换包和重新发布。
  这时候就提出一个问题：有没有办法以补丁的方式动态修复紧急Bug，
	不再需要重新发布App，不再需要用户重新下载，覆盖安装？


#搜索发现有这3种方式可以实现（至于其他的方式，暂不清楚）

	1.dexposed     github https://github.com/alibaba/dexposed

	2.andfix   github https://github.com/alibaba/AndFix

	3.bsdiff  http://blog.csdn.net/lazyer_dog/article/details/47173013

	dexposed和andfix是alibaba的开源项目，都是apk增量更新的实现框架，目前dexposed的兼容性较差，
	只有2.3，4.0~4.4兼容，其他Android版本不兼容或未测试，详细可以去dexposed的github项目主页查看，
	而andfix则兼容2.3~6.0，所以就拿这个项目来实现增量更新吧。至于bsdiff，只是阅览了一下，还没研究过。



#使用步骤

##一，Android studio可以在build.gradle里导入andfix，
	  compile 'com.alipay.euler:andfix:0.3.1'

	gradle导入的话还有个小问题，所以还是建议把类库源码导入到你的项目中去
	但是我是使用module的方式添加andfix，这样可以直接查看编辑源码，而且直接，后面再说。
	我的项目里有andfix类库源码，你可以直接把源码导入到项目中，如下图所示
![image](https://github.com/qiushi123/AndFixDemo/blob/master/imagsDemo/1.png?raw=true)
	
	
##二，在你的项目记得新建jniLibs文件夹，把我项目中jniLibs里的so文件移到你的jniLibs里，如下图。
![image](https://github.com/qiushi123/AndFixDemo/blob/master/imagsDemo/2.png?raw=true)

三，把我项目的以下代码拷贝到你的Application中，（我项目简单起见就两个类：mainactivity和myapplication）
	public class MainApplication extends Application {
    private static final String TAG = "euler";
    private static final String APATCH_PATH = "/out.apatch";
    private static final String DIR = "apatch";//补丁文件夹
    /**
     * patch manager
     */
    private PatchManager mPatchManager;

    @Override
    public void onCreate() {
        super.onCreate();
        // initialize
        mPatchManager = new PatchManager(this);
        mPatchManager.init("1.0");
        Log.d(TAG, "inited.");

        // load patch
        mPatchManager.loadPatch();
        //        Log.d(TAG, "apatch loaded.");

        // add patch at runtime
        try {
            // .apatch file path
            String patchFileString = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + APATCH_PATH;
            mPatchManager.addPatch(patchFileString);
            Log.d(TAG, "apatch:" + patchFileString + " added.");
        } catch (IOException e) {
            Log.e(TAG, "", e);
        }
    }

    }

四，接着打包1.apk（这个1.apk就相当于你上线的版本）
![image](https://github.com/qiushi123/AndFixDemo/blob/master/imagsDemo/4.png?raw=true)

	然后修改mainactivity的toast()，打包2.apk。（
![image](https://github.com/qiushi123/AndFixDemo/blob/master/imagsDemo/5.png?raw=true)

五，生成你的补丁包（最重要的一步）
	这时需要用到：apkpatch-1.0.3 你可以到网上下，也可以直接用我项目里的apkpatch-1.0.3压缩文件
	
	在apkpatch-1.0.3中把你生成的1.apk 和2.apk复制到apkpatch-1.0.3文件中，把你的签名文件：**.keystore也复制到apkpatch-1.0.3文件中
	接下来cmd命令行上场了
![image](https://github.com/qiushi123/AndFixDemo/blob/master/imagsDemo/6.png?raw=true)	
![image](https://github.com/qiushi123/AndFixDemo/blob/master/imagsDemo/7.png?raw=true)	
	
上面输入的命令行：
	apkpatch -f 2.apk -t 1.apk -o output1 -k qiushi.jks -p 123456 -a qiushi -e 123456 
命令行意思：
	apkpatch -f new.apk -t old.apk -o output1 -k 签名文件 -p 签名密码 -a 机构名 -e 机构签名密码

	
六，如无错误，编译后会生成一个apatch文件，改名成out.apatch，如下图
![image](https://github.com/qiushi123/AndFixDemo/blob/master/imagsDemo/8.png?raw=true)

安装打开1.apk
![image](https://github.com/qiushi123/AndFixDemo/blob/master/imagsDemo/9.png?raw=true)

关闭app（不是简单的退出，而是要结束app的进程），将out.apatch放sdcard根目录（就是你的手机内存根目录）后，重新打开app，toast方法改变了
	

七，还有源码混淆


ps：

1. 这里只是简单的测试了一下，没有复杂的功能，而且andfix不支持布局资源等的修改。

2. 使用了apk加固时（360加固，百度加固等等），发现在加固前要先apkpatch制作补丁，
	不能使用加固后的apk制作，否则补丁无法使用，但是在加固前制作的补丁可以很容易的被反编译出源码








