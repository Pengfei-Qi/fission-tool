#  一.官方文档-- Hello World in JVM/Java on Fission

The `io.fission.MysqlAccess.java` class is a very simple fission function that implements `io.fission.Function` and says "Hello, World!" .

## Building and deploying using Fission

Fission's builder can be used to create the binary artifact from source code. Create an environment with builder image and then create a package. 

```
$ zip -r java-src-pkg.zip *
$ fission env create --name java --image fission/jvm-env --version 2 --keeparchive --builder fission/jvm-builder
$ fission package create --sourcearchive java-src-pkg.zip --env java
java-src-pkg-zip-tvd0
$ fission package info --name java-src-pkg-zip-tvd0
Name:        java-src-pkg-zip-tvd0
Environment: java
Status:      succeeded
Build Logs:
[INFO] Scanning for projects...
[INFO] 
[INFO] -----------------------< io.fission:hello-world >-----------------------
[INFO] Building hello-world 1.0-SNAPSHOT
[INFO] --------------------------------[ jar ]---------------------------------
```

Once package's status is `succeeded` then that package can be used to create and execute a function.

```
$ fission fn create --name hello --pkg java-src-pkg-zip-tvd0 --env java --entrypoint io.fission.MysqlAccess
$ fission fn test --name hello
Hello World!
```

## Building locally and deploying with Fission

You can build the jar file in one of the two ways below based on your setup:

- You can use docker without the need to install JDK and Maven to build the jar file from source code:

```bash
$ bash -x ./build.sh

```
- If you have JDK and Maven installed, you can directly build the JAR file using command:

```
$ mvn clean package
```

Both of above steps will generate a target subdirectory which has the archive `target/hello-world-1.0-SNAPSHOT-jar-with-dependencies.jar` which will be used for creating function.

- The archive created above will be used as a deploy package when creating the function.

```
$ fission env create --name jvm --image fission/jvm-env --version 2 --keeparchive=true
$ fission fn create --name hello --deploy target/hello-world-1.0-SNAPSHOT-jar-with-dependencies.jar --env jvm --entrypoint io.fission.MysqlAccess
$ fission route create --function hello --url /hellop --method GET
$ fission fn test --name hello
Hello World!
```
# 二. GitHub 代码提交
参考连接: https://stackoverflow.com/questions/46232906/git-clone-error-rpc-failed-curl-56-openssl-ssl-read-ssl-error-syscall-errno  
 ```shell
git config --global http.sslVerify "false"

git config --global http.postBuffer 524288000

git push
```


# 三. 遗留问题

`2021-12-03` : 未解决  
[![oaEpvD.jpg](https://z3.ax1x.com/2021/12/03/oaEpvD.jpg)](https://imgtu.com/i/oaEpvD)  
```shell
fission httptrigger  list 
# c46f62cf-1178-4866-8718-a48ecb12b36e POST   /tool/{name} fission-tool false   *    /tool/{name}
fission fn list  
# NAME         ENV      EXECUTORTYPE MINSCALE MAXSCALE MINCPU MAXCPU MINMEMORY MAXMEMORY TARGETCPU SECRETS CONFIGMAPS
# db01         java-env poolmgr      0        0        0      0      0         0         0                 
# fission-tool java-env poolmgr      0        0        0      0      0         0         0                 
# msg-test     java-env poolmgr      0        0        0      0      0         0         0                 
# test         nodejs   poolmgr      0        0        0      0      0         0         0
fission fn logs -f  --name fission-tool
# [2021-12-03 07:18:39.283810556 +0000 UTC] 2021-12-03 07:18:39.283 ERROR 1 --- [nio-8888-exec-2] o.a.c.c.C.[.[.[/].[dispatcherServlet]    : Servlet.service() for servlet [dispatcherServlet] in context with path [] threw exception [Handler dispatch failed; nested exception is java.lang.NoClassDefFoundError: cn/hutool/crypto/symmetric/SymmetricAlgorithm] with root cause\n
# [2021-12-03 07:18:39.283841006 +0000 UTC] \n
# [2021-12-03 07:18:39.283845365 +0000 UTC] java.lang.ClassNotFoundException: cn.hutool.crypto.symmetric.SymmetricAlgorithm\n
# [2021-12-03 07:18:39.283849068 +0000 UTC] \u0009at java.net.URLClassLoader.findClass(URLClassLoader.java:382) ~[na:1.8.0_212]\n
# [2021-12-03 07:18:39.28385326 +0000 UTC] \u0009at java.lang.ClassLoader.loadClass(ClassLoader.java:424) ~[na:1.8.0_212]\n
# [2021-12-03 07:18:39.283857273 +0000 UTC] \u0009at java.net.FactoryURLClassLoader.loadClass(URLClassLoader.java:817) ~[na:1.8.0_212]\n
# [2021-12-03 07:18:39.283861601 +0000 UTC] \u0009at java.lang.ClassLoader.loadClass(ClassLoader.java:357) ~[na:1.8.0_212]\n
# [2021-12-03 07:18:39.283867422 +0000 UTC] \u0009at io.fission.util.DeAndEnUtils.enAes(DeAndEnUtils.java:21) ~[na:na]\n
# [2021-12-03 07:18:39.283873151 +0000 UTC] \u0009at io.fission.HelloTool.call(HelloTool.java:32) ~[na:na]\n
# [2021-12-03 07:18:39.283878864 +0000 UTC] \u0009at io.fission.Server.home(Server.java:38) ~[classes!/:0.0.1-SNAPSHOT]\n
# [2021-12-03 07:18:39.283884804 +0000 UTC] \u0009at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method) ~[na:1.8.0_212]\n
# [2021-12-03 07:18:39.283890768 +0000 UTC] \u0009at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62) ~[na:1.8.0_212]\n
# [2021-12-03 07:18:39.28389687 +0000 UTC] \u0009at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) ~[na:1.8.0_212]\n
# [2021-12-03 07:18:39.283902721 +0000 UTC] \u0009at java.lang.reflect.Method.invoke(Method.java:498) ~[na:1.8.0_212]\n
# [2021-12-03 07:18:39.283906519 +0000 UTC] \u0009at org.springframework.web.method.support.InvocableHandlerMethod.doInvoke(InvocableHandlerMethod.java:209) ~[spring-web-5.0.5.RELEASE.jar!/:5.0.5.RELEASE]\n
# [2021-12-03 07:18:39.283910503 +0000 UTC] \u0009at org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:136) ~[spring-web-5.0.5.RELEASE.jar!/:5.0.5.RELEASE]\n
# [2021-12-03 07:18:39.283914346 +0000 UTC] \u0009at org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:102) ~[spring-webmvc-5.0.5.RELEASE.jar!/:5.0.5.RELEASE]\n
# [2021-12-03 07:18:39.283918237 +0000 UTC] \u0009at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:877) ~[spring-webmvc-5.0.5.RELEASE.jar!/:5.0.5.RELEASE]\n
# [2021-12-03 07:18:39.283936436 +0000 UTC] \u0009at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:783) ~[spring-webmvc-5.0.5.RELEASE.jar!/:5.0.5.RELEASE]\n
# 
# ...........................
# 
# 
# [2021-12-03 07:18:39.284104167 +0000 UTC] \u0009at java.lang.Thread.run(Thread.java:748) [na:1.8.0_212]\n
# [2021-12-03 07:18:39.284107547 +0000 UTC] \n
# [2021-12-03 07:26:48.63112746 +0000 UTC] 2021-12-03 07:26:48.630  INFO 1 --- [       Thread-3] ConfigServletWebServerApplicationContext : Closing org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext@3d04a311: startup date [Fri Dec 03 06:57:36 GMT 2021]; root of context hierarchy\n
# [2021-12-03 07:26:48.633155275 +0000 UTC] 2021-12-03 07:26:48.632  INFO 1 --- [       Thread-3] o.s.j.e.a.AnnotationMBeanExporter        : Unregistering JMX-exposed beans on shutdown\n
# 
```

参考链接(已提issue ,等待回复): https://github.com/fission/fission/issues/1204  
参考链接(官网-java): https://fission.io/docs/usage/languages/java/#accessing-http-requests  
参考链接(官方GitHub): https://github.com/fission/fission  