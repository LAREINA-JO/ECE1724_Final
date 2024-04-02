# ECE1724_Final
 

Hadoop:
```console
cd docker-hadoop
```
```console
docker-compose up --build -d
```


Run Hadoop in Docker:
```console
docker exec -it namenode bash
```

Run MapReduce program:
```console
hadoop fs -mkdir -p /outputs
```
```console
hadoop fs -mkdir -p /inputs
```
```console
hadoop fs -put data /inputs
```

```console
hdfs dfs -rm -r /outputs/result
```

```console
hadoop jar /opt/hadoop-3.3.1/share/hadoop/tools/lib/hadoop-streaming-3.3.1.jar \
    -D map.output.key.field.separator=- \
    -D mapred.text.key.partitioner.options=-k1,1 \
    -D mapred.reduce.tasks=5 \
    -file $PWD/jars/mapper.py\
    -file $PWD/jars/reducer.py\
    -mapper mapper.py \
    -reducer reducer.py \
    -input /inputs/data \
    -output /outputs/result \
    -partitioner org.apache.hadoop.mapred.lib.KeyFieldBasedPartitioner
```

```console
hdfs dfs -ls /outputs/result
```
```console
hdfs dfs -head /outputs/result/part-00000
```
```console
hdfs dfs -head /outputs/result/part-00001
```
```console
hdfs dfs -head /outputs/result/part-00002
```
```console
hdfs dfs -head /outputs/result/part-00003
```
```console
hdfs dfs -head /outputs/result/part-00004
```
```console


rm -r /outputs/res_out_of_hdfs
```
```console
mkdir -p /outputs/res_out_of_hdfs
```
```console
hdfs dfs -copyToLocal /outputs/result/* /outputs/res_out_of_hdfs
```
```console

ls /outputs/res_out_of_hdfs
```

