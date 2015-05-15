# Appstart configuration #

## Config file properties ##

| **Property** | **Description** | **Default value** |
|:-------------|:----------------|:------------------|
| app.main.class | a fully-qualified class. **Required** | null |
| app.libs.dir | the directory where appstart can find the dependent jars | "" |
| app.vm.options | a list of VM options | "" |
| app.class.path | additional class paths | "" |
| app.follow | set to true to follow the application output to console. Useful for debugging purposes | false |

Furthermore, you should note that each key=value pair in the appstart.properties file is **put as a system property** in the spawned application, so you can specify other useful entries such as **java.util.logging.config.file**, **http.proxyHost**, **java.rmi.codebase**, ...

## Appstart system properties ##

| appstart.properties | overrides the path to the appstart.properties file |
|:--------------------|:---------------------------------------------------|
| appstart.verbose | print log information about the start process |