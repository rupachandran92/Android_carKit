# Easy-Response-App
Listens to User’s whistling sound to answer the phone. User can reject the call by just shaking the phone. Activate voice commands to invoke Camera, Music, and Nearby Restaurants etc. Used an API “musicG.jar” Audio Analysis Library and Sensor Manager.

System Requirements:
For Windows
•	Microsoft® Windows® 7/8/10 (32- or 64-bit)
•	2 GB RAM minimum, 8 GB RAM recommended
•	2 GB of available disk space minimum,
4 GB Recommended (500 MB for IDE + 1.5 GB for Android SDK and emulator system image)
•	1280 x 800 minimum screen resolution
•	Java Development Kit (JDK) 8
•	For accelerated emulator: 64-bit operating system and Intel® processor with support for Intel® VT-x, Intel® EM64T (Intel® 64), and Execute Disable (XD) Bit functionality

Check the system requirements above and follow the below steps to download the Android Studio,

  Android Studio is Android's official IDE.  Download using this link: https://developer.android.com/studio/index.html 
While the Android Studio download completes, verify which version of the JDK you have: open a command line and type javac -version. If the JDK is not available or the version is lower than 1.8, download the Java SE Development Kit 8 here using this link: http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html.

Install Android Studio:

To install Android Studio on Windows, proceed as follows:

Launch the .exe file you downloaded. Follow the setup wizard to install Android Studio and any necessary SDK tools.
On some Windows systems, the launcher script does not find where the JDK is installed. If you encounter this problem, you need to set an environment variable indicating the correct location.

Select Start menu > Computer > System Properties > Advanced System Properties. Then open Advanced tab > Environment Variables and add a new system variable JAVA_HOME that points to your JDK folder, for example C:\Program Files\Java\jdk1.8.0_77.

How to Run:

1.) Download the code from the github and unzip it

2.) Open the Android Studio and it will give you 2 options: 
     1.) Open the existing project 
     2.) Create a new project. 
You already have a project so just go the path and open the unzipped file. It will open the project in the Android studio. Download the latest SDK tools and platforms using the SDK Manager before running the project.

Run on a Real Device:

If you have a device running Android, here's how to install and run your app.

Set up your device
1.	Plug in your device to your development machine with a USB cable. If you're developing on Windows, you might need to install the appropriate USB driver for your device. For help installing drivers, see the OEM USB Drivers document.
2.	Enable USB debugging on your device. On Android 4.0 and newer, go to Settings > Developer options.

Run the app from Android Studio
1.	Select one of your project's files and click Run   from the toolbar.
3.	In the Choose Device window that appears, select the Choose a running device radio button, select your device, and click OK .
4.	Android Studio installs the app on your connected device and starts it
5.	 Smile appears on the emulator screen. “Smile is project name which I given”

Tips: If you are new to the android developer follow the link to get good understanding. https://developer.android.com/training/basics/firstapp/index.html



