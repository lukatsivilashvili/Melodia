# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
# ==== DISABLE ALL OBFUSCATION ====
# Keep all @Serializable classes and their serializers
# ===== KOTLIN SERIALIZATION =====
-keep @kotlinx.serialization.Serializable class ** { *; }
-keepclassmembers class * implements kotlinx.serialization.Serializable {
    <methods>;
}

# Keep Kotlin Serialization runtime
-keep class kotlinx.serialization.** { *; }
-keepclassmembers class kotlinx.serialization.** { *; }

# Keep critical annotations/metadata
-keepattributes *Annotation*, InnerClasses, Signature
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations

# For debugging (optional)
-keepattributes SourceFile, LineNumberTable