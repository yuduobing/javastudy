FROM  java:8
#拷贝这个目录下的文件
COPY *.jar /app.jar
CMD ["--server.port=8080"]
EXPOSE 8080
#s启动后执行
ENTRYPOINT ["java","-jar","/app.jar"]
#启动命令 docker build -t  ydb666 .
# -p不指定随机分配 docker run -d -P --name ydb888 ydb666
