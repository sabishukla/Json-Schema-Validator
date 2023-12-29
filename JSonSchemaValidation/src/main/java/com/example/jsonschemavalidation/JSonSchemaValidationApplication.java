package com.example.jsonschemavalidation;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.*;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import java.util.stream.Collectors;

@SpringBootApplication
public class JSonSchemaValidationApplication {

    public static void main(String[] args) {
        try {
            // Your JSON data

            String jsonString = "{\"value1\":\"981.892\",\"value2\":\"189938.2934\",\"value3\":123.123}";

            // Create ObjectMapper instance
            ObjectMapper objectMapper = new ObjectMapper();

            // Convert JSON string to JsonNode
            JsonNode jsonNodeData = objectMapper.readTree(jsonString);

            SchemaValidatorsConfig config = new SchemaValidatorsConfig();
            config.setTypeLoose(true);

            String jsonSchemaString = "{" +
                    "  \"type\": \"object\"," +
                    "  \"properties\": {" +
                    "    \"value1\": {" +
                    "      \"type\": \"number\"," +
                    "      \"multipleOf\": 0.01" +
                    "    }," +
                    "    \"value2\": {" +
                    "      \"type\": \"number\"," +
                    "      \"multipleOf\": 0.01" +
                    "    }," +
                    "    \"value3\": {" +
                    "      \"type\": \"number\"," +
                    "      \"multipleOf\": 0.01" +
                    "    }" +
                    "  }," +
                    "  \"required\": [\"value1\"]" +
                    "}";
            JsonNode jsonSchemaNode = objectMapper.readTree(jsonSchemaString);
            JsonSchema jsonSchema = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7).getSchema(jsonSchemaNode, config);

            System.out.println(jsonSchema.validate(jsonNodeData)
                    .stream()
                    .map(ValidationMessage::getMessage)
                    .collect(Collectors.toList())
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
