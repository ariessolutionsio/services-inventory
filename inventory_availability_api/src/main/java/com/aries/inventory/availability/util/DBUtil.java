package com.aries.inventory.availability.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

public class DBUtil {

  private DBUtil(){}
  public static String performHashing(String skuId, String node) {
    return DigestUtils.md5Hex(skuId.concat(StringUtils.isBlank(node) ? "default" : node));
  }
}
