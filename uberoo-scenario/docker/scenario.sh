#!/bin/sh

echo "Listing meals :"
curl -s http://localhost:8080/meals > meals.json
cat meals.json
printf "\n\n"

echo "Sending request to order the ramen :"
user_id=1
meal_address=$(cat meals.json | jq -r '._embedded.meals[] | select(.tag.label == "asian") | ._links.self.href' | tr -d '"')
meal_id=${meal_address##*/}
curl -s -d '{"clientId":"'$user_id'", "mealId":"'$meal_id'"}' -H "Content-Type: application/json" -X POST http://localhost:8181/orders > order_create_response.json
cat order_create_response.json
printf "\n\n"

echo "Confirming the order :"
curl -s -X PUT $(cat order_create_response.json | jq '._links.complete.href' | tr -d '"') > order_confirm_response.json
cat order_confirm_response.json
printf "\n\n"

echo "Display created orders :"
curl -s http://localhost:8181/orders > orders.json
cat orders.json
printf "\n\n"

echo "Assigning the order :"
coursier_id=42
curl -s -d '{"coursierId":"'$coursier_id'"}' -H "Content-Type: application/json" -X PUT $(cat order_confirm_response.json | jq '._links.assign.href' | tr -d '"') > order_assigned_response.json
cat order_assigned_response.json