#!/bin/bash

# Original NameNode setup
namedir=`echo $HDFS_CONF_dfs_namenode_name_dir | perl -pe 's#file://##'`
if [ ! -d $namedir ]; then
  echo "Namenode name directory not found: $namedir"
  exit 2
fi

if [ -z "$CLUSTER_NAME" ]; then
  echo "Cluster name not specified"
  exit 2
fi

echo "remove lost+found from $namedir"
rm -r $namedir/lost+found

if [ "`ls -A $namedir`" == "" ]; then
  echo "Formatting namenode name directory: $namedir"
  $HADOOP_HOME/bin/hdfs --config $HADOOP_CONF_DIR namenode -format $CLUSTER_NAME
fi

# Start NameNode
$HADOOP_HOME/bin/hdfs --config $HADOOP_CONF_DIR namenode &

# Wait for the NameNode to exit safe mode
echo "Waiting for namenode to exit safe mode..."
until hdfs dfsadmin -safemode get | grep "Safe mode is OFF"; do
  sleep 10
done


# Setup the input and output directories on HDFS
echo "Creating HDFS directories..."
hadoop fs -mkdir -p /outputs
hadoop fs -mkdir -p /inputs

# Put data into the input directory on HDFS
echo "Putting data into HDFS..."
hadoop fs -put data /inputs


# Remove the previous output directory, if it exists
echo "Cleaning up the output directory..."
hdfs dfs -rm -r /outputs/result

# Run the MapReduce job
echo "Running MapReduce job..."
hadoop jar /opt/hadoop-3.3.1/share/hadoop/tools/lib/hadoop-streaming-3.3.1.jar \
    -D map.output.key.field.separator=- \
    -D mapred.text.key.partitioner.options=-k1,1 \
    -D mapred.reduce.tasks=5 \
    -file $PWD/jars/mapper.py \
    -file $PWD/jars/reducer.py \
    -mapper mapper.py \
    -reducer reducer.py \
    -input /inputs/data \
    -output /outputs/result \
    -partitioner org.apache.hadoop.mapred.lib.KeyFieldBasedPartitioner

echo "MapReduce job completed."
hdfs dfs -ls /outputs/result

echo "Copy the output to the local filesystem..."
rm -r /outputs/res_out_of_hdfs
mkdir -p /outputs/res_out_of_hdfs
hdfs dfs -copyToLocal /outputs/result/* /outputs/res_out_of_hdfs



