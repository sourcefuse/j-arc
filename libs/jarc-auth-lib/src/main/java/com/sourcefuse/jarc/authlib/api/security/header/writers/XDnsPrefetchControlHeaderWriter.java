package com.sourcefuse.jarc.authlib.api.security.header.writers;

import com.sourcefuse.jarc.authlib.api.security.config.XDnsPrefetchControlConfigOptions;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.header.HeaderWriter;

public final class XDnsPrefetchControlHeaderWriter implements HeaderWriter {

  private static final String X_DNS_PREFETCH_CONTROL_HEADER =
    "X-DNS-Prefetch-Control";

  private String headerValue;

  public XDnsPrefetchControlHeaderWriter() {
    headerValue = fetchHeaderValueFromOptions(null);
  }

  public XDnsPrefetchControlHeaderWriter(
    XDnsPrefetchControlConfigOptions options
  ) {
    headerValue = fetchHeaderValueFromOptions(options);
  }

  @Override
  public void writeHeaders(
    HttpServletRequest request,
    HttpServletResponse response
  ) {
    if (!response.containsHeader(X_DNS_PREFETCH_CONTROL_HEADER)) {
      response.setHeader(X_DNS_PREFETCH_CONTROL_HEADER, this.headerValue);
    }
  }

  public void setHeaderValue(XDnsPrefetchControlConfigOptions options) {
    this.headerValue = fetchHeaderValueFromOptions(options);
  }

  private static String fetchHeaderValueFromOptions(
    XDnsPrefetchControlConfigOptions options
  ) {
    return (options != null && options.isAllow()) ? "on" : "off";
  }

  @Override
  public String toString() {
    return getClass().getName() + " [headerValue=" + this.headerValue + "]";
  }
}
