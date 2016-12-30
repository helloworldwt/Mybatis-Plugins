## 简介
    使用Mybatis interceptor实现slow sql log 

## 使用方法
1. 引入SlowSqlItcpt到项目中,并在Mybatis配置中配置plugin,如spring-mybatis.xml中所示
2. 配置executeTimeThreshold阀值,超过阀值的sql会被记录

## 原理
1. http://blog.csdn.net/helloworldwt/article/details/51819015 (博客待续)
