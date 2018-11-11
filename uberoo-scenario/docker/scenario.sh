#!/bin/sh

echo "Listing meals :"
curl -s http://localhost:8080/meals
printf "\n\n"

echo "Filtering meals by \"asian\" tag :"
curl -s http://localhost:8080/meals?tag=asian > asian_meals.json
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
echo "CCreating a promotional code '10%OFF' on full menu orders for the restaurant id: ${restaurant_id}"
curl -s -d '{ "code": "10%MENU", "restaurantId": "'${restaurant_id}'" , "amount": "10", "discountType": "MENU_PERCENT", "description": "10% Off !", "date_expires": "2018-12-28"}' -H "Content-Type: application/json;charset=UTF-8" -X POST http://localhost:8080/coupons > coupons.json
cat coupons.json
printf "\n\n"

echo "Listing coupons :"
curl -s http://localhost:8080/coupons
printf "\n\n"

echo "Sending request to order the an entry-main course-dessert with a coupon :"
curl -s -d '{"clientId": '${user_id}', "meals": [{"id": '${meal_entree_id}'}, {"id": '${meal_plat_id}'}, {"id": '${meal_dessert_id}'}], "coupon": {"code": "10%MENU"}, "restaurant": {"id": "'${restaurant_id}'", "name": "Absolute Ramen", "address": {"address_1": "3327 Gnatty Creek Road", "city": "Westbury", "postcode": "11590", "country": "US"}}, "shippingAddress": {"firstName": "John", "lastName": "Doe", "address_1": "1035 Golden Ridge Road", "address_2": "null", "city": "Schenectady", "postcode": "12305", "country": "US", "email": "jdoe@gmail.com", "phone": "518-393-5066"}, "billingAddress": {"firstName": "John", "lastName": "Doe", "address_1": "1035 Golden Ridge Road", "address_2": "null", "city": "Schenectady", "postcode": "12305", "country": "US"}}' -H "Content-Type: application/json" -X POST http://localhost:8181/orders > order_create_response.json
cat order_create_response.json
printf "\n\n"


echo "Confirming the order the eta is  :"
curl -s -d {"status": "ACCEPTED"} -X PATCH $(cat order_create_response.json | jq '._links.complete.href' | tr -d '"') > order_confirm_response.json
cat order_confirm_response.json
printf "\n\n"

echo "Display created orders for the restaurant :"
curl -s "http://localhost:8181/orders?status=ACCEPTED&restaurant=$restaurant_id"
printf "\n\n"

