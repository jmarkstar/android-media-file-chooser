package com.jmarkstar.mfc.util;

import android.support.v4.content.FileProvider;

/**
 * Providing a custom {@code FileProvider} prevents manifest {@code <provider>} name collisions.
 *
 * https://developer.android.com/guide/topics/manifest/provider-element.html
 *
 * android:name: The name of the class that implements the content provider, a subclass of ContentProvider.
 * This should be a fully qualified class name (such as, "com.example.project.TransportationProvider").
 * However, as a shorthand, if the first character of the name is a period, it is appended to the package name specified in the <manifest> element.
 *
 * Created by jmarkstar on 24/06/2017.
 */
public class MfcFileProvider extends FileProvider {
    // This class intentionally left blank.
}
