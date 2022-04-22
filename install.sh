#!/bin/bash

docker pull clickhouse/clickhouse-server:22.3.3.44
docker run -d --ulimit nofile=262144:262144 -p 8123:8123 -p 9000:9000 -p 9009:9009 clickhouse/clickhouse-server:22.3.3.44
