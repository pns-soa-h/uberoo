#!/bin/sh

# $1 : request address
send_get_request(){
    # echo "Sending GET request to " $1
    curl $1
}

# $1 : request address
send_put_request(){
    # echo "Sending PUT request to " $1
    curl -X PUT $1
}

# $1 : request address
# $2 : request body
send_patch_request(){
    # echo "Sending PATCH request to " $1
    # echo "JSON body :" $2
    curl -d $2 -H "Content-Type: application/json" -X PATCH $1
}

# $1 : request address
# $2 : request body
send_post_request(){
    # echo "Sending POST request to " $1
    # echo "JSON body :" $2
    curl -s --data $2 -H "Content-Type: application/json;charset=UTF-8" POST $1
}

meals="http://localhost:8080/meals"
coupons="http://localhost:8080/coupons"
orders="http://localhost:8181/orders"
deliveries="http://localhost:8282/"
geolocation="http://localhost:8383/coursiers"
payments="http://localhost:8484/"

echo "Listing meals :"
curl $meals
printf "\n\n"

echo "Filtering meals by \"asian\" tag :"
send_get_request ${meals}?tag=asian > asian_meals.json
cat asian_meals.json
printf "\n\n"

user_id=1
# Restaurant id is the same for the other meals as ramen
restaurant_id=$(cat asian_meals.json | jq -r '._embedded.meals[] | select(.label == "Ramen") | .restaurant.id')
meal_address_entree=$(cat asian_meals.json | jq -r '._embedded.meals[] | select(.label == "Soupe miso") | ._links.self.href' | tr -d '"')
meal_address_plat=$(cat asian_meals.json | jq -r '._embedded.meals[] | select(.label == "Ramen") | ._links.self.href' | tr -d '"')
meal_address_dessert=$(cat asian_meals.json | jq -r '._embedded.meals[] | select(.label == "Glace coco") | ._links.self.href' | tr -d '"')
meal_entree_id=${meal_address_entree##*/}
meal_plat_id=${meal_address_plat##*/}
meal_dessert_id=${meal_address_dessert##*/}

# -------------
echo "Creating a promotional code '10%OFF' on full menu orders for the restaurant id: ${restaurant_id}"
body='{"code":"10OFF","restaurantId":"6","amount":10,"discountType":"MENU_PERCENT","description":"offer","date_expires":"2018-12-28"}'
send_post_request $coupons $body > coupons.json
cat coupons.json
printf "\n\n"

echo "Listing coupons :"
curl $coupons
printf "\n\n"

echo "Sending request to order the an entry-main course-dessert with a coupon :"
send_post_request $orders '{"clientId":'${user_id}',"meals":[{"id":'${meal_entree_id}'},{"id":'${meal_plat_id}'},{"id":'${meal_dessert_id}'}],"coupon":{"code":"10%MENU"},"restaurant":{"id":"'${restaurant_id}'","name":"AbsoluteRamen","address":{"address_1":"3327GnattyCreekRoad","city":"Westbury","postcode":"11590","country":"US"}},"shippingAddress":{"firstName":"John","lastName":"Doe","address_1":"1035GoldenRidgeRoad","address_2":"null","city":"Schenectady","postcode":"12305","country":"US","email":"jdoe@gmail.com","phone":"518-393-5066"},"billingAddress":{"firstName":"John","lastName":"Doe","address_1":"1035GoldenRidgeRoad","address_2":"null","city":"Schenectady","postcode":"12305","country":"US"}}' > order_create_response.json
cat order_create_response.json
printf "\n\n"

echo "Confirming the order :"
var=$(cat order_create_response.json | jq '._links.update.href' | tr -d '"')
send_patch_request $var '{"status":"ACCEPTED","payment_method":"cb"}' > order_confirm_response.json
cat order_confirm_response.json
printf "\n\n"

echo "Display created orders for the restaurant :"
curl ${orders}"?status=ACCEPTED&restaurant=${restaurant_id}" > created_orders.json
cat created_orders.json
printf "\n\n"

echo "Display pending orders :"
curl ${deliveries}"/orders"
printf "\n\n"

echo "Getting the first order :"
id=$(cat created_orders.json | jq '._embedded.orders[0].id' | tr -d '"')
curl ${deliveries}"/orders/${id}" > first_order.json
cat first_order.json
printf "\n\n"

echo "Display existing coursiers :"
curl ${deliveries}"/coursiers"
printf "\n\n"

echo "As Jamie, I take the previous order."
send_patch_request ${deliveries}"/orders/${id}" '{"id":"1","name":"Jamie"}'
printf "\n\n"

echo "The order becomes is assigned."
send_patch_request ${deliveries}"/orders/${id}/status" '{"status":"ASSIGNED"}'
printf "\n\n"

echo "We would like to know where the coursier is."
send_get_request ${geolocation}"/1"
printf "\n\n"

echo "As Jamie, I announce that the order has been delivered."
send_patch_request ${deliveries}"/orders/${id}/status" '{"status":"COMPLETED"}'
printf '\n\n'
