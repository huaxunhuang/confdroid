# Quick Start

We build ConfDroid using Java 1.8. We did not test on other Java versions.

Download the Android framework jars in this <a href="https://repo1.maven.org/maven2/org/robolectric/android-all/">link</a>. Note that android.jar in Android Studio does not work since it has removed all the code logic defined in the Android framework. 

ConfDroid takes the following parameters as inputs:
* apiLevel1: the API level of the android jar passed in androidJarPath1
* androidJarPath1: A path points to a directory containing the android jar of apiLevel1
* apiLevel2: the API level of the android jar passed in androidJarPath2
* androidJarPath2: A path points to a directory containing the android jar of apiLevel2

You can launch ConfDroid as follows:

```
java -jar ConfDroid.jar $apiLevel1 $androidJarPath1 $apiLevel2 $androidJarPath2
```
The output of ConfDroid will be the incompatibility-inducing configuration attribute, tag, data format, incompatibility-inducing API levels.

It may take several hours for ConfDroid to stop. The output of the ConfDroid will be like as follows:
```
attribute: android:tint
tag: shape
data format: color|styled_color
API level: 21,22
```
