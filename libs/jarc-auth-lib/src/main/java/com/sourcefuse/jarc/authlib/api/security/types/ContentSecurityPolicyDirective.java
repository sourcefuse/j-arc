//package com.sourcefuse.jarc.authlib.helmet.types;
//
//import java.util.Set;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//@AllArgsConstructor
//@NoArgsConstructor
//@Getter
//@Builder
//public class ContentSecurityPolicyDirective {
//
//  private Object defaultSrc;
//  private Object baseUri;
//  private Object fontSrc;
//  private Object formAction;
//  private Object frameAncestors;
//  private Object imgSrc;
//  private Object objectSrc;
//  private Object scriptSrc;
//  private Object scriptSrcAttr;
//  private Object styleSrc;
//  private Object upgradeInsecureRequests;
//
//  public void setDefaultSrc(Object defaultSrc) {
//    checkIfStringOrSet(defaultSrc);
//    this.defaultSrc = defaultSrc;
//  }
//
//  public void setBaseUri(Object baseUri) {
//    checkIfStringOrSet(baseUri);
//    this.baseUri = baseUri;
//  }
//
//  public void setFontSrc(Object fontSrc) {
//    checkIfStringOrSet(fontSrc);
//    this.fontSrc = fontSrc;
//  }
//
//  public void setFormAction(Object formAction) {
//    checkIfStringOrSet(formAction);
//    this.formAction = formAction;
//  }
//
//  public void setFrameAncestors(Object frameAncestors) {
//    checkIfStringOrSet(frameAncestors);
//    this.frameAncestors = frameAncestors;
//  }
//
//  public void setImgSrc(Object imgSrc) {
//    checkIfStringOrSet(imgSrc);
//    this.imgSrc = imgSrc;
//  }
//
//  public void setObjectSrc(Object objectSrc) {
//    checkIfStringOrSet(objectSrc);
//    this.objectSrc = objectSrc;
//  }
//
//  public void setScriptSrc(Object scriptSrc) {
//    checkIfStringOrSet(scriptSrc);
//    this.scriptSrc = scriptSrc;
//  }
//
//  public void setScriptSrcAttr(Object scriptSrcAttr) {
//    checkIfStringOrSet(scriptSrcAttr);
//    this.scriptSrcAttr = scriptSrcAttr;
//  }
//
//  public void setStyleSrc(Object styleSrc) {
//    checkIfStringOrSet(styleSrc);
//    this.styleSrc = styleSrc;
//  }
//
//  public void setUpgradeInsecureRequests(Object upgradeInsecureRequests) {
//    checkIfStringOrSet(upgradeInsecureRequests);
//    this.upgradeInsecureRequests = upgradeInsecureRequests;
//  }
//
//  public boolean checkIfStringOrSet(Object source) {
//    StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
//    String callingMethod = stackTrace[2].getMethodName();
//    if (source instanceof String) {
//      return true;
//    }
//    if (
//      source instanceof Set &&
//      ((Set<?>) source).stream().allMatch(ele -> ele instanceof String)
//    ) {
//      return true;
//    }
//    throw new IllegalArgumentException(
//      callingMethod + " only accepts String or Set<String>"
//    );
//  }
//}