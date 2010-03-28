/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.appstart;

import java.awt.SplashScreen;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 *
 * @author marco
 */
public class Starter {
    static Logger log = Logger.getLogger("AppStart");

    public static final String APPSTART_MAIN_CLASS = "app.main.class";
    public static final String APP_VM_OPTIONS = "app.vm.options";
    public static final String APPSTART_FILE = "appstart.properties";
    public static final String JAVA_PATH = "bin" + File.separator + "java";
    /**
     * Holds the child process
     */
    static Process child;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        if (!Boolean.getBoolean("appstart.verbose")) {
            log.setLevel(Level.OFF);
        }
        File java = new File(System.getProperty("java.home"),
                JAVA_PATH);
        log.info("using java in " + java.getParent());

        // get the class path URLs
        URL launcherUrl = ((URLClassLoader) Starter.class.getClassLoader()).getURLs()[0];

        // search for a appstart.properties file
        File appstartDir = new File(launcherUrl.toURI());
        if (appstartDir.isFile()) {
            appstartDir = appstartDir.getParentFile();
        }
        log.info("appstart dir: " + appstartDir);

        Properties launchProps = findLaunchProperties(appstartDir, null);
        // searching for appstart.properties in current directory
        launchProps = findLaunchProperties(new File("."), launchProps);
        if (launchProps == null) {
            javax.swing.JOptionPane.showMessageDialog(
                    null,
                    "Missing config file " + APPSTART_FILE,
                    "Error starting application",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        // build the class path
        String classpath = new File(
                appstartDir,
                launchProps.getProperty("app.class.path", "")).
                getPath().concat(File.pathSeparator);
        
        File libsDir = new File(appstartDir, launchProps.getProperty(
                "app.libs.dir", "lib"));
        log.info("libs dir: " + libsDir);
        if (libsDir.isDirectory()) {
            File[] libs = libsDir.listFiles();
            for (File lib : libs) {
                classpath = classpath.
                        concat(lib.getAbsolutePath()).
                        concat(File.pathSeparator);
            }
        }
        log.info("classpath = " + classpath);

        List<String> vmOptions = new ArrayList<String>();
        String vmOptionsString = launchProps.getProperty(
                APP_VM_OPTIONS, "");
        log.info("vmoptions = " + vmOptionsString);
        for (StringTokenizer st = new StringTokenizer(vmOptionsString); st.hasMoreTokens();) {
            String token = st.nextToken();
            vmOptions.add(token);
        }

        // build system properties based on the key=value of the appstart.properties file
        List<String> systemProps = new ArrayList<String>();
        for (String prop : launchProps.stringPropertyNames()) {
            systemProps.add(String.format(
                    "-D%s=%s", prop, launchProps.getProperty(prop)));
        }
        String mainClass = launchProps.getProperty(
                APPSTART_MAIN_CLASS);
        log.info("main class = " + mainClass);
        if (mainClass == null) {
            javax.swing.JOptionPane.showMessageDialog(
                    null,
                    "Missing config entry " + APPSTART_MAIN_CLASS,
                    "Error starting application",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        // flag to follow child process output
        Boolean follow = Boolean.parseBoolean(launchProps.getProperty(
                "app.follow", "false"));
        File splash = new File(appstartDir, "splash.jpg");

        ArrayList<String> cmd = new ArrayList<String>();
        cmd.add(java.getAbsolutePath());
        cmd.addAll(vmOptions);
        if (splash.isFile()) {
            log.info("splash file: " + splash.getAbsolutePath());
            cmd.add("-splash:" + splash.getAbsolutePath());
        }
        cmd.addAll(systemProps);
        cmd.add("-cp");
        cmd.add(classpath);
        cmd.add(mainClass);
        for (int i = 0; i < args.length; i++) {
            cmd.add(args[i]);
        }
        log.info("command line:\n" + cmd);
        ProcessBuilder pb = new ProcessBuilder(cmd);

        pb.redirectErrorStream(true);
        child = pb.start();

        SplashScreen.getSplashScreen().close();
        if (follow) {
            log.info("starting follower thread");
            new FollowerThread().start();
        }
//        System.out.println(Runtime.getRuntime().maxMemory());
//        System.out.println("Bootstrap terminating");
        //System.exit(0);
    }

    private static Properties findLaunchProperties(File file, Properties launchProps) throws IOException {
        if (file.isFile()) {
            file = file.getParentFile();
        }
//        System.out.println("searching launch.properties in " + file);
        File propFile = new File(file, APPSTART_FILE);
        if (propFile.isFile()) {
//            System.out.println("found launch.properties here");
            launchProps = new Properties(launchProps);
            InputStream in = new FileInputStream(propFile);
            launchProps.load(in);
            in.close();

            return launchProps;
        } else {
            return launchProps;
        }
    }

    /**
     * Writes on stdout the output of the child thread
     */
    static class FollowerThread extends Thread {

        public FollowerThread() {
            setPriority(MIN_PRIORITY);
        }

        @Override
        public void run() {
            InputStream stream = child.getInputStream();
            byte[] buf = new byte[1024];
            try {
                int read = stream.read(buf);
                while (read >= 0) {
                    System.out.write(buf, 0, read);
                    read = stream.read(buf);
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            } finally {
            }
        }
    }
}
