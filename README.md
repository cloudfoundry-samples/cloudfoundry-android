#Cloud Foundry Android

This is an Android application that allows you to monitor and control any Cloud Foundry compatible Cloud.

##Using the Application
Please make sure to read this short app documentation [here](https://github.com/cloudfoundry-samples/cloudfoundry-android/wiki), as it contains important usage guidelines.

[![Android app on Google Play](http://developer.android.com/images/brand/en_generic_rgb_wo_60.png)](http://play.google.com/store/apps/details?id=org.cloudfoundry.android.cfdroid)

##Building

This project uses some third party Android 'Library' projects and as such, needs special handling.
Please follow the instructions carefully :

### Cloning ###
This project embeds [ActionBarSherlock][abs] and [ViewPagerIndicator][vpi] as Git submodules. Be sure to use

    $ git clone --recursive git@github.com:cloudfoundry-samples/cloudfoundry-android.git
or alternatively

    $ git clone git@github.com:cloudfoundry-samples/cloudfoundry-android.git
    $ cd cloudfoundry-android
    $ git submodule init
    $ git submodule update
### Eclipse ###
You'll need to have Eclipse [ADT Plugin][adt] installed. Then, do the following:

1. Use "New > Android Project from Existing Code" and browse to `ActionBarSherlock/library`.
2. Rename this project in Eclipse from `library` to _eg._ `ActionBarSherlock` (this is to avoid having two projects named `library`)
3. Use "New > Android Project from Existing Code" and browse to `ViewPagerIndicator/library`.
4. Again, rename this project to _eg._ `ViewPagerIndicator`
5. Finally, import the actual application from the `cfdroid` subfolder.

[abs]: https://github.com/JakeWharton/ActionBarSherlock 
[vpi]: https://github.com/JakeWharton/Android-ViewPagerIndicator
[adt]: http://developer.android.com/tools/sdk/eclipse-adt.html
