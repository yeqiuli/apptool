-optimizationpasses 5          # 指定代码的压缩级别
-dontusemixedcaseclassnames   # 是否使用大小写混合
-dontpreverify           # 混淆时是否做预校验
-verbose                # 混淆时是否记录日志
-ignorewarnings       #抑制警告 这个不建议加 可以不用写

#-dontobfuscate
#-dontoptimize

-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*  # 混淆时所采用的算法
-keep public class * extends android.app.Activity      # 保持哪些类不被混淆
-keep public class * extends android.app.Application   # 保持哪些类不被混淆
-keep public class * extends android.app.Service       # 保持哪些类不被混淆
-keep public class * extends android.content.BroadcastReceiver  # 保持哪些类不被混淆
-keep public class * extends android.content.ContentProvider    # 保持哪些类不被混淆
-keep public class * extends android.app.backup.BackupAgentHelper # 保持哪些类不被混淆
-keep public class * extends android.preference.Preference        # 保持哪些类不被混淆
#-keep public class com.android.vending.licensing.ILicensingService    # 保持哪些类不被混淆
-keepclassmembers class * extends android.app.Activity { # 保持自定义控件类不被混淆
    public void *(android.view.View);
}
-keepclassmembers enum * {     # 保持枚举 enum 类不被混淆
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keepclassmembers enum * {     # 保持枚举 enum 类不被混淆
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keepclasseswithmembernames class * {  # 保持 native 方法不被混淆
    native <methods>;
}
#保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context,android.util.AttributeSet);
 }
-keepclasseswithmembers class * {# 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keep public class * extends android.view.View{
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}
################不混淆R类##################
-keep public class com.tecsun.fkyStandard.R$*{
   public static final int *;
}
 ####################support.v4#####################
-dontwarn android.
##OkHttp3
-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.** { *;}
-dontwarn okio.**
# Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
# RxJava RxAndroid
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}
#自定义类
-keep class com.hgy.bean.** {*;}
-keep class com.hgy.jsonAdapter.** {*;}

#bugly
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}

#andserver
-keep class com.yanzhenjie.andserver.** {*;}
#MQTT
-keep class org.eclipse.paho.client.mqttv3.** {*;}
-keep class org.eclipse.paho.android.service.** {*;}
#组件库
-keep class com.tecsun.network.bean.** {*;}

-keep class com.turbomanage.** {*;}
#ZLoading
-keep class com.zyao89.view.** {*;}
#摄像头
-keep class com.guo.android_extend.** {*;}

-keep class httpasyncclient.org.apache.http.**{*;}
-keep class httpclient.org.apache.http.**{*;}
-keep class httpcode.org.apache.commons.**{*;}
-keep class httpcore.org.apache.http.**{*;}
-keep class httpcorenio.org.apache.http.**{*;}
-keep class httplog.org.apache.commons.**{*;}
-keep class org.ietf.jgss.**{*;}


##Glide
-dontwarn com.bumptech.glide.**
-keep class com.bumptech.glide.**{*;}
#-keep public class * implements com.bumptech.glide.module.GlideModule
#-keep public class * extends com.bumptech.glide.AppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

#解决报错
-dontwarn org.xmlpull.v1.XmlPullParser
-dontwarn org.xmlpull.v1.XmlSerializer
-keep class org.xmlpull.v1.* {*;}

#关闭输出
-assumenosideeffects class android.util.Log {
  public static boolean isLoggable(java.lang.String, int);
    public static *** d(...);
    public static *** w(...);
    public static *** v(...);
    public static *** i(...);
     public static int wtf(...);
}

#不混淆Parcelable和它的实现子类，还有Creator成员变量
    -keep class * implements android.os.Parcelable {
      public static final android.os.Parcelable$Creator *;
    }

    #不混淆Serializable和它的实现子类、其成员变量
    -keepclassmembers class * implements java.io.Serializable {
        static final long serialVersionUID;
        private static final java.io.ObjectStreamField[] serialPersistentFields;
        private void writeObject(java.io.ObjectOutputStream);
        private void readObject(java.io.ObjectInputStream);
        java.lang.Object writeReplace();
        java.lang.Object readResolve();
}
  #使用GSON、fastjson等框架时，所写的JSON对象类不混淆，否则无法将JSON解析成对应的对象
    -keepclassmembers class * {
        public <init>(org.json.JSONObject);
    }

    # Room 数据库
    -dontwarn android.arch.util.paging.CountedDataSource
    -dontwarn android.arch.persistence.room.paging.LimitOffsetDataSource

