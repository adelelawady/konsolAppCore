# Konsol Core API

Konsol Core API documentation

## Version: 0.0.1

### Terms of service

**Contact information:**  
ADEL ALI (KONSOL)  
https://github.com/adelelawady  
adel50ali50@gmail.com

**License:** [APACHE LICENSE, VERSION 2.0](https://www.apache.org/licenses/LICENSE-2.0)

### /stores/{id}

#### GET

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id   | path       |             | Yes      | string |
| id   | path       |             | Yes      | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

#### DELETE

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id   | path       |             | Yes      | string |
| id   | path       |             | Yes      | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

#### PATCH

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id   | path       |             | Yes      | string |
| id   | path       |             | Yes      | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

### /monies/{id}

#### GET

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id   | path       |             | Yes      | string |
| id   | path       |             | Yes      | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

#### DELETE

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id   | path       |             | Yes      | string |
| id   | path       |             | Yes      | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

#### PATCH

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id   | path       |             | Yes      | string |
| id   | path       |             | Yes      | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

### /items/{id}

#### GET

##### Summary:

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id   | path       |             | Yes      | string |
| id   | path       |             | Yes      | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

#### PUT

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id   | path       |             | Yes      | string |
| id   | path       |             | Yes      | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

#### DELETE

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id   | path       |             | Yes      | string |
| id   | path       |             | Yes      | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

#### PATCH

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id   | path       |             | Yes      | string |
| id   | path       |             | Yes      | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

### /invoices/{id}

#### GET

##### Description:

get single invoice by id

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id   | path       |             | Yes      | string |
| id   | path       |             | Yes      | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

#### DELETE

##### Description:

delete invoice by invoice id

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id   | path       |             | Yes      | string |
| id   | path       |             | Yes      | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

#### PATCH

##### Description:

update invoice

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id   | path       |             | Yes      | string |
| id   | path       |             | Yes      | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

#### POST

##### Summary:

save and submit invoice

##### Description:

save and submit invoice by invoice id
changes invoice status to active

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id   | path       |             | Yes      | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

### /invoices/view/{id}

#### GET

##### Description:

get single invoice by id

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id   | path       |             | Yes      | string |
| id   | path       |             | Yes      | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

### /banks/{id}

#### GET

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id   | path       |             | Yes      | string |
| id   | path       |             | Yes      | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

#### DELETE

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id   | path       |             | Yes      | string |
| id   | path       |             | Yes      | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

#### PATCH

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id   | path       |             | Yes      | string |
| id   | path       |             | Yes      | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

### /account-user/{id}

#### GET

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id   | path       |             | Yes      | string |
| id   | path       |             | Yes      | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

#### DELETE

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id   | path       |             | Yes      | string |
| id   | path       |             | Yes      | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

#### PATCH

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id   | path       |             | Yes      | string |
| id   | path       |             | Yes      | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

### /account-user/{id}/transactions

#### POST

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id   | path       |             | Yes      | string |
| id   | path       |             | Yes      | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

### /account-user

#### GET

##### Parameters

| Name | Located in | Description                                   | Required                                                                      | Schema  |
| ---- | ---------- | --------------------------------------------- | ----------------------------------------------------------------------------- | ------- | ---------- |
| page | query      | Zero-based page index (0..N)                  | No                                                                            | integer |
| size | query      | The size of the page to be returned           | No                                                                            | integer |
| sort | query      | Sorting criteria in the format: property,(asc | desc). Default sort order is ascending. Multiple sort criteria are supported. | No      | [ string ] |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

#### POST

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

### /account-user/view

#### POST

##### Summary:

SearchAccountUsers

##### Description:

search in all account users using all fields available

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

### /stores

#### GET

##### Parameters

| Name      | Located in | Description                                   | Required                                                                      | Schema  |
| --------- | ---------- | --------------------------------------------- | ----------------------------------------------------------------------------- | ------- | ---------- |
| page      | query      | Zero-based page index (0..N)                  | No                                                                            | integer |
| size      | query      | The size of the page to be returned           | No                                                                            | integer |
| sort      | query      | Sorting criteria in the format: property,(asc | desc). Default sort order is ascending. Multiple sort criteria are supported. | No      | [ string ] |
| eagerload | query      |                                               | No                                                                            | boolean |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

#### POST

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

### /stores/names

#### GET

##### Description:

return all store names with id

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

### /stores/storeItems

#### POST

##### Summary:

##### Description:

creates if not exsits or updates exsitsing store item
for selected item and store

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

### /stores/item/{id}/storeItems

#### GET

##### Description:

return all store items qty in all stores item availabe in with item id

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id   | path       |             | Yes      | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

### /stores/item/{id}/storeItems/all

#### GET

##### Description:

return all store items qty in all stores with item id

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id   | path       |             | Yes      | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

### /stores/{id}/storeItems

#### POST

##### Summary:

##### Description:

return store items for store by store id

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id   | path       |             | Yes      | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

### /monies

#### GET

##### Parameters

| Name | Located in | Description                                   | Required                                                                      | Schema  |
| ---- | ---------- | --------------------------------------------- | ----------------------------------------------------------------------------- | ------- | ---------- |
| page | query      | Zero-based page index (0..N)                  | No                                                                            | integer |
| size | query      | The size of the page to be returned           | No                                                                            | integer |
| sort | query      | Sorting criteria in the format: property,(asc | desc). Default sort order is ascending. Multiple sort criteria are supported. | No      | [ string ] |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

#### POST

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

### /monies/view

#### POST

##### Summary:

Search Monies [ pagination , time , plaintext , sort ] { view model dto }

##### Description:

invoice view dto search and pagination and sort request

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

### /items

#### GET

##### Parameters

| Name      | Located in | Description                                   | Required                                                                      | Schema  |
| --------- | ---------- | --------------------------------------------- | ----------------------------------------------------------------------------- | ------- | ---------- |
| page      | query      | Zero-based page index (0..N)                  | No                                                                            | integer |
| size      | query      | The size of the page to be returned           | No                                                                            | integer |
| sort      | query      | Sorting criteria in the format: property,(asc | desc). Default sort order is ascending. Multiple sort criteria are supported. | No      | [ string ] |
| eagerload | query      |                                               | No                                                                            | boolean |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

#### POST

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

### /items/{id}/units

#### GET

##### Description:

get all item units available

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id   | path       |             | Yes      | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

### /items/units/{id}/delete

#### DELETE

##### Summary:

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id   | path       |             | Yes      | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

### /items/view

#### POST

##### Summary:

##### Description:

item view dto search and pagination and sort request

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

### /items/categories

#### GET

##### Description:

get categories from all items available

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

### /items/categories/{containerId}/view

#### POST

##### Summary:

##### Parameters

| Name        | Located in | Description | Required | Schema |
| ----------- | ---------- | ----------- | -------- | ------ |
| containerId | path       |             | Yes      | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

### /items/category/listItems

#### POST

##### Summary:

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

### /items/pk/{PkId}

#### GET

##### Description:

get item by pk id

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| PkId | path       |             | Yes      | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

### /items/id/{id}/before

#### GET

##### Description:

get the item before this item by id

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id   | path       |             | Yes      | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

### /items/id/{id}/after

#### GET

##### Description:

get the item before this item by id

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id   | path       |             | Yes      | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

### /invoices

#### GET

##### Parameters

| Name      | Located in | Description                                   | Required                                                                      | Schema  |
| --------- | ---------- | --------------------------------------------- | ----------------------------------------------------------------------------- | ------- | ---------- |
| page      | query      | Zero-based page index (0..N)                  | No                                                                            | integer |
| size      | query      | The size of the page to be returned           | No                                                                            | integer |
| sort      | query      | Sorting criteria in the format: property,(asc | desc). Default sort order is ascending. Multiple sort criteria are supported. | No      | [ string ] |
| eagerload | query      |                                               | No                                                                            | boolean |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

#### POST

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

### /invoices/{id}/invoiceItems

#### GET

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id   | path       |             | Yes      | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

### /invoices/view

#### POST

##### Summary:

Search Invoices [ pagination , time , plaintext , sort ] { view model dto }

##### Description:

invoice view dto search and pagination and sort request

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

### /invoices/{kind}/create

#### GET

##### Description:

initialize a new Invoice

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| kind | path       |             | Yes      | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

### /invoices/{id}/print

#### GET

##### Description:

create a printable object of invoice print dto and global options used for user and general info

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id   | path       |             | Yes      | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

### /invoices/{id}/add

#### POST

##### Summary:

add invoice item to invocie

##### Description:

add item to invoice includes price qty etc ..

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id   | path       |             | Yes      | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

### /invoices/invoice-items/{id}

#### DELETE

##### Summary:

##### Description:

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id   | path       |             | Yes      | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

#### PATCH

##### Summary:

##### Description:

update invoices invoice item

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id   | path       |             | Yes      | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

### /banks

#### GET

##### Parameters

| Name | Located in | Description                                   | Required                                                                      | Schema  |
| ---- | ---------- | --------------------------------------------- | ----------------------------------------------------------------------------- | ------- | ---------- |
| page | query      | Zero-based page index (0..N)                  | No                                                                            | integer |
| size | query      | The size of the page to be returned           | No                                                                            | integer |
| sort | query      | Sorting criteria in the format: property,(asc | desc). Default sort order is ascending. Multiple sort criteria are supported. | No      | [ string ] |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

#### POST

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

### /register

#### POST

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |

##### Responses

| Code | Description |
| ---- | ----------- |
| 201  | Created     |

### /admin/users

#### GET

##### Parameters

| Name | Located in | Description                                   | Required                                                                      | Schema  |
| ---- | ---------- | --------------------------------------------- | ----------------------------------------------------------------------------- | ------- | ---------- |
| page | query      | Zero-based page index (0..N)                  | No                                                                            | integer |
| size | query      | The size of the page to be returned           | No                                                                            | integer |
| sort | query      | Sorting criteria in the format: property,(asc | desc). Default sort order is ascending. Multiple sort criteria are supported. | No      | [ string ] |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

#### PUT

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

#### POST

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

### /authenticate

#### GET

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

#### POST

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

### /account

#### GET

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

#### POST

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

### /account/reset-password/init

#### POST

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

### /account/reset-password/finish

#### POST

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

### /account/change-password

#### POST

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

### /users

#### GET

##### Parameters

| Name | Located in | Description                                   | Required                                                                      | Schema  |
| ---- | ---------- | --------------------------------------------- | ----------------------------------------------------------------------------- | ------- | ---------- |
| page | query      | Zero-based page index (0..N)                  | No                                                                            | integer |
| size | query      | The size of the page to be returned           | No                                                                            | integer |
| sort | query      | Sorting criteria in the format: property,(asc | desc). Default sort order is ascending. Multiple sort criteria are supported. | No      | [ string ] |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

### /authorities

#### GET

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

### /admin/users/{login}

#### GET

##### Parameters

| Name  | Located in | Description | Required | Schema |
| ----- | ---------- | ----------- | -------- | ------ |
| login | path       |             | Yes      | string |
| login | path       |             | Yes      | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

#### DELETE

##### Parameters

| Name  | Located in | Description | Required | Schema |
| ----- | ---------- | ----------- | -------- | ------ |
| login | path       |             | Yes      | string |
| login | path       |             | Yes      | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

### /activate

#### GET

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| key  | query      |             | Yes      | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

### /sys/settings

#### GET

##### Summary:

Get System Options

##### Description:

get main system invoices , handlers , pos global options file

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

#### PATCH

##### Summary:

Update Global Application options

##### Description:

Update Global Application options

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

### /banks/{id}/transactions

#### POST

##### Summary:

##### Description:

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id   | path       |             | Yes      | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

### /banks/{id}/analysis

#### GET

##### Summary:

Your GET endpoint

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id   | path       |             | Yes      | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

### /items/{id}/analysis

#### POST

##### Summary:

##### Description:

get all item analysis by store , or dates

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id   | path       |             | Yes      | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

### /items/{id}/charts

#### POST

##### Summary:

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id   | path       |             | Yes      | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

### /financial/dashboard

#### POST

##### Summary:

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

### /financial/playstation/dashboard

#### POST

##### Summary:

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

### /playstation/device

#### GET

##### Summary:

get all playstation devices

##### Description:

get all playstation devices

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

#### POST

##### Summary:

##### Description:

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

### /playstation/device/category

#### POST

##### Summary:

##### Description:

get all devices by category

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

#### GET

##### Summary:

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

### /playstation/device/{id}/orders

#### POST

##### Summary:

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id   | path       |             | Yes      | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

### /playstation/device/{id}/move/{deviceId}

#### POST

##### Summary:

##### Description:

##### Parameters

| Name     | Located in | Description | Required | Schema |
| -------- | ---------- | ----------- | -------- | ------ |
| id       | path       |             | Yes      | string |
| deviceId | path       |             | Yes      | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

### /playstation/device/{id}/session/updateType/{typeId}

#### POST

##### Summary:

##### Description:

##### Parameters

| Name   | Located in | Description | Required | Schema |
| ------ | ---------- | ----------- | -------- | ------ |
| id     | path       |             | Yes      | string |
| typeId | path       |             | Yes      | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

### /playstation/device/{id}/orders/{orderId}

#### POST

##### Summary:

##### Description:

##### Parameters

| Name    | Located in | Description | Required | Schema |
| ------- | ---------- | ----------- | -------- | ------ |
| id      | path       |             | Yes      | string |
| orderId | path       |             | Yes      | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

#### DELETE

##### Summary:

##### Parameters

| Name    | Located in | Description | Required | Schema |
| ------- | ---------- | ----------- | -------- | ------ |
| id      | path       |             | Yes      | string |
| orderId | path       |             | Yes      | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

### /playstation/device/type

#### GET

##### Summary:

get all playstation devices types

##### Description:

get all playstation devices types

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

#### POST

##### Summary:

##### Description:

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

### /playstation/session

#### POST

##### Summary:

##### Description:

get all playstation devices sessions

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

### /playstation/session/{containerId}/view

#### POST

##### Summary:

##### Description:

get all playstation devices sessions by the container

##### Parameters

| Name        | Located in | Description | Required | Schema |
| ----------- | ---------- | ----------- | -------- | ------ |
| containerId | path       |             | Yes      | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

### /playstation/device/{id}

#### GET

##### Summary:

get all playstation devices

##### Description:

get all playstation devices

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id   | path       |             | Yes      | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

#### PUT

##### Summary:

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id   | path       |             | Yes      | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

#### PATCH

##### Summary:

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id   | path       |             | Yes      | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

#### DELETE

##### Summary:

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id   | path       |             | Yes      | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

### /playstation/session/{id}

#### GET

##### Summary:

get all playstation devices

##### Description:

get playstation session

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id   | path       |             | Yes      | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

#### PUT

##### Summary:

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id   | path       |             | Yes      | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

#### PATCH

##### Summary:

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id   | path       |             | Yes      | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

#### DELETE

##### Summary:

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id   | path       |             | Yes      | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

### /playstation/session/{id}/print

#### POST

##### Summary:

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id   | path       |             | Yes      | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

### /playstation/device/{id}/session/start

#### POST

##### Summary:

Start Device Session

##### Description:

Start Device Session

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id   | path       |             | Yes      | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

### /playstation/device/{id}/session/stop

#### POST

##### Summary:

Stop Device Session

##### Description:

Stop Device Session

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id   | path       |             | Yes      | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

### /playstation/device/{id}/session/invoice/update

#### POST

##### Summary:

Update Device Session Invoice

##### Description:

Update Device Session Invoice

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id   | path       |             | Yes      | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

### /playstation/device/type/{id}

#### GET

##### Summary:

get all playstation devices

##### Description:

get playstation device type

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id   | path       |             | Yes      | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

#### PUT

##### Summary:

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id   | path       |             | Yes      | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

#### PATCH

##### Summary:

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id   | path       |             | Yes      | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

#### DELETE

##### Summary:

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id   | path       |             | Yes      | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

### /playstation-containers

#### POST

##### Summary:

Create a new PlaystationContainer

##### Description:

Creates a new PlaystationContainer if it does not already have an ID.

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |

##### Responses

| Code | Description                               |
| ---- | ----------------------------------------- |
| 201  | Successfully created PlaystationContainer |
| 400  | Invalid input or ID already exists        |

#### GET

##### Summary:

Get all PlaystationContainers

##### Description:

Retrieves a paginated list of all PlaystationContainers.

##### Parameters

| Name | Located in | Description            | Required | Schema  |
| ---- | ---------- | ---------------------- | -------- | ------- |
| page | query      | The page number.       | No       | integer |
| size | query      | The size of each page. | No       | integer |

##### Responses

| Code | Description                                          |
| ---- | ---------------------------------------------------- |
| 200  | Successfully retrieved list of PlaystationContainers |

### /playstation-containers/{id}

#### GET

##### Summary:

Get a PlaystationContainer by ID

##### Description:

Retrieves a single PlaystationContainer by its ID.

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id   | path       |             | Yes      | string |
| id   | path       |             | Yes      | string |

##### Responses

| Code | Description                                 |
| ---- | ------------------------------------------- |
| 200  | Successfully retrieved PlaystationContainer |
| 404  | PlaystationContainer not found              |

#### PUT

##### Summary:

Update a PlaystationContainer

##### Description:

Updates an existing PlaystationContainer by ID.

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id   | path       |             | Yes      | string |
| id   | path       |             | Yes      | string |

##### Responses

| Code | Description                               |
| ---- | ----------------------------------------- |
| 200  | Successfully updated PlaystationContainer |
| 400  | Invalid ID or data                        |
| 404  | PlaystationContainer not found            |

#### PATCH

##### Summary:

Partially update a PlaystationContainer

##### Description:

Updates specific fields of a PlaystationContainer.

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id   | path       |             | Yes      | string |
| id   | path       |             | Yes      | string |

##### Responses

| Code | Description                               |
| ---- | ----------------------------------------- |
| 200  | Successfully updated PlaystationContainer |
| 400  | Invalid ID or data                        |
| 404  | PlaystationContainer not found            |

#### DELETE

##### Summary:

Delete a PlaystationContainer

##### Description:

Deletes an existing PlaystationContainer by ID.

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id   | path       |             | Yes      | string |
| id   | path       |             | Yes      | string |

##### Responses

| Code | Description                               |
| ---- | ----------------------------------------- |
| 204  | Successfully deleted PlaystationContainer |
| 404  | PlaystationContainer not found            |

### /shefts

#### POST

##### Summary:

Create a new Sheft

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |

##### Responses

| Code | Description |
| ---- | ----------- |
| 201  | Created     |
| 400  | Bad Request |

#### GET

##### Summary:

Get all Shefts

##### Parameters

| Name | Located in | Description                | Required | Schema  |
| ---- | ---------- | -------------------------- | -------- | ------- |
| page | query      | Page number for pagination | No       | integer |
| size | query      | Page size for pagination   | No       | integer |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

### /shefts/{id}

#### GET

##### Summary:

Get a Sheft by ID

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id   | path       |             | Yes      | string |
| id   | path       |             | Yes      | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |
| 404  | Not Found   |

#### PUT

##### Summary:

Update a Sheft

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id   | path       |             | Yes      | string |
| id   | path       |             | Yes      | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |
| 400  | Bad Request |
| 404  | Not Found   |

#### PATCH

##### Summary:

Partially update a Sheft

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id   | path       |             | Yes      | string |
| id   | path       |             | Yes      | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |
| 400  | Bad Request |
| 404  | Not Found   |

#### DELETE

##### Summary:

Delete a Sheft

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id   | path       |             | Yes      | string |
| id   | path       |             | Yes      | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 204  | No Content  |

### /shefts/current/active

#### POST

##### Summary:

##### Parameters

| Name  | Located in | Description | Required | Schema  |
| ----- | ---------- | ----------- | -------- | ------- |
| print | query      |             | No       | boolean |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

#### GET

##### Summary:

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

#### PUT

##### Summary:

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

### /shefts/current/active/sessions

#### GET

##### Summary:

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

### /sys/settings/backup

#### POST

##### Summary:

Perform manual backup

##### Description:

Initiates a manual backup of the system using current backup settings

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

### /sys/settings/restore

#### POST

##### Summary:

Restore system from backup

##### Description:

Restores the system from a specified backup directory

##### Responses

| Code | Description                                           |
| ---- | ----------------------------------------------------- |
| 200  | OK                                                    |
| 400  | Bad Request - Invalid backup path or backup not found |
| 500  | Internal Server Error - Restore operation failed      |

### /playstation/device/{id}/type/{typeId}

#### POST

##### Summary:

Update device type

##### Description:

Updates the device type and optionally updates the active session type

##### Parameters

| Name          | Located in | Description                                          | Required | Schema  |
| ------------- | ---------- | ---------------------------------------------------- | -------- | ------- |
| id            | path       | ID of the device to update                           | Yes      | string  |
| typeId        | path       | ID of the new device type                            | Yes      | string  |
| updateSession | query      | Whether to update the type on active session as well | No       | boolean |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

### /sys/license

#### POST

##### Summary:

Process super admin license key

##### Description:

Processes an encrypted license key to generate a new license

##### Responses

| Code | Description                                     |
| ---- | ----------------------------------------------- |
| 200  | License successfully processed and generated    |
| 400  | Invalid license key or processing failed        |
| 401  | Unauthorized - insufficient permissions         |
| 500  | Internal server error during license processing |

### /public/sys/license

#### POST

##### Summary:

Process super admin license key

##### Description:

Processes an encrypted license key to generate a new license

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |

##### Responses

| Code | Description                                     |
| ---- | ----------------------------------------------- |
| 200  | License successfully processed and generated    |
| 400  | Invalid license key or processing failed        |
| 401  | Unauthorized - insufficient permissions         |
| 500  | Internal server error during license processing |

#### GET

##### Summary:

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

### /public/sys/user/license

#### GET

##### Summary:

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200  | OK          |

### /sys/license/generate

#### POST

##### Summary:

Generate encrypted admin license key

##### Description:

Generates an encrypted admin license key for testing purposes (development only)

##### Responses

| Code | Description                                 |
| ---- | ------------------------------------------- |
| 200  | Encrypted admin key generated successfully  |
| 400  | Invalid request parameters                  |
| 401  | Unauthorized - insufficient permissions     |
| 500  | Internal server error during key generation |
