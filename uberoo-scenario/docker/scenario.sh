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

echo "Listing meals :"
send_get_request http://localhost:8080/meals
printf "\n\n"

echo "Filtering meals by \"asian\" tag :"
send_get_request http://localhost:8080/meals?tag=asian > asian_meals.json
cat asian_meals.json
printf "\n\n"

user_id=1
# Restaurant id is the same for the other meals as ramen
restaurant_id=$(cat asian_meals.json | jq -r '._embedded.meals[] | select(.label == "Ramen") | .restaurant.id')
echo ${restaurant_id}
meal_address_entree=$(cat asian_meals.json | jq -r '._embedded.meals[] | select(.label == "Soupe miso") | ._links.self.href' | tr -d '"')
echo ${meal_address_entree}
meal_address_plat=$(cat asian_meals.json | jq -r '._embedded.meals[] | select(.label == "Ramen") | ._links.self.href' | tr -d '"')
echo ${meal_address_plat}
meal_address_dessert=$(cat asian_meals.json | jq -r '._embedded.meals[] | select(.label == "Glace coco") | ._links.self.href' | tr -d '"')
echo ${meal_address_dessert}
meal_entree_id=${meal_address_entree##*/}
meal_plat_id=${meal_address_plat##*/}
meal_dessert_id=${meal_address_dessert##*/}

echo "${meal_entree_id} + ${meal_plat_id} + ${meal_dessert_id}"

# -------------
echo "Creating a promotional code '10%OFF' on full menu orders for the restaurant id: ${restaurant_id}"
send_post_request http://localhost:8080/coupons '{ "code": "10%MENU", "restaurantId": "'${restaurant_id}'" , "amount": "10", "discountType": "MENU_PERCENT", "description": "10% Off !", "date_expires": "2018-12-28"}' > coupons.json
cat coupons.json
printf "\n\n"

echo "Listing coupons :"
send_get_request http://localhost:8080/coupons
printf "\n\n"

echo "Sending request to order the an entry-main course-dessert with a coupon :"
send_post_request http://localhost:8181/orders '{"clientId": '${user_id}', "meals": [{"id": '${meal_entree_id}'}, {"id": '${meal_plat_id}'}, {"id": '${meal_dessert_id}'}], "coupon": {"code": "10%MENU"}, "restaurant": {"id": "'${restaurant_id}'", "name": "Absolute Ramen", "address": {"address_1": "3327 Gnatty Creek Road", "city": "Westbury", "postcode": "11590", "country": "US"}}, "shippingAddress": {"firstName": "John", "lastName": "Doe", "address_1": "1035 Golden Ridge Road", "address_2": "null", "city": "Schenectady", "postcode": "12305", "country": "US", "email": "jdoe@gmail.com", "phone": "518-393-5066"}, "billingAddress": {"firstName": "John", "lastName": "Doe", "address_1": "1035 Golden Ridge Road", "address_2": "null", "city": "Schenectady", "postcode": "12305", "country": "US"}}' > order_create_response.json
printf "\n\n"

echo "Confirming the order :"
send_patch_request $(cat order_create_response.json | jq '._links.complete.href' | tr -d '"') '{"status": "ACCEPTED"}' > order_confirm_response.json
printf "\n\n"

echo "Display created orders for the restaurant :"
send_get_request "http://localhost:8181/orders?status=COMPLETED&restaurant=$restaurant_id"
printf "\n\n"
