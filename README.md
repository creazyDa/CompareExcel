# CompareExcel
比较两个Excel表格中的数据，自动将超过阀值的项标红.

./bin文件夹下已经有生成好的执行文件.

本工具适合行政人员日常工作中比对Excel表格的数据项差异.

两个表中的行各列的顺序可以不一样，可以不是一一对应.
#应用场景:
hyberbin是某公司薪酬岗人员,每次做工资条的时候都是心惊胆战,生怕给谁工资算掉了或者算错了.

他分析得出每月每个月的工资基本上差不太多,为何不能把这个月他的工资中每项与上月的进行比较呢?

每个工资项都设置一个阀值,与果该项与上月相差超过这个阀值就自动告警.


同样还可以应用于考勤,等表格数据的比较


#依赖说明

```xml

<dependencies>
        <dependency>
            <groupId>org.jplus</groupId>
            <artifactId>J-Excel</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
        <dependency>
            <groupId>org.jplus</groupId>
            <artifactId>J-hyberbin</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
        <dependency>
            <groupId>org.xerial</groupId>
            <artifactId>sqlite-jdbc</artifactId>
            <version>3.8.7</version>
        </dependency>
        <dependency>
            <groupId>org.beautyeye</groupId>
            <artifactId>beautyeye</artifactId>
            <version>1.0</version>
        </dependency>
</dependencies>

```
J-Excel 操作Excel的包,地址:https://github.com/hyberbin/J-Excel.git

J-hyberbin 操作sqlite数据库的包,用户上一次的一些设置会自动保存在本地sqlite数据中,地址:https://github.com/hyberbin/J-hyberbin.git

sqlite-jdbc sqlite数据库驱动

beautyeye 界面UI的包,可以到./bin目录下去找

#运行说明

本地必须要装一个JRE,版本1.6以上

windows系统运行./bin/run.bat
linux,mac运行./bin/sh.bat

![](https://raw.githubusercontent.com/hyberbin/CompareExcel/master/pic/1.png)

![](https://raw.githubusercontent.com/hyberbin/CompareExcel/master/pic/2.png)

![](https://raw.githubusercontent.com/hyberbin/CompareExcel/master/pic/3.png)

![](https://raw.githubusercontent.com/hyberbin/CompareExcel/master/pic/4.png)
