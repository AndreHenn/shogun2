package de.terrestris.shoguncore.util.json;

import com.bedatadriven.jackson.datatype.jts.JtsModule;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import org.junit.Test;

import java.text.DateFormat;
import java.util.List;
import java.util.TimeZone;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

/**
 * @author Nils Bühner
 */
public class ShogunCoreJsonObjectMapperTest {

    /**
     * The object mapper to test.
     */
    private final ObjectMapper objectMapper = new ShogunCoreJsonObjectMapper();

    /**
     * Tests whether the JodaModule is registered.
     */
    @Test
    public void testModules() {

        List<Module> modules = ShogunCoreJsonObjectMapper.findModules();

        assertEquals(2, modules.size());

        assertThat(modules.get(1), instanceOf(JtsModule.class));
    }

    /**
     * Tests whether the dateFormat is ISO8601
     */
    @Test
    public void testDateFormat() {

        SerializationConfig serializationConfig = objectMapper.getSerializationConfig();
        DeserializationConfig deserializationConfig = objectMapper.getDeserializationConfig();

        DateFormat serializationDateFormat = serializationConfig.getDateFormat();
        DateFormat deserializationDateFormat = deserializationConfig.getDateFormat();

        assertThat(serializationDateFormat, instanceOf(StdDateFormat.class));
        assertThat(deserializationDateFormat, instanceOf(StdDateFormat.class));
    }

    /**
     * Tests whether the correct TimeZone is set.
     */
    @Test
    public void testTimezone() {

        TimeZone expectedTimeZone = TimeZone.getDefault();

        SerializationConfig serializationConfig = objectMapper.getSerializationConfig();
        DeserializationConfig deserializationConfig = objectMapper.getDeserializationConfig();

        TimeZone serializationTimeZone = serializationConfig.getTimeZone();
        TimeZone deserializationTimeZone = deserializationConfig.getTimeZone();

        assertEquals(expectedTimeZone, serializationTimeZone);
        assertEquals(expectedTimeZone, deserializationTimeZone);
    }

    /**
     * Tests whether the dates are not serialized as timestamps, but as
     * their textual representation.
     */
    @Test
    public void testDateSerialization() {

        SerializationConfig serializationConfig = objectMapper.getSerializationConfig();

        boolean writesDatesAsTimestamps = serializationConfig.isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        assertFalse(writesDatesAsTimestamps);
    }

}
