# Introduction #
One of the frustrations about writing Java desktop applications is the setup needed to launch the Java Virtual Machine. This is often implemented using some platform-dependent launch scripts, which their only role is to find a suitable JRE and launch it with some options.

This is not optimal, since this defeats the cross-platform nature of Java.

Enter **Appstart**, the universal cross-platform Java application launcher.

# What is Appstart ? #
Appstart is a _cross-platform application launcher_ (is written in Java itself), that relieves you from writing another shell/batch script to launch a Java desktop application.
All you have to do is:
  * put the appstart.jar in your application dir. You can rename it to your likings, such as run.jar, start.jar, your\_app\_name.jar
  * setup the appstart.properties file, specifying the main class to launch and other VM options (see AppstartProperties)
  * then put all your jars and classes in the subfolder "lib".

Voila! You can now double-click the appstart.jar and your application is started, without hideous DOS windows, class-path headhaches or fears of the dreaded OutOfMemoryException.

Even more: do you want to show a splash screen for your application ? Then place a splash.img file (can be jpeg, png or gif format) and you will automatically have a splash screen that is loaded at JRE startup (this feature leverages the -splash option of JavaSE 6).

To develop Appstart i took inspiration from the Java Web Start technology, but this project is much simpler and oriented towards standard desktop applications.

# Features #
  * Easy set-up and configuration, uses a small `start.jar` file and a `appstart.properties` file. No fighting with class-path or current directory issues.
  * Each application resides in its own folder in disk.
  * Absolutely cross-platform
  * No dependencies, only a Java Runtime Environment is needed
  * Do not write and run .bat or .sh scripts anymore. All you ned to do is a double click on `start.jar` !
  * Specify system properties and VM heap space for the application in the `appstart.properties` file. No more hard-coded configuration or OutOfMemoryException !
  * Support for JavaSE 6 splash screen
  * Open source, licensed with the [MIT license](http://www.opensource.org/licenses/mit-license.php)

# But isn't this already done by other projects ? #
There are other projects with similar objectives, some of these (like [launch4j](http://launch4j.sourceforge.net/)) create small .exe files that launch the Java virtual machine.
Of course this solution works only on Windows machines: linux machines will still have to run a shell script.