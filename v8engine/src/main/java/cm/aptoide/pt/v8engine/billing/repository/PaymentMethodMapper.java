/*
 * Copyright (c) 2016.
 * Modified by Marcelo Benites on 11/08/2016.
 */

package cm.aptoide.pt.v8engine.billing.repository;

import cm.aptoide.pt.dataprovider.model.v3.PaymentServiceResponse;
import cm.aptoide.pt.v8engine.billing.Payer;
import cm.aptoide.pt.v8engine.billing.PaymentMethod;
import cm.aptoide.pt.v8engine.billing.services.AptoidePaymentMethod;
import cm.aptoide.pt.v8engine.billing.services.BoaCompraPaymentMethod;
import cm.aptoide.pt.v8engine.billing.services.PayPalPaymentMethod;

public class PaymentMethodMapper {

  public static final String PAYPAL = "paypal";
  public static final String BOACOMPRA = "boacompra";
  public static final String BOACOMPRAGOLD = "boacompragold";
  public static final String DUMMY = "dummy";

  private final PaymentRepositoryFactory paymentRepositoryFactory;
  private final AuthorizationRepository authorizationRepository;
  private final AuthorizationFactory authorizationFactory;
  private final Payer payer;

  public PaymentMethodMapper(PaymentRepositoryFactory paymentRepositoryFactory,
      AuthorizationRepository authorizationRepository, AuthorizationFactory authorizationFactory,
      Payer payer) {
    this.paymentRepositoryFactory = paymentRepositoryFactory;
    this.authorizationRepository = authorizationRepository;
    this.authorizationFactory = authorizationFactory;
    this.payer = payer;
  }

  public PaymentMethod map(PaymentServiceResponse paymentService) {
    switch (paymentService.getShortName()) {
      case PAYPAL:
        return new PayPalPaymentMethod(paymentService.getId(), paymentService.getName(),
            paymentService.getDescription(), paymentRepositoryFactory);
      case BOACOMPRA:
      case BOACOMPRAGOLD:
        return new BoaCompraPaymentMethod(paymentService.getId(), paymentService.getName(),
            paymentService.getDescription(), paymentRepositoryFactory, payer,
            authorizationRepository, authorizationFactory);
      case DUMMY:
        return new AptoidePaymentMethod(paymentService.getId(), paymentService.getName(),
            paymentService.getDescription(), paymentRepositoryFactory);
      default:
        throw new IllegalArgumentException(
            "Payment not supported: " + paymentService.getShortName());
    }
  }
}