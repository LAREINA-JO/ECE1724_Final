# ECE1724_Final
 

Hadoop:
```console
cd docker-hadoop
docker-compose up --build -d

```
Run Hadoop in Docker:
```console
docker exec -it namenode bash
```

Run MapReduce program:
```console
hadoop fs -mkdir -p /outputs
hadoop fs -mkdir -p /inputs
hadoop fs -put data /inputs

hdfs dfs -rm -r /outputs/result

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

hdfs dfs -ls /outputs/result
hdfs dfs -head /outputs/result/part-00000
hdfs dfs -head /outputs/result/part-00001
hdfs dfs -head /outputs/result/part-00002
hdfs dfs -head /outputs/result/part-00003
hdfs dfs -head /outputs/result/part-00004


rm -r /outputs/res_out_of_hdfs
mkdir -p /outputs/res_out_of_hdfs
hdfs dfs -copyToLocal /outputs/result/* /outputs/res_out_of_hdfs

ls /outputs/res_out_of_hdfs
```

