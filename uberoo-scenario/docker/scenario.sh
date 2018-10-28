#!/bin/sh

echo "Listing meals :"
curl -s http://localhost:8080/meals
printf "\n\n"

echo "Filtering meals by \"asian\" tag :"
curl -s http://localhost:8080/meals?tag=asian > asian_meals.json
cat asian_meals.json
printf "\n\n"

echo "Sending request to order the ramen :"
user_id=1
restaurant_id=$(cat asian_meals.json | jq -r '._embedded.meals[] | select(.label == "Ramen") | .restaurant.id')
meal_address=$(cat asian_meals.json | jq -r '._embedded.meals[] | select(.label == "Ramen") | ._links.self.href' | tr -d '"')
meal_id=${meal_address##*/}
curl -s -d '{"clientId":"'$user_id'", "mealId":"'$meal_id'", "restaurantId":"'$restaurant_id'"}' -H "Content-Type: application/json" -X POST http://localhost:8181/orders > order_create_response.json
cat order_create_response.json
printf "\n\n"

echo "Confirming the order :"
curl -s -X PUT $(cat order_create_response.json | jq '._links.complete.href' | tr -d '"') > order_confirm_response.json
cat order_confirm_response.json
printf "\n\n"

echo "Display created orders for the restaurant :"
curl -s "http://localhost:8181/orders?status=COMPLETED&restaurant=$restaurant_id"
printf "\n\n"

echo "Assigning the order :"
coursier_id=42
curl -s -d '{"coursierId":"'$coursier_id'"}' -H "Content-Type: application/json" -X PUT $(cat order_confirm_response.json | jq '._links.assign.href' | tr -d '"')