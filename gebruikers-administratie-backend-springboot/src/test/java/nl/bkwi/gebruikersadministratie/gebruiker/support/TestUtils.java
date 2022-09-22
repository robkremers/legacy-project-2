package nl.bkwi.gebruikersadministratie.gebruiker.support;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.util.Assert;

public class TestUtils {

  public static Consumer<DataBuffer> assertDataBufferEquals(String expected) {
    // maakt een consumer van een DataBuffer die assert dat de String die in de DataBuffer zit
    // gelijk is aan de expected String.
    return dataBuffer -> {
      String actual = dumpString(dataBuffer, StandardCharsets.UTF_8);
      org.junit.Assert.assertEquals(expected, actual);
      DataBufferUtils.release(dataBuffer);
    };
  }

  private static String dumpString(DataBuffer buffer, Charset charset) {
    Assert.notNull(charset, "'charset' must not be null");
    byte[] bytes = dumpBytes(buffer);
    return new String(bytes, charset);
  }

  private static byte[] dumpBytes(DataBuffer buffer) {
    Assert.notNull(buffer, "'buffer' must not be null");
    byte[] bytes = new byte[buffer.readableByteCount()];
    buffer.read(bytes);
    return bytes;
  }
}
