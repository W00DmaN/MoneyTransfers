## Micronaut 

This repository is simple app for transfer money

### API 

##### Create user 

POST http://localhost:8080/user

###### request
```json
{
	"name" : "UserName"
}
```
###### response

```json
{
    "id": 1,
    "name": "UserName",
    "cents": 0
}
```

##### Get user 

GET http://localhost:8080/user/{userId}

###### response

```json
{
    "id": 1,
    "name": "UserName",
    "cents": 0
}
```

##### Get all users

GET http://localhost:8080/user/all

###### response

```json
[
    {
        "id": 1,
        "name": "UserName",
        "cents": 0
    }
]
```

##### Add money for user

PUT http://localhost:8080/user/{userId}}/deposit

###### request
```json
{
	"countCents" : 5000
}
```

###### response

```json
{
    "id": 1,
    "name": "UserName",
    "cents": 5000
}
```

##### Transfer money

POST http://localhost:8080/transfer/money/from/{uerId}}/to/{userId}

###### request
```json
{
	"summ" : 1
}
```


###### response

```json
{
    "id": 1,
    "userFrom": 1,
    "userTo": 2,
    "summ": 1
}
```