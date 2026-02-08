package hub.com.api_store.nums;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GlobalUnit Tests")
class GlobalUnitTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Nested
    @DisplayName("fromString() method")
    class FromStringTests {

        @Test
        @DisplayName("convierte minúsculas a enum")
        void withLowerCase_shouldConvertToEnum() {
            // Act
            GlobalUnit result = GlobalUnit.fromString("kg");

            // Assert
            assertEquals(GlobalUnit.KG, result);
        }

        @Test
        @DisplayName("convierte mayúsculas a enum")
        void withUpperCase_shouldConvertToEnum() {
            // Act
            GlobalUnit result = GlobalUnit.fromString("LITER");

            // Assert
            assertEquals(GlobalUnit.LITER, result);
        }

        @Test
        @DisplayName("convierte mixto a enum")
        void withMixedCase_shouldConvertToEnum() {
            // Act
            GlobalUnit result = GlobalUnit.fromString("GrAm");

            // Assert
            assertEquals(GlobalUnit.GRAM, result);
        }
    }

    @Nested
    @DisplayName("toValue() method")
    class ToValueTests {

        @Test
        @DisplayName("retorna el nombre del enum en mayúsculas")
        void shouldReturnEnumName() {
            // Act
            String result = GlobalUnit.KG.toValue();

            // Assert
            assertEquals("KG", result);
        }
    }

    @Nested
    @DisplayName("Jackson Integration")
    class JacksonIntegrationTests {

        @Test
        @DisplayName("deserializa JSON con minúsculas correctamente")
        void deserialization_withLowerCase_shouldWork() throws Exception {
            // Arrange
            String json = "\"kg\"";

            // Act
            GlobalUnit result = objectMapper.readValue(json, GlobalUnit.class);

            // Assert
            assertEquals(GlobalUnit.KG, result);
        }

        @Test
        @DisplayName("serializa enum a String en mayúsculas")
        void serialization_shouldReturnUpperCase() throws Exception {
            // Act
            String result = objectMapper.writeValueAsString(GlobalUnit.LITER);

            // Assert
            assertEquals("\"LITER\"", result);
        }
    }
}