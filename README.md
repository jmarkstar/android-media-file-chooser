[![](https://jitpack.io/v/jmarkstar/android-media-file-chooser.svg)](https://jitpack.io/#jmarkstar/android-media-file-chooser)


# android-media-file-chooser
&lt;in Process>, Im going to develop a library to pick media files for android following those especifications https://developer.android.com/guide/topics/media/media-formats.html.



Step 1. Add it in your root build.gradle at the end of repositories:


```gradle
allprojects {
  repositories {
    maven { url 'https://jitpack.io' }
  }
}
```
	
Step 2. Add the dependency

```gradle
dependencies {
    compile 'com.github.jmarkstar:android-media-file-chooser:0.1.0.alpha'
 }
 ```

Step 3. Add this provider on your AndroidManifest.xml.

```xml
<provider
    android:name="android.support.v4.content.FileProvider"
    android:authorities="${applicationId}.provider"
    android:exported="false"
    android:grantUriPermissions="true">
    <meta-data
        android:name="android.support.FILE_PROVIDER_PATHS"
        android:resource="@xml/provider_paths"/>
</provider>
```