#!/bin/sh

# $1 : request address
send_get_request(){
    # echo "Sending GET request to " $1
    curl -s $1
}

# $1 : request address
send_put_request(){
    # echo "Sending PUT request to " $1
    curl -s -X PUT $1
}

# $1 : request address
# $2 : request body
send_patch_request(){
    # echo "Sending PATCH request to " $1
    # echo "JSON body :" $2
    curl -s -d $2 -H "Content-Type: application/json" -X PATCH $1
}

# $1 : request address
# $2 : request body
send_post_request(){
    # echo "Sending POST request to " $1
    # echo "JSON body :" $2
    curl -s -d $2 -H "Content-Type: application/json" -X POST $1
}

meals="http://localhost:8383/meals"
coupons="http://localhost:8383/coupons"
orders="http://localhost:8181/orders"
deliveries="http://localhost:8282/deliveries"

echo "Listing meals :"
send_get_request $meals
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
send_post_request $coupons '{ "code": "10%MENU", "restaurantId": "'${restaurant_id}'" , "amount": "10", "discountType": "MENU_PERCENT", "description": "10% Off !", "date_expires": "2018-12-28"}' > coupons.json
cat coupons.json
printf "\n\n"

echo "Listing coupons :"
send_get_request $coupons
printf "\n\n"

echo "Sending request to order the an entry-main course-dessert with a coupon :"
send_post_request $orders '{"clientId": '${user_id}', "meals": [{"id": '${meal_entree_id}'}, {"id": '${meal_plat_id}'}, {"id": '${meal_dessert_id}'}], "coupon": {"code": "10%MENU"}, "restaurant": {"id": "'${restaurant_id}'", "name": "Absolute Ramen", "address": {"address_1": "3327 Gnatty Creek Road", "city": "Westbury", "postcode": "11590", "country": "US"}}, "shippingAddress": {"firstName": "John", "lastName": "Doe", "address_1": "1035 Golden Ridge Road", "address_2": "null", "city": "Schenectady", "postcode": "12305", "country": "US", "email": "jdoe@gmail.com", "phone": "518-393-5066"}, "billingAddress": {"firstName": "John", "lastName": "Doe", "address_1": "1035 Golden Ridge Road", "address_2": "null", "city": "Schenectady", "postcode": "12305", "country": "US"}}' > order_create_response.json
printf "\n\n"

echo "Confirming the order :"
send_patch_request $(cat order_create_response.json | jq '._links.complete.href' | tr -d '"') '{"status": "ACCEPTED"}' > order_confirm_response.json
printf "\n\n"

echo "Display created orders for the restaurant :"
send_get_request ${orders}"?status=ACCEPTED&restaurant=$restaurant_id"
printf "\n\n"

echo "Display pending orders :"
send_get_request ${deliveries}"/orders" > 
printf "\n\n"

echo "Getting the first order :"
send_get_request ${deliveries}"/orders/0" > first_order.json
printf "\n\n"

echo "Display existing coursiers :"
send_get_request ${deliveries}"/coursiers"
printf "\n\n"

echo "As Jamie, I take the previous order."
send_patch_request ${deliveries}"/orders/0" '{"id": "1","name": "Jamie"}'
printf "\n\n"

echo "The order becomes is assigned."
send_patch_request ${deliveries}"/orders/0" '{status": "ASSIGNED"}'
printf "\n\n"

#TODO do something
echo "We would like to know where the coursier is."
send_get_request ${geolocation}"/coursiers/0"
printf "\n\n"

echo "As Jamie, I announce that the order has been delivered."
send_patch_request ${deliveries}"/orders/0/status" '{status": ""}'
printf '\n\n'