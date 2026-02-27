package com.pgms.shared.exception;

import org.springframework.http.HttpStatus;

public class SubscriptionInactiveException extends PgmsException {

  public SubscriptionInactiveException() {
    super("SUBSCRIPTION_SUSPENDED",
      "Your subscription is suspended.",
      HttpStatus.FORBIDDEN);
  }
}