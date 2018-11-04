# Uberoo Deliveries

This service offers the possibility to list the coursiers, and allows a coursier to assign themselves to a pending order.

## Documentation

### Objects

#### Coursier

| Field name | Type | Description |
 ------ | ---- | -----|
 `id` | `long` | Unique numeric identifier for the coursier.
 `name` | `string` | Name of the coursier.
 
#### Order

| Field name | Type | Description |
 ------ | ---- | -----|
 `meal` | Meal | Meal to be delivered. |
 `coursier` | Coursier | The coursier assigned to the order, if any.
 `eta` | `long` | Estimated time of arrival previously computed.
 
#### Meal

| Field name | Type | Description |
 ------ | ---- | -----|
 `label` | `string` | Name of the meal.
 `restaurant` | Restaurant | The restaurant making the meal.
 `description` | `string` | Description of the meal.
 
#### Restaurant

| Field name | Type | Description |
 ------ | ---- | -----|
 `name` | `string` | Name of the restaurant.

### Requests

| Command | URI | Description |
 ------- | --- | ----------- |
`GET`| `/coursiers` | Returns a list of the different Coursiers.
`GET`| `/coursiers/{id}` | Return the Coursier identified by the specified `id`.
`GET` | `/orders` | Returns the whole set of Orders to be delivered.
`GET` | `/orders/{id}` | Returns a specific Order identified by the specified `id`.
`PATCH` | `/orders/{id}` | By specifying a Coursier object in request body, assigns a Coursier to the specified Order.

