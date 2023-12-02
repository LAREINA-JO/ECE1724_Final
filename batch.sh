#!/bin/bash

num_req=$1
echo $num_req

gsutil ls gs://dataproc-staging-us-central1-1070337408838-7e5gn3px/ |\
  grep  output |\
  xargs -i{} gsutil rm -r {}


for ((i=1; i<=$num_req; i++)); do
    gcloud dataproc jobs submit hadoop --cluster=word-count --jar=gs://dataproc-staging-us-central1-1070337408838-7e5gn3px/wc.jar -- gs://dataproc-staging-us-central1-1070337408838-7e5gn3px/data gs://dataproc-staging-us-central1-1070337408838-7e5gn3px/output$i &
done

wait