# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\android-sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:

#   public *;
#}

-keep class * extends android.app.Activity {*;}
-keep class * extends android.app.Application
-keep class * extends android.app.Fragment {*;}
-keep class android.support.design.** { *;}
-keep class android.support.v7.** { *; }
-keep class android.support.v4.** { *; }
#-keep class com.ddmax.zjnucloud.** { *;}
-keep class com.ddmax.zjnucloud.ui.view.**
-keep class com.ddmax.zjnucloud.common.widget.**
-keep class com.ddmax.courseview.**
-keep class com.samsistemas.calendarview.**
-keep class com.soundcloud.android.crop.**

# Gson model
-keep class com.ddmax.zjnucloud.model.** { *;}

# Keep glide
-keep class com.bumptech.glide.**

-keep class pl.droidsonroids.gif.**

# Keep bmob
-libraryjars libs/*.jar
-keep class cn.bmob.v3.** {*;}
-keep class com.bmob.**
-dontwarn cn.bmob.**
# 保证继承自BmobObject、BmobUser类的JavaBean不被混淆
-keep class * extends cn.bmob.v3.BmobObject {
    *;
}

# Keep baidu
-keep class com.baidu.** {*;}
-keep class vi.com.** {*;}
-dontwarn com.baidu.**

# Keep annotation
-keepattributes *Annotation*
-dontwarn java.lang.invoke.*

# Keep butterknife
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

-dontwarn okio.**
-dontwarn org.joda.convert.**
-dontnote com.google.vending.licensing.ILicensingService
-dontnote com.android.vending.licensing.ILicensingService
-dontnote android.support.**
-dontnote com.baidu.**
-dontnote com.google.gson.**
-dontnote com.lidroid.xutils.**
-dontnote com.squareup.**
-dontnote org.joda.**
-dontnote pl.droidsonroids.gif.**
-dontnote retrofit.**
-dontnote com.ddmax.zjnucloud.ui.activity.**

# Keep retrofit
-dontwarn retrofit.**
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions
-dontwarn com.squareup.okhttp.**
-keep class com.squareup.okhttp.** { *;}
-keep interface com.squareup.okhttp.** { *; }
-dontwarn okio.**

-keep class com.rengwuxian.materialedittext.**
-keep class com.lidroid.xutils.**

# Gson
-keep class com.google.gson.**
-keepattributes EnclosingMethod
# Gson specific classes
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }

# Hide Log.v
-assumenosideeffects class android.util.Log {
    public static int v(...);
}

# ActiveAndroid
-keep class com.activeandroid.** { *; }
-keep class com.activeandroid.**.** { *; }
-keep class * extends com.activeandroid.Model
-keep class * extends com.activeandroid.serializer.TypeSerializer

-keepclassmembers class ** {
    public void onEvent*(**);
}

-keep public class com.ddmax.zjnucloud.R$*{
		public static final int *;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

# for stacktrace
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable,InnerClasses,Signature