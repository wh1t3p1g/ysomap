
# YSOMAP 
![Platforms](https://img.shields.io/badge/Platforms-OSX-green.svg)
![Java version](https://img.shields.io/badge/Java-8%2b-blue.svg)
![License](https://img.shields.io/badge/License-apache%202-green.svg)

Ysomap is A helpful Java Deserialization exploit framework based on ysoserial

## 0x00 闹着玩系列一：仍在开发中

### DONE

- [x] CommonsCollections系列，可达成远程jar载入、命令执行、代码执行、发起jndi效果
- [x] RMIRegistryExploit 包括最初的和绕过方式，[原理](http://blog.0kami.cn/2020/02/06/rmi-registry-security-problem/)
- [x] RMIListener 原JRMPListener
- [x] RMIRefListener 配合jndi使用，利用[原理](http://blog.0kami.cn/2020/02/09/jndi-with-rmi/)
- [x] SimpleHTTPServer 挂载恶意的class文件或jar文件，填写具体代码或需执行的命令即可
- [x] URLDNS
- [x] LDAPRefListener、LDAPLocalChainListener 前者使用reference的方式，后者使用目标环境下的反序列化利用链，[原理](http://blog.0kami.cn/2020/03/01/jndi-with-ldap/)
- [x] RMIConnectWithUnicastRemoteObject 增加JRMP反连Payload，这部分暂时没有在攻击RMI Registry的时候成功？虽然[原理](https://mogwailabs.de/blog/2020/02/an-trinhs-rmi-registry-bypass/)上为绕过方法之一,但在测试时并未成功，[原因](http://blog.0kami.cn/2020/02/06/rmi-registry-security-problem/)第0x07部分
- [x] JMXInvokeMBean 增加对JMX Server的攻击

## 0x01 起因

在实际分析ysoserial的利用链时，有时候会觉得框架写的太死，有以下几个缺点：

1. 同一个利用链如果想改变一下最后的利用效果，如命令执行改成代码执行，我们需要改写这个利用链或者是重新增加一个利用链。这时，我们其实可以看到利用链的前半部分是不变的，变的只是后续的利用效果。
2. ysoserial仅实现了常规的序列化利用链，对于类似JSON格式的序列化利用链，以当前的这个框架扩展起来会比较麻烦

所以萌生了开发一个更加灵活的框架来扩展反序列化利用链，也就是当前这个试验品ysomap。

## 0x02 原理

我将利用链切分成了两个部分**payload**和**bullet**：

1. payload：指代利用链的前序部分
2. bullet：指代最终利用链可达成的效果

#### 实际案例分析

CommonsCollection1和3，在分析时我们可以看到实际1和3的区别在于1使用的是`InvokerTransformer`，而3使用的是`templatesImpl`的方式。那么提取相同的前序payload部分，我们只需写两个不同的bullet即可。而且这两个bullet也同样能被用在其他的payload上。

实际还有就是我在写RMIRegistryExploit时，也有这种可将不变部分重用的地方，而无需2,3之类的出现。

## 0x03 使用方法

使用`mvn clean package -DskipTests`

生成`ysomap-0.0.1-SNAPSHOT-all.jar`

执行`java -jar ysomap-0.0.1-SNAPSHOT-all.jar`

payload默认生成obj.ser文件在当前目录，为序列化后的数据。

可使用的命令

#### 1.查看当前可用的exploits/payloads/bullets

`list [exploits|payloads|bullets]`

#### 2.使用指定的bullet/payload/expliot

`use [bullet|payload|exploit] name`

#### 3.查看当前的设置选项

`show options`

#### 4.设置当前的选项

`set key value`

#### 5.运行当前的payload/exploit

`run`

#### 6.查看当前运行着的exploits sessions

`sessions`

#### 7.停止某一session

删除所有`kill all`

删除指定`kill 0`

#### 8.其余的看help

## 0x04 感谢

@ysoserial：https://github.com/frohoff/ysoserial