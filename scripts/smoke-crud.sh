#!/usr/bin/env bash
set -euo pipefail

api_base_url="${API_BASE_URL:-http://localhost:8080/api}"

curl -sS -f "$api_base_url/products" >/tmp/products-list.json

created=$(curl -sS -f -X POST "$api_base_url/products" \
  -H 'Content-Type: application/json' \
  -d '{"name":"Smoke Product","description":"Runtime endpoint check","price":12.5,"moodleSyncEnabled":false}')

id=$(printf '%s' "$created" | sed -n 's/.*"id":"\([^"]*\)".*/\1/p')
if [ -z "$id" ]; then
  echo "Unable to extract product id from response: $created" >&2
  exit 1
fi

curl -sS -f "$api_base_url/products/$id" >/tmp/product-get.json
curl -sS -f -X PUT "$api_base_url/products/$id" \
  -H 'Content-Type: application/json' \
  -d '{"name":"Smoke Product Updated","description":"Runtime endpoint check","price":15.0,"moodleSyncEnabled":false}' >/tmp/product-put.json
curl -sS -f -X PATCH "$api_base_url/products/$id/activate" >/tmp/product-activate.json
curl -sS -f -X PATCH "$api_base_url/products/$id/deactivate" >/tmp/product-deactivate.json
curl -sS -f -X DELETE "$api_base_url/products/$id" >/tmp/product-delete.txt

echo "CRUD smoke OK for $id"