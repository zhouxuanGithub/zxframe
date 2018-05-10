# zxframe JAVA框架
*.支持JPA：继承hibernate的便捷操作和mybatis的sql自由编写；支持乐观锁<br/>
*.支持缓存：本地一级缓存，本地全局二级缓存，远程缓存，查询缓存；融入事务，可回滚<br/>
*.支持DB多数据源：分库分表，读写分离，多读数据源熔断<br/>
*.支持高可用定时器<br/>
*.支持通用分布式锁<br/>
*.支持全服务热更新配置<br/>
-->SpringCloudConfigServer高可用成本较高，运维成本提升<br/>
-->本功能只为降低成本，可选用<br/>

*.封装本地和远程任意缓存存储,使用极其简单,支持对缓存组删除<br/>
-->zxframe.cache.local.LocalCacheManager<br/>
-->zxframe.cache.redis.RedisCacheManager<br/>

*.内置工具：<br/>
-->数据库定时备份<br/>
-->防缓存击穿<br/>

即将支持：<br/>
*.支持通用分布式事务<br/>