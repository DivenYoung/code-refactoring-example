package org.coderead;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.coderead.model.Invoice;
import org.coderead.model.Play;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

/**
 * 新建类
 *
 * @author kendoziyu
 * @since 2020/10/11 0011
 */
public class StatementTest {

    private ObjectMapper mapper;

    @Before
    public void setUp() {
        mapper = new ObjectMapper();
    }

    @Test
    public void test() throws JsonProcessingException {
        String expected = """
                Statement for BigCo Hamlet: $650.00 (55 seats)
                 As You Like It: $580.00 (35 seats)
                 Othello: $500.00 (40 seats)
                Amount owed is $1,730.00
                You earned 47 credits
                """;

        // language=JSON
        final String plays = """
                 {
                     "hamlet":{
                         "name":"Hamlet",
                         "type":"tragedy"
                     },
                     "as-like":{
                         "name":"As You Like It",
                         "type":"comedy"
                     },
                     "othello":{
                         "name":"Othello",
                         "type":"tragedy"
                     }
                 }
                """;

        //language=JSON
        final String invoices = """
                 {
                     "customer":"BigCo",
                     "performances":[
                         {
                             "playId":"hamlet",
                             "audience":55
                         },
                         {
                             "playId":"as-like",
                             "audience":35
                         },
                         {
                             "playId":"othello",
                             "audience":40
                         }
                     ]
                 }
                """;

        TypeReference<Map<String, Play>> typeReference = new TypeReference<>() {
        };
        Map<String, Play> playMap = mapper.readValue(plays, typeReference);
        Invoice invoice = mapper.readValue(invoices, Invoice.class);
        Statement statement = new Statement(invoice, playMap);
        String result = statement.show();
        Assert.assertEquals(expected, result);
    }
}
