#!/bin/bash

# Define the logfile location within the local directory /namenode
LOGFILE="./logfile.log"

# Function to log messages with a timestamp
log_with_date() {
  echo "$(date '+%Y-%m-%d %H:%M:%S') - $1" | tee -a "$LOGFILE"
}

# Initialize the log file to make sure it exists
touch "$LOGFILE"
chmod +w "$LOGFILE"

# Redirect all output to the logfile
exec &> >(tee -a "$LOGFILE")

log_with_date "Starting run.sh script"

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
log_with_date "Waiting for namenode to exit safe mode..."
until hdfs dfsadmin -safemode get | grep "Safe mode is OFF"; do
  sleep 10
done


# Setup the input and output directories on HDFS
log_with_date "Creating HDFS directories..."
{ time hadoop fs -mkdir -p /outputs; } 2>&1
{ time hadoop fs -mkdir -p /inputs; } 2>&1
log_with_date "HDFS directories created."

# Put data into the input directory on HDFS
log_with_date "Putting data into HDFS..."
{ time hadoop fs -put data /inputs; } 2>&1
log_with_date "Data put into HDFS."


# Remove the previous output directory, if it exists
log_with_date "Cleaning up the output directory..."
{ time hadoop fs -rm -r -f /outputs/result; } 2>&1
log_with_date "Output directory cleaned up."

# Run the MapReduce job
log_with_date "Running MapReduce job..."
{ time hadoop jar /opt/hadoop-3.3.1/share/hadoop/tools/lib/hadoop-streaming-3.3.1.jar \
    -D map.output.key.field.separator=- \
    -D mapred.text.key.partitioner.options=-k1,1 \
    -D mapred.reduce.tasks=5 \
    -file $PWD/jars/mapper.py \
    -file $PWD/jars/reducer.py \
    -mapper mapper.py \
    -reducer reducer.py \
    -input /inputs/data \
    -output /outputs/result \
    -partitioner org.apache.hadoop.mapred.lib.KeyFieldBasedPartitioner; } 2>&1



log_with_date "MapReduce job completed."
{ time hadoop fs -ls /outputs/result;}
log_with_date "MapReduce job completed."

rm -r /outputs/res_out_of_hdfs
mkdir -p /outputs/res_out_of_hdfs

log_with_date "Copy the output to the local filesystem..."
{ time hadoop fs -copyToLocal /outputs/result/* /outputs/res_out_of_hdfs; } 2>&1
log_with_date "Output copied to the local filesystem."

log_with_date "Namenode run.sh script completed."



