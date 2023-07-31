package com.basic.example.facadeserviceexample.constants;

public final class EmailConstants {

  public static final String INVITATION_MAIL_TEMPLATE =
    "<!doctype html><html><head><meta name='viewport'" +
    " content='width=device-width, initial-scale=1.0' /><meta http-equiv='Content-Type'" +
    " content='text/html; charset=UTF-8' /><title>Invitation Email</title><style>body " +
    "{background-color: #f6f6f6;font-family: sans-serif;-webkit-font-smoothing: antialiased;font-size:" +
    " 14px;line-height: 1.4;margin: 0;padding: 0;-ms-text-size-adjust: 100%;-webkit-text-size-adjust:" +
    " 100%;}.body {background-color: #f6f6f6;width: 100%;}.container {display: block;margin: 0 auto" +
    " !important;max-width: 580px;padding: 10px;width: 580px;}.btn {color: #fff;background-color: " +
    "#337ab7;border-color: #2e6da4;border-radius: 5px;padding: 10px;text-decoration: unset;}.btn:" +
    "hover {opacity: 0.7;}</style></head><body class='body'><table class='container'><tr><td><table " +
    "border='0' cellpadding='0' cellspacing='0'><tr><td><p>Hi there,</p><p>Greeting , You have been " +
    "invited by %s, Please click on the below link to register yourself </p><br /><a class='btn' " +
    "href='%s' target='_blank'>Call To Action</a><br /><br /><p>Good luck! </p></td></tr>" +
    "</table></td></tr></table></body></html>";
}
