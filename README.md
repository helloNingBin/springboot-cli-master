<h1 align="center" style="margin: 30px 0 30px; font-weight: bold;">Springboot-cli</h1>
<h4 align="center">基于SpringBoot的轻量级快速开发脚手架集合</h4>
<p align="center">
	<a href="#"><img src="https://img.shields.io/badge/Springboot-2.5.3-blue"></a>
	<a href="#"><img src="https://img.shields.io/badge/license%20-MIT-green"></a>
	<a href="https://gitee.com/liangqiding/springboot-cli"><img src="https://img.shields.io/badge/%E7%A0%81%E4%BA%91-%E5%9B%BD%E5%86%85%E5%9C%B0%E5%9D%80-yellow"></a>
</p>

## 介绍

Springboot、SpringCloud各种常用框架使用案例，完善的文档，致力于让开发者快速搭建基础环境并让应用跑起来，并提供丰富的使用示例供使用者参考，快速上手。

- 代码优雅整洁
- 遵守阿里编程规范，高质量、高效率
- 统一的注释方法和风格
- 项目持续增加、持续更新维护
- [国内访问地址](https://gitee.com/liangqiding/springboot-cli)

## tcp项目

`基于Springboot、netty的tcp项目模板`

| #                            | 目录                             | 说明                    | 状态 | 更新时间  | 文档                                   |
| -------------------------------------------------- | ----------------------- | ---- | --------- | -------------------------------------- | -------------------------------------- |
| 1            | [netty-tcp-client](./netty-tcp-client)             | tcp 客户端              | 完成 | 2022.4.17 | [项目讲解](http://t.csdn.cn/MXQiP) |
| 2            | [netty-tcp-server](./netty-tcp-server)             | tcp 服务器              | 完成 | 2022.4.17 | [项目讲解](http://t.csdn.cn/aLiIV) |
| 3  | [netty-protobuf-client](./netty-protobuf-client)   | tcp protobuf 测试客户端 | 完成 | 2022.4.18 | [项目讲解](http://t.csdn.cn/MeE4v) |
| 4  | [netty-protobuf-server](./netty-protobuf-server)   | tcp protobuf 服务器     | 完成 | 2022.4.18 | [项目讲解](http://t.csdn.cn/yme8h) |
| 5 | [netty-websocket-client](./netty-websocket-client) | webcocket客户端         | 完成 | 2022.4.20 | [项目讲解](http://t.csdn.cn/0uko7) |
| 6 | [netty-websocket-server](./netty-websocket-server) | websocket服务器         | 完成 | 2022.4.20 | [项目讲解](http://t.csdn.cn/IXnFu) |
| 7 | [mind-mqtt](https://github.com/liangqiding/mind-mqtt) | mqtt broker 及 client | 完成 | 2022.4.10 | [项目讲解](https://github.com/liangqiding/mind-mqtt) |

## Springboot常用项目

- `Springboot常用框架基础模板`

| #    | 目录                                                         | 说明                                                 | 状态 | 更新时间  | 文档                               |
| ---- | ------------------------------------------------------------ | ---------------------------------------------------- | ---- | --------- | ---------------------------------- |
| 1    | [springboot-web](./springboot-web)                           | springboot-基础web项目                               | 完成 | 2022.4.21 | [项目讲解](https://blog.csdn.net/qq_42411805) |
| 2    | [springboot-swagger](./springboot-swagger)                   | springboot-整合swagger3                              | 完成 | 2022.4.21 | [项目讲解](https://blog.csdn.net/qq_42411805) |
| 5    | [springboot-thymeleaf](./springboot-thymeleaf)               | 整合thymeleaf，及使用案例                            | 完成 | 2022.4.25 | [项目讲解](https://blog.csdn.net/qq_42411805) |
| 3    | [springboot-druid](./springboot-druid)                       | 整合druid连接池，开启sql监控，慢SQL检测              | 完成 | 2022.4.28 | [项目讲解](https://blog.csdn.net/qq_42411805) |
| 4    | [springboot-mongodb](./springboot-mongodb)                   | 整合mongodb，及使用案例                              | 完成 | 2022.4.23 | [项目讲解](https://blog.csdn.net/qq_42411805) |
| 6    | [springboot-redis](./springboot-redis)                       | springboot-整合redis                                 | 完成 | 2022.4.21 | [项目讲解](https://blog.csdn.net/qq_42411805) |
| 7    | [springboot-redisson](./springboot-redisson)                 | redisson分布式缓存、分布式锁                         | 完成 | 2022.5.28 | [项目讲解](https://blog.csdn.net/qq_42411805) |
| 8    | [springboot-mybatis-plus](./springboot-mybatis-plus)         | mybatis-plus使用示例                                 | 完成 | 2022.4.12 | [项目讲解](https://blog.csdn.net/qq_42411805) |
| 9    | [springboot-mybatis-plus-generator](./springboot-mybatis-plus-generator) | mybatis-plus代码生成器                               | 完成 | 2022.4.12 | [项目讲解](https://blog.csdn.net/qq_42411805) |
| 10   | [springboot-upload](./springboot-upload)                     | 文件上传下载                                         | 完成 | 2022.4.12 | [项目讲解](https://blog.csdn.net/qq_42411805) |
| 11   | [springboot-minio](./springboot-minio)                       | minio文件服务器                                      | 完成 | 2022.5.2  | [项目讲解](https://blog.csdn.net/qq_42411805) |
| 12   | [springboot-kafka](./springboot-kafka)                       | 消息队列kafka提供者及消费者                          | 完成 | 2022.4.24 | [项目讲解](https://blog.csdn.net/qq_42411805) |
| 13   | [springboot-rabbitMQ](./springboot-rabbitMQ)                 | 消息队列rabbitMQ提供者及消费者，各种类型队列使用案例 | 完成 | 2022.4.27 | [项目讲解](https://blog.csdn.net/qq_42411805) |
| 14   | [springboot-excel-export](./springboot-excel-export)         | Excel、word文档生成导出                              | 完成 | 2022.4.19 | [项目讲解](https://blog.csdn.net/qq_42411805) |
| 15   | [springboot-email](./springboot-email)                       | hutool版邮件发送案例                                 | 完成 | 2022.4.26 | [项目讲解](https://blog.csdn.net/qq_42411805) |
| 16   | [springboot-async](./springboot-async)                       | @Async开启异步多线程                                 | 完成 | 2022.5.27 | [项目讲解](https://blog.csdn.net/qq_42411805) |
| 17   | [springboot-scheduler](./springboot-scheduler)               | 定时任务、异步任务                                   | 完成 | 2022.4.24 | [项目讲解](https://blog.csdn.net/qq_42411805) |
| 18   | [springboot-exception](./springboot-exception)               | 统一响应和异常处理                                   | 完成 | 2022.4.26 | [项目讲解](https://blog.csdn.net/qq_42411805) |
| 19   | [springboot-aop-logger](./springboot-aop-logger)             | AOP日记，注解实现                                    | 完成 | 2022.4.27 | [项目讲解](https://blog.csdn.net/qq_42411805) |
| 20   | [springboot-elasticsearch](./springboot-elasticsearch)       | es索引引擎                                           | 完成 | 2022.5.1  | [项目讲解](https://blog.csdn.net/qq_42411805) |
| 21   | [springboot-validator](./springboot-validator)               | validator参数校验及捕捉                              | 完成 | 2022.5.21 | [项目讲解](https://blog.csdn.net/qq_42411805) |
| 22   | [springboot-captcha](./springboot-captcha)                   | hutool版Captcha图形验证码登录                        | 完成 | 2022.5.22 | [项目讲解](https://blog.csdn.net/qq_42411805) |

- `Security安全框架`

| #    | 目录                                                         | 说明                                                         | 状态 | 更新时间  | 文档                               |
| ---- | ------------------------------------------------------------ | ------------------------------------------------------------ | ---- | --------- | ---------------------------------- |
| 1    | [springboot-security](./springboot-security)                 | security安全框架-入门体验版                                  | 完成 | 2022.5.26 | [项目讲解](https://blog.csdn.net/qq_42411805) |
| 2    | [springboot-security-thymeleaf](./springboot-security-thymeleaf) | security自定义账号密码验证+thymeleaf登录案例（附带网页案例） | 完成 | 2022.5.27 | [项目讲解](https://blog.csdn.net/qq_42411805) |
| 3    | [springboot-security-captcha](./springboot-security-captcha) | security+验证码登录案例（附带网页案例）                      | 完成 | 2022.6.13 | [项目讲解](https://blog.csdn.net/qq_42411805) |
| 4    | [springboot-security-jwt](./springboot-security-jwt)         | security+jwt 实现无状态认证，前后端分离（附带网页案例）      | 完成 | 2022.6.8  | [项目讲解](https://blog.csdn.net/qq_42411805) |

- `shiro安全框架`

| #    | 目录                                           | 说明                                      | 状态 | 更新时间  | 文档                               |
| ---- | ---------------------------------------------- | ----------------------------------------- | ---- | --------- | ---------------------------------- |
| 1    | [springboot-jwt](./springboot-jwt)             | JWT实现token登录认证                      | 完成 | 2022.4.25 | [项目讲解](https://blog.csdn.net/qq_42411805) |
| 2    | [springboot-shiro](./springboot-shiro)         | shiro安全框架、及登录案例（附带网页案例） | 完成 | 2022.4.25 | [项目讲解](https://blog.csdn.net/qq_42411805) |
| 3    | [springboot-shiro-jwt](./springboot-shiro-jwt) | shiro+jwt 实现无状态认证，前后端分离      | 完成 | 2022.5.19 | [项目讲解](https://blog.csdn.net/qq_42411805) |

## SpringCloud常用项目

`Springcloud常用框架基础模板`

| #    | 目录                                         | 说明                                             | 状态 | 更新时间  | 文档                               |
| ---- | -------------------------------------------- | ------------------------------------------------ | ---- | --------- | ---------------------------------- |
| 1    | [springCloud-nacos](./springcloud-nacos)     | 升级2020.0.5版cloud，整合nacos+openfeign调用示例 | 完成 | 2022.5.8  | [项目讲解](https://blog.csdn.net/qq_42411805) |
| 2    | [springCloud-gateway](./springcloud-gateway) | 整合网关服务，实现服务统一拦截，统一转发         | 完成 | 2022.5.12 | [项目讲解](https://blog.csdn.net/qq_42411805) |
| 3    | [springCloud-hystrix](./springcloud-hystrix) | 服务容错保护（Hystrix服务降级）                  | 完成 | 2022.5.25 | [项目讲解](https://blog.csdn.net/qq_42411805) |

