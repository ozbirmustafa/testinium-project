package com.trello;

import com.beymen.utils.ConfigReader;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class TrelloApiClient {
    private static final String BASE  = ConfigReader.getProperty("trello.base.url");
    private static final String KEY   = ConfigReader.getProperty("trello.key");
    private static final String TOKEN = ConfigReader.getProperty("trello.token");

    static {
        RestAssured.baseURI = BASE;
    }

    private static RequestSpecification withAuth() {
        return RestAssured
                .given()
                .queryParam("key", KEY)
                .queryParam("token", TOKEN)
                .contentType("application/json")
                .accept("application/json");
    }

    public static Response createBoard(String name) {
        return withAuth()
                .queryParam("name", name)
                .when()
                .post("/boards")
                .then()
                .statusCode(200)
                .extract().response();
    }

    public static Response getLists(String boardId) {
        return withAuth()
                .when()
                .get("/boards/" + boardId + "/lists")
                .then()
                .statusCode(200)
                .extract().response();
    }

    public static String getFirstListId(String boardId) {
        Response r = getLists(boardId);
        return r.jsonPath().getString("[0].id");
    }

    public static Response createCard(String listId, String name) {
        return withAuth()
                .queryParam("idList", listId)
                .queryParam("name", name)
                .when()
                .post("/cards")
                .then()
                .statusCode(200)
                .extract().response();
    }

    public static Response updateCard(String cardId, String newName) {
        return withAuth()
                .queryParam("name", newName)
                .when()
                .put("/cards/" + cardId)
                .then()
                .statusCode(200)
                .extract().response();
    }

    public static void deleteCard(String cardId) {
        withAuth()
                .when()
                .delete("/cards/" + cardId)
                .then()
                .statusCode(200)
                .extract().response();
    }

    public static void deleteBoard(String boardId) {
        withAuth()
                .when()
                .delete("/boards/" + boardId)
                .then()
                .statusCode(200)
                .extract().response();
    }
}