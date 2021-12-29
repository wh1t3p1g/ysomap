
# YSOMAP 
![Platforms](https://img.shields.io/badge/Platforms-OSX-green.svg)
![Java version](https://img.shields.io/badge/Java-8-blue.svg)
![License](https://img.shields.io/badge/License-apache%202-green.svg)

Ysomap is A helpful Java Deserialization exploit framework.

Ysomap是一款适配于各类实际复杂环境的Java反序列化利用框架，可动态配置具备不同执行效果的Java反序列化利用链payload。

随着利用链的补充，ysomap同样可作为一款Java反序列化利用链教学库。目前，ysomap支持Java原生反序列化利用链、fastjson利用链、hessian利用链、xmldecoder、xstream等等。

另外，ysomap具备exploit模块，用于充分调动反序列化利用链。目前，ysomap已支持RMI、JNDI、JMX、shiro、xmlrpc等exploits。

## #1 如何使用

在谈如何使用ysomap之前，假设使用者有一定的Java反序列化利用的前置知识，以及一些常见利用的原理，如rmi、ldap等。

### Jar编译

由于XStream的几个payload依赖JDK8的环境，所以后续的使用均在JDK8的环境下编译并运行

```bash
mvn clean package -DskipTests
```

正常编译不出错，可在`cli/target`目录找到ysomap.jar

当然，你也可以直接下载[release](https://github.com/wh1t3p1g/ysomap/releases)，但还是推荐自行clone后编译，因为大版本的更新将积攒一批利用链后才会发布release。

### Jar运行

经过几次迭代，目前ysomap支持两种运行模式：终端cli模式和脚本模式

终端模式
```bash
java -jar ysomap.jar cli
```
脚本模式
```bash
java -jar ysomap.jar script path/to/script.yso
```
终端模式更易于选择和配置exploit、payload、bullet，但对于重复性的配置，终端模式显的格外繁琐。所以后续又增加了脚本模式。通过编写特定配置的yso脚本，使用ysomap进行载入调用。脚本模式在正确配置的前提下将极大的节省使用者输入重复配置的工作量，提高使用效率。同时，yso脚本也可以被分享给其他使用者进行快捷使用。

### 基础使用方法

参见[YSOMAP食用指北](https://github.com/wh1t3p1g/ysomap/wiki/YSOMAP%E9%A3%9F%E7%94%A8%E6%8C%87%E5%8C%97)

## #2 当前进度

### DONE

- [x] 支持CommonsCollections系列payload
- [x] 支持执行效果bullet：远程jar载入、命令执行、代码执行、发起jndi效果、tomcat内存马、延时判断、文件写入、socket shell。
- [x] 支持现有RMI系列攻击包 [原理1](http://blog.0kami.cn/2020/02/06/rmi-registry-security-problem/) [原理2](http://blog.0kami.cn/2020/02/09/jndi-with-rmi/) [原理3](https://mogwailabs.de/blog/2020/02/an-trinhs-rmi-registry-bypass/)
- [x] 支持现有LDAP系列攻击包 [原理](http://blog.0kami.cn/2020/03/01/jndi-with-ldap/)
- [x] 支持HTTP服务动态挂载恶意的class文件或jar文件
- [x] 支持URLDNS
- [x] 支持现有JMX系列攻击包 [原理](http://blog.0kami.cn/2020/03/10/java-jmx-rmi/)
- [x] 支持fastjson JdbcRowSetImpl、TemplatesImpl gadget [原理](http://blog.0kami.cn/2020/04/13/talk-about-fastjson-deserialization/)
- [x] 支持现有XStream系列payload包 [原理](http://blog.0kami.cn/2020/04/18/talk-about-xstream-deserialization/)
- [x] 支持weblogic XMLDecoder payloads

### TODO

- [ ] 支持weblogic系列攻击包
- [ ] 支持websphere系列攻击包

## #3 由来

在实际分析ysoserial的利用链时，有时候会觉得框架写的太死，有以下几个缺点：

1. 同一个利用链如果想改变一下最后的利用效果，如命令执行改成代码执行，我们需要改写这个利用链或者是重新增加一个利用链。这时，我们其实可以看到利用链的前半部分是不变的，变的只是后续的利用效果。
2. ysoserial仅实现了常规的序列化利用链，对于类似JSON格式的序列化利用链，以当前的这个框架扩展起来会比较麻烦

所以萌生了开发一个更加灵活的框架来扩展反序列化利用链，也就是当前这个试验品ysomap。

PS：YSOMAP项目为另一个项目的子项目，后续将开源该项目，敬请期待......

## #4 原理

我将利用链切分成了两个部分**payload**和**bullet**：

1. payload：指代利用链的前序部分
2. bullet：指代最终利用链可达成的效果

#### 实际案例分析

CommonsCollection1和3，在分析时我们可以看到实际1和3的区别在于1使用的是`InvokerTransformer`，而3使用的是`templatesImpl`的方式。那么提取相同的前序payload部分，我们只需写两个不同的bullet即可。而且这两个bullet也同样能被用在其他的payload上。

实际还有就是我在写RMIRegistryExploit时，也有这种可将不变部分重用的地方，而无需2,3之类的出现。


## #5 免责申明

未经授权许可使用YSOMAP攻击目标是非法的。 本程序应仅用于授权的安全测试与研究目的。

## #6 致谢

@ysoserial: https://github.com/frohoff/ysoserial
@marshalsec: https://github.com/mbechler/marshalsec

## #7 404星链计划

<img src="https://github.com/knownsec/404StarLink-Project/raw/master/logo.png" width="30%">

ysomap现已加入 [404星链计划](https://github.com/knownsec/404StarLink)