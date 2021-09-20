1.启动Redis及MySQL服务器，在application.properties配置文件中配置相关数据库信息

2.使用maven将源代码打成jar包

3.将jar包放在data文件夹同级目录，输出日志以及gc log在同一级目录，整体目录如下： 

```
.
├── data							   //数据目录
│   ├── collect
│   │   ├── 揽收时效数_1.csv
│   │   ├── 揽收时效数_2.csv
│   │   └── 揽收时效数_3.csv
│   ├── deliver
│   │   ├── 派送时效数据_1.csv
│   │   ├── 派送时效数据_2.csv
│   │   └── 派送时效数据_3.csv
│   └── transit
│       ├── 中转时效数据_1.csv
│       ├── 中转时效数据_2.csv
│       └── 中转时效数据_3.csv
├── gc.log						       //gc log
├── Logs							   //日志目录
│   └── service.log					   //日志
└── transportation-0.0.1-SNAPSHOT.jar  //程序
```

4.启动程序，命令如下： 

```
java -jar -Xms2g -Xmx2g -Xmn1g -Xloggc:gc.log -XX:MetaspaceSize=100m -XX:+PrintGCDetails transportation-0.0.1-SNAPSHOT.jar
```

5.当springboot启动成功后，调用接口，得到结果。

接口为：

```
POST请求 
/delivery_time 端口8080 
Body示例：
{
	"createTime": "2021-05-19 08:57:01",
	"srcProvinceId": 909,
	"srcCityId": 82056,
	"srcCountyId": 131820,
	"srcTownId": 59784501,
	"dstProvinceId": 18,
	"dstCityId": 92682,
	"dstCountyId": 1690146
}
```

