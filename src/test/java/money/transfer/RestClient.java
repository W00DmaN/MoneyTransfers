/*
 * Copyright 2018 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package money.transfer;

import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.client.annotation.Client;
import io.reactivex.Single;
import money.transfer.rest.model.req.CreateUserRequest;
import money.transfer.rest.model.req.DepositUserRequest;
import money.transfer.rest.model.req.TransferRequest;
import money.transfer.rest.model.res.TransferResponse;
import money.transfer.rest.model.res.UserResponse;

import java.util.List;

@Client("/")
public interface RestClient {
    @Get("/health")
    Single<String> health();

    @Post("/user")
    Single<UserResponse> createUser(CreateUserRequest request);

    @Get("/user/all")
    Single<List<UserResponse>> getAllUsers();

    @Get("/user/{id}")
    Single<UserResponse> getUserById(long id);

    @Put("/user/{id}/deposit")
    Single<UserResponse> addMoney(long id, DepositUserRequest depositUserRequest);

    @Post("/transfer/money/from/{userFromId}/to/{userToId}")
    Single<TransferResponse> transferMoney(long userFromId, long userToId, TransferRequest request);
}
