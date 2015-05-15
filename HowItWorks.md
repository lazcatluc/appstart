# How Appstart works #

At the core of Appstart, a child process is spawned after the launch of the Appstart jar file. Basically, everything boils down to:
```
Runtime.getRuntime().exec(<java process with parameters>);
```
of course, there is more that that:
  * The position of the "java" launcher is taken from the system property ${java.home}. So, the JRE used to run start.jar is also used to run the child process.
  * The folder in which resides the start.jar file is called the _"Application folder"_
  * A file named _appstart.properties_ is searched inside the application folder. Inside it there must be the reference to the main class to launch and eventual JVM options to use.
  * If a subfolder of the application folder named _"lib"_ is found, every folder and jar file placed in there will be added to the classpath. You can configure the position of the lib folder in the appstart.properties file
  * If a file named "splash.img" is found in the application folder, it is used as a splash screen of the child java process. This feature requires the use of Java 6.

The final command line run by Appstart is this:
```
${java.home}/bin/java <app.vm.options> -cp <app.class.path> -splash:splash.img <app.main.class>
```