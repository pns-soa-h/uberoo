#!/bin/sh

# $1 : request address
send_get_request(){
    echo "Sending GET request to " $1
    curl -s $1
}

# $1 : request address
send_put_request(){
    echo "Sending PUT request to " $1
    curl -s -X PUT $1
}

# $1 : request address
# $2 : request body
send_post_request(){
    echo "Sending POST request to " $1
    echo "JSON body :" $2
    curl -s -d $2 -H "Content-Type: application/json" -X POST $1
}

echo "Listing meals :"
send_get_request http://localhost:8080/meals
printf "\n\n"

echo "Filtering meals by \"asian\" tag :"
send_get_request http://localhost:8080/meals?tag=asian > asian_meals.json
cat asian_meals.json
printf "\n\n"

echo "Sending request to order the ramen :"
user_id=1
restaurant_id=$(cat asian_meals.json | jq -r '._embedded.meals[] | select(.label == "Ramen") | .restaurant.id')
meal_address=$(cat asian_meals.json | jq -r '._embedded.meals[] | select(.label == "Ramen") | ._links.self.href' | tr -d '"')
meal_id=${meal_address##*/}
send_post_request http://localhost:8181/orders '{"clientId":"'$user_id'", "mealId":"'$meal_id'", "restaurantId":"'$restaurant_id'"}' > order_create_response.json
cat order_create_response.json
printf "\n\n"

echo "Confirming the order :"
send_put_request $(cat order_create_response.json | jq '._links.complete.href' | tr -d '"') > order_confirm_response.json
cat order_confirm_response.json
printf "\n\n"

echo "Display created orders for the restaurant :"
send_get_request "http://localhost:8181/orders?status=COMPLETED&restaurant=$restaurant_id"
printf "\n\n"

echo "Assigning the order :"
coursier_id=42
curl -s -d '{"coursierId":"'$coursier_id'"}' -H "Content-Type: application/json" -X PUT $(cat order_confirm_response.json | jq '._links.assign.href' | tr -d '"')
