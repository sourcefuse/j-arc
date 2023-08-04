package com.sourcefuse.jarc.core.utils;

import java.beans.PropertyDescriptor;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Component;

@Component
public final class CommonUtils {

  private CommonUtils() {}

  public static String[] getNullPropertyNames(Object source) {
    final BeanWrapper src = new BeanWrapperImpl(source);
    PropertyDescriptor[] pds = src.getPropertyDescriptors();
    Set<String> emptyNames = new HashSet<>();
    for (PropertyDescriptor pd : pds) {
      Object srcValue = src.getPropertyValue(pd.getName());
      if (srcValue == null) {
        emptyNames.add(pd.getName());
      }
    }
    String[] result = new String[emptyNames.size()];
    return emptyNames.toArray(result);
  }

  public static boolean calculateTimeDiff(LocalDateTime savedDateTime) {
    final int EXPIRY_IN_MINUTES = 20;
    LocalDateTime currentDateTime = LocalDateTime.now();
    Duration difference = Duration.between(savedDateTime, currentDateTime);
    long differenceInMinutes = difference.toMinutes();
    return differenceInMinutes <= EXPIRY_IN_MINUTES;
  }
}
