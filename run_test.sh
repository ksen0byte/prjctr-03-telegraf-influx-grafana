#!/bin/bash

# Usage function to display help
usage() {
  echo "Usage: $0 [-n requests] [-c concurrency] [-s service_name] [-e entries] [-u url]"
  exit 1
}

# Default values for parameters
requests=10000
concurrency=200
service_name="tig-nginx-1"
entries=1000
base_url="http://nginx/reviews"

# Parse command line arguments
while getopts ":n:c:s:e:u:h" opt; do
  case ${opt} in
    n )
      requests=$OPTARG
      ;;
    c )
      concurrency=$OPTARG
      ;;
    s )
      service_name=$OPTARG
      ;;
    e )
      entries=$OPTARG
      ;;
    u )
      base_url=$OPTARG
      ;;
    h )
      usage
      ;;
    \? )
      echo "Invalid Option: -$OPTARG" 1>&2
      usage
      ;;
    : )
      echo "Invalid Option: -$OPTARG requires an argument" 1>&2
      usage
      ;;
  esac
done
shift $((OPTIND -1))

# Construct the full URL including the number of entries
url="${base_url}/${entries}"

# Run the Docker command with the specified arguments
#docker run --rm --network=container:tig-nginx-1 jordi/ab -n 10000 -c 200 http://nginx/reviews/1000

docker run --rm --network=container:${service_name} jordi/ab -n ${requests} -c ${concurrency} ${url}

