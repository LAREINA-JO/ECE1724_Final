# Hadoop Standalone
## Build standalone image

```bash
docker build -t wxwmatt/hadoop-standalone:2.1.1-hadoop3.3.1-java8 . 
```

## Run WordCount Job
Every time when a job is being committed, a new input and a new output directory will be created.
The input and output directory name can be any valid names.
>Note
The output directory name must be different from the existing directories.

```bash
rm -rf data/input res/output
mkdir data/input
cp README.md ./data/input
./hadoop jar /jars/WordCount.jar WordCount /data/input /res/output
cat res/output/*
```

## Directory
As Docker is used, the mappings between directories on host computer and directories in Docker container are:
```
./jars => /jars
./res  => /res
./data => /data
```
That's why in the command `./hadoop`, `/data/input` and `/data/output` were used, which were corresponding to the directories `./data/input` and `./data/output` on the host computer.

### Jar files saved to directory `jars`

For example, `WordCount.jar` will go to this directory.

### Input files saved to directory `data`

For example, `input` directory and `README.txt` will go to this directory.

### Output files saved to directory  `res`

For example, `output` directory and `` will go to this directory.