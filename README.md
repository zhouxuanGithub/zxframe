# zxframe JAVA框架-让你的应用有能力且方便的解决巨大访问量瓶颈
*.支持读写分离，分库分表，读数据源熔断，数据源运行监控<br/>
*.支持JPA，既有hibernate的便捷操作，又有mybatis的sql集中编写or管理的风格；支持乐观锁<br/>
*.支持防缓存穿透，防缓存击穿，防缓存雪崩<br/>
*.支持多层次缓存：本地一级缓存，本地全局二级缓存，远程缓存，查询缓存，方法级别缓存<br/>
*.支持缓存融入事务，业务异常不提交缓存数据<br/>
*.支持通用分布式锁<br/>
*.支持全服务热更新配置<br/>
*.支持服务运行状态和日志查询，便于快速定位问题<br/>
-->SpringCloudConfigServer高可用成本较高，运维成本提升<br/>
-->本功能只为降低成本，可选用<br/>
*.封装本地和远程任意缓存存储,使用极其简单,支持对缓存组删除<br/>
-->zxframe.cache.mgr.CacheManager<br/>
*.建议代理层使用nginx+keepalived，或其他类似功能代理。反向代理，动静分离，请求结果缓存，静的资源也可以放CDN
*.建议使用SpringBoot构建，也可使用SpringMVC构建
-->SpringBoot：启动类加上注解即可，@ZxFrameComponentScan
-->SpringMVC：加上包扫描即可，<context:component-scan base-package="zxframe"/> 

# Demo运行步骤
*.导入项目，Maven构建，源码相对路径：/demo/webproject
*.file目录下的两个SQL脚本执行，创建必要的测试库
*.修改zxframe.xml里的数据源配置，如password，和数据库地址[127.0.0.1:3300]
*.如需要测试远程缓存则可在zxframe.xml里配置redis集群地址，如不需要则可直接删除
*.执行zxframe.demo.Application类，启动服务即可
*.浏览器访问：http://127.0.0.1:8888

# Demo内容
*.springboot介绍，yml的使用，tomcat配置优化
*.增删改查，读写分离，分库分表
*.通用缓存，方法级别缓存，数据级缓存（内部自动解决缓存穿透，击穿，雪崩问题）
*.配置热更新
*.分布式锁，乐观锁
*.数据源监控，运行状态查询，错误日志查询