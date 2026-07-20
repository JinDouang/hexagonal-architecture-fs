#!/usr/bin/env bash
set -euo pipefail

container_name="hex-poc-api-open"
network_name="hexagonal-architecture-fs_default"
image_name="hexagonal-architecture-fs-api"
api_base_url="http://localhost:18080/api"

cleanup() {
  docker rm -f "$container_name" >/dev/null 2>&1 || true
}
trap cleanup EXIT

cleanup

docker run -d --name "$container_name" --network "$network_name" -p 18080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/training_catalogue \
  -e SPRING_DATASOURCE_USERNAME=training \
  -e SPRING_DATASOURCE_PASSWORD=training \
  -e APP_SECURITY_ENABLED=false \
  "$image_name" >/dev/null

ready=false
for _ in $(seq 1 30); do
  if curl -sS -f "$api_base_url/products" >/dev/null 2>&1; then
    ready=true
    break
  fi
  sleep 1
done

if [ "$ready" != true ]; then
  docker logs --tail=120 "$container_name"
  exit 1
fi

API_BASE_URL="$api_base_url" ./scripts/smoke-crud.sh