# weixin
weixin devlop
spring boot 搭建微信基础环境
1、使用微信用户的openid作为唯一标识
2、使用spring security 保存用户session信息(如果开启redis后默认会将用户session信息已hash的形式存入redis中,可解决集群环境下session共享问题)
3、使用mongodb保存从微信获取的文件、头像信息
4、通过nginx做负载均衡转发

#tomcat session共享策略
1、使用nginx的ip_hash策略，利用nginx的基于访问ip的hash路由策略，将某个ip固定在一台机器，用户每次请求都会转发到这台机器，可以解决session共享问题；缺点：出现宕机后，会丢失。
2、使用tomcat自带的cluster进行session复制，tomcat的session复制是基于IP组播(multicast)来完成的, 详细的IP组播介绍可以参考这里；简单的说就是需要进行集群的tomcat通过配置统一的组播IP和端口来确定一个集群组, 当一个node的session发生变更的时候, 它会向IP组播发送变更的数据, IP组播会将数据分发给所有组里的其他成员(node)。
3、使用memcached、redis等保存session。