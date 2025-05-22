package com.trello;

import com.beymen.utils.LoggerHelper;
import io.restassured.response.Response;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/*
 1 - Trello üzerinde bir board oluşturunuz.
 2 - Oluşturduğunuz board’ a iki tane kart oluşturunuz.
 3 - Oluştrduğunuz bu iki karttan random olacak sekilde bir tanesini güncelleyiniz.
 4 - Oluşturduğunuz kartları siliniz.
 5 - Oluşturduğunuz board’ u siliniz.
 */

public class TrelloApiTest {

    private String boardId;
    private String listId;
    private final List<String> createdCardIds = new ArrayList<>();

    private static final Logger logger = LoggerHelper.getLogger(TrelloApiClient.class);


    @Before
    public void setUp() {
        // 1 - Trello üzerinde bir board oluşturunuz.

        String boardName = "Board_" + System.currentTimeMillis();
        Response boardResp = TrelloApiClient.createBoard(boardName);
        boardId = boardResp.jsonPath().getString("id");
        assertNotNull("Board ID should not be null", boardId);
        logger.info("Board created with ID: {}", boardId);

        listId = TrelloApiClient.getFirstListId(boardId);
        assertNotNull("List ID should not be null", listId);
    }

    @After
    public void tearDown() {
        // 4 - Oluşturduğunuz kartları siliniz.
        for (String cardId : createdCardIds) {
            TrelloApiClient.deleteCard(cardId);
            logger.info("Card deleted with ID: {}", cardId);
        }

        // 5 - Oluşturduğunuz board’ u siliniz.
        if (boardId != null) {
            TrelloApiClient.deleteBoard(boardId);
            logger.info("Board deleted with ID: {}", boardId);
        }
    }

    @Test
    public void testTrelloBoardWorkflow() {
        // 2 - Oluşturduğunuz board’ a iki tane kart oluşturunuz.
        createCardWithName("CardOne_");
        createCardWithName("CardTwo_");

        // 3 - Oluştrduğunuz bu iki karttan random olacak sekilde bir tanesini güncelleyiniz.
        String randomCardId = getRandomCardId();
        String updatedName = "Updated_" + System.currentTimeMillis();
        Response updateResponse = TrelloApiClient.updateCard(randomCardId, updatedName);
        assertEquals("Card name should be updated", updatedName, updateResponse.jsonPath().getString("name"));
        logger.info("Card updated with ID: {}", randomCardId);
    }

    private void createCardWithName(String prefix) {
        String name = prefix + System.currentTimeMillis();
        Response response = TrelloApiClient.createCard(listId, name);
        String cardId = response.jsonPath().getString("id");
        assertNotNull("Card ID should not be null", cardId);
        assertEquals("Card name should match", name, response.jsonPath().getString("name"));
        logger.info("Card created with ID: {}", cardId);
        createdCardIds.add(cardId);
    }

    private String getRandomCardId() {
        Random rand = new Random();
        return createdCardIds.get(rand.nextInt(createdCardIds.size()));
    }
}
