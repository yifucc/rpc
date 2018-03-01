# rpc
an RPC framework implemented by myself
自己实现的一个rpc服务框架


使用说明：
spring配置文件：
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:rpc="http://southeast.edu/schema/rpc"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://southeast.edu/schema/rpc
       http://southeast.edu/schema/rpc/rpc.xsd
       http://www.springframework.org/schema/context
       http://www.springframework.org/schema/context/spring-context.xsd">
文件头部加入rpc的namespace；
服务端：
服务端标签：
    <rpc:protocol port="8888"/>
    <rpc:registry address="ifcc:2181,coco:2181,cc:2181" protocol="zookeeper"/>
    <rpc:service interface="edu.southeast.rpctest.dao.HelloService" ref="helloServiceImpl"/>
其中，protocol标签属性分析：port指定服务端绑定的端口
registry标签属性分析： protocol指定使用的注册中心协议，目前只支持zookeeper
address指定zookeeper的地址
service标签属性分析：interface指定接口的完整类名，ref指定实现类的id，且该实现类必须放入spring容器中
service标签也可以用注解代替@RPCService，value值为接口的class对象
客户端：
客户端标签
<rpc:discovery address="ifcc:2181,coco:2181,cc:2181" protocol="zookeeper" />
	<rpc:reference id="service" interface="edu.southeast.rpctest.dao.HelloService"/>
其中，disovery标签属性分析：protocol目前只支持zookeeper，address指定zookeeper的地址
reference标签属性分析：interface指定接口的完整类名。
