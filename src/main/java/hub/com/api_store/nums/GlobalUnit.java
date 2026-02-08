package hub.com.api_store.nums;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum GlobalUnit {
    KG,
    LITER,
    UNIT,
    GRAM,
    METER,
    BOX,
    PACKAGE;

    @JsonCreator
    public static GlobalUnit fromString(String value) {
        return GlobalUnit.valueOf(value.toUpperCase());
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}
