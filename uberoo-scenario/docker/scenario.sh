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
curl -s -d '{"clientId": "'${user_id}'", "meals": [{"id": "'${meal_id}'"}], "restaurant": {"id": "'${restaurant_id}'", "name": "Absolute Ramen", "address": {"address_1": "3327 Gnatty Creek Road", "city": "Westbury", "postcode": "11590", "country": "US"}}, "shippingAddress": {"firstName": "John", "lastName": "Doe", "address_1": "1035 Golden Ridge Road", "address_2": "null", "city": "Schenectady", "postcode": "12305", "country": "US", "email": "jdoe@gmail.com", "phone": "518-393-5066"}, "billingAddress": {"firstName": "John", "lastName": "Doe", "address_1": "1035 Golden Ridge Road", "address_2": "null", "city": "Schenectady", "postcode": "12305", "country": "US"}}' -H "Content-Type: application/json" -X POST http://localhost:8181/orders > order_create_response.json
cat order_create_response.json
printf "\n\n"

echo "Confirming the order :"
curl -s -d {"status": "ACCEPTED"} -X PATCH $(cat order_create_response.json | jq '._links.complete.href' | tr -d '"') > order_confirm_response.json
cat order_confirm_response.json
printf "\n\n"

echo "Display created orders for the restaurant :"
curl -s "http://localhost:8181/orders?status=ACCEPTED&restaurant=$restaurant_id"
printf "\n\n"

echo "Assigning the order :"
coursier_id=42
curl -s -d '{"coursierId":"'$coursier_id'"}' -H "Content-Type: application/json" -X PUT $(cat order_confirm_response.json | jq '._links.assign.href' | tr -d '"')
