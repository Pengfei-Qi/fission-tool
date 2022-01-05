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
`正常提交`:  
```shell
git add .

git commit -m "添加GitHub 代码提交方式"

git push 
```
`异常提交` 出现以下问题:
```shell
unable to access 'https://github.com/YearningLife/fission-tool.git/': Failed to connect to github.com port 443: Timed out

git clone error: RPC failed; curl 56 OpenSSL SSL_read: SSL_ERROR_SYSCALL, errno 10054

unable to access github: OpenSSL SSL_read: Connection was reset, errno 10054
```


参考连接: https://stackoverflow.com/questions/46232906/git-clone-error-rpc-failed-curl-56-openssl-ssl-read-ssl-error-syscall-errno  
参考连接: https://juejin.cn/post/6844904193170341896  
参考链接: https://stackoverflow.com/questions/55768926/logon-failed-use-ctrlc-to-cancel-basic-credential-prompt-to-git-in-azure-devop
 ```shell
git config --global http.sslVerify "false"

git config --global http.postBuffer 524288000

git push 
```
可执行这个按钮,之后重试  
[![oatPEt.jpg](https://z3.ax1x.com/2021/12/03/oatPEt.jpg)](https://imgtu.com/i/oatPEt).  

2022-01-05 更新

```shell
# 如果弹出以下提示, 则依次执行命令

# Logon failed, use ctrl+c to cancel basic credential prompt to Git in Azure DevOps

# windows:
  git update-git-for-windows
  
  git push

# Linux/Unix: 
  git update
  
  git push origin branch_name

```


# 三. 笔记

## 1. 新创建

```bash
# 创建environment
fission env create --name sql-jvm --image fission/jvm-env --version 2 --keeparchive=true --builder fission/jvm-builder
# 在env 基础上创建function 命令简称fn
fission fn create --name  mysql-02 --deploy target/sql-access-fission-1.0-SNAPSHOT-jar-with-dependencies.jar --env sql-jvm --entrypoint io.fission.MysqlAccess
# 查看fn列表
fission fn list
# 创建url请求路径,route创建时不能创建变量, httptrigger可以指定变量,例如下面的 {name}
# fission route create --function sql-access-fission --url /sql-access-fission --method GET
fission httptrigger create --method GET --url "/mysq02/{name}" --function mysql-02 
# 查看http请求列表
fission httptrigger list
# 测试fn数据
fission fn test --name mysql-02
# 查看地址参数
kubectl -n fission get svc
# 拼接地址参数,并且测试
curl http://10.233.51.50/mysq02/oracle?company=zhangsan&address=shanghai
```
## 2. 更新参数

```shell
# 更新package
fission fn update --name mysql-02 --deploy target/sql-access-fission-1.0-SNAPSHOT-jar-with-dependencies.jar
# 测试fn数据
fission fn test --name mysql-02
# 查看地址参数
kubectl -n fission get svc
# 拼接地址参数,并且测试
curl http://10.233.51.50/mysq02/oracle?company=zhangsan&address=shanghai
```

## 3. 删除参数
`删除 httptrigger(route)`
```shell
# 获取列表,请求参数列表
fission httptrigger list
# NAME                                 METHOD URL                           FUNCTION(s)        INGRESS HOST PATH                          TLS ANNOTATIONS
# 621092bc-baf7-4ab2-aace-2621ab64c24c GET    /sql-access-fission-01        sql-access-fission false   *    /sql-access-fission-01            
# 97137735-1f86-4127-9098-5d962036c3ae GET    /mysql01                      mysql-01           false   *    /mysql01                          
# 978f3fbc-0cf0-422e-84da-616cc5f060b6 GET    /sql-access-fission/{tblName} sql-access-fission true    *    /sql-access-fission/{tblName}     
# e998884f-36b6-4dbd-83c1-ea1abaf40b9a GET    /mysq02/{name}                mysql-02           false   *    /mysq02/{name} 
fission httptrigger delete --name 978f3fbc-0cf0-422e-84da-616cc5f060b6
# trigger '978f3fbc-0cf0-422e-84da-616cc5f060b6' deleted
fission httptrigger list
# NAME                                 METHOD URL                    FUNCTION(s)        INGRESS HOST PATH                   TLS ANNOTATIONS
# 621092bc-baf7-4ab2-aace-2621ab64c24c GET    /sql-access-fission-01 sql-access-fission false   *    /sql-access-fission-01     
# 97137735-1f86-4127-9098-5d962036c3ae GET    /mysql01               mysql-01           false   *    /mysql01                   
# e998884f-36b6-4dbd-83c1-ea1abaf40b9a GET    /mysq02/{name}         mysql-02           false   *    /mysq02/{name}
```
`删除function`
```shell
fission fn list
# NAME               ENV     EXECUTORTYPE MINSCALE MAXSCALE MINCPU MAXCPU MINMEMORY MAXMEMORY TARGETCPU SECRETS CONFIGMAPS
# mysql-01           sql-jvm poolmgr      0        0        0      0      0         0         0                 
# mysql-02           sql-jvm poolmgr      0        0        0      0      0         0         0                 
# sql-access-fission java    poolmgr      0        0        0      0      0         0         0                 
fission fn delete --name sql-access-fission
# function 'sql-access-fission' deleted
```
## 4.  K8S部署
> 参考连接: https://fission.io/docs/usage/spec/#custom-resources-references
>
> 故障排查: https://fission.io/docs/trouble-shooting/setup/fission/
>
> spec文件参考:  https://github.com/fission/examples/tree/master/spec-example

# 四. 遗留问题

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