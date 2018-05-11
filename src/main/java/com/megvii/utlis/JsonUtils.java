package com.megvii.utlis;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.Assert;

public class JsonUtils {



  /**
   * ObjectMapper
   */
  private static ObjectMapper mapper = new ObjectMapper();

  /**
   * 不可实例化
   */
  private JsonUtils() {
  }


  /**
   * 将对象转换为JSON字符串
   *
   * @param value 对象
   * @return JSOn字符串
   */
  public static String toJson(Object value) {
    try {
      return mapper.writeValueAsString(value);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 将JSON字符串转换为对象
   *
   * @param json JSON字符串
   * @param valueType 对象类型
   * @return 对象
   */
  public static <T> T toObject(String json, Class<T> valueType) {
    Assert.hasText(json, "json字符串不能为空");
    Assert.notNull(valueType, "转换对象类型不能为空");
    try {
      mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
      return mapper.readValue(json, valueType);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * 将JSON字符串转换为对象
   *
   * @param json JSON字符串
   * @param valueType 对象类型
   * @return 对象
   */
  public static Object toObject(String json, String valueType) {
    Assert.hasText(json, "json字符串不能为空");
    Assert.notNull(valueType, "转换对象类型不能为空");
    try {
      return mapper.readValue(json, Class.forName(valueType));
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }



}
