/*
 * Copyright (c) 2016.
 * Modified by Marcelo Benites on 25/08/2016.
 */

package cm.aptoide.pt.billing;

import cm.aptoide.pt.billing.external.ExternalBillingSerializer;
import cm.aptoide.pt.billing.product.InAppPurchase;
import cm.aptoide.pt.billing.product.PaidAppPurchase;
import cm.aptoide.pt.billing.product.SimplePurchase;
import cm.aptoide.pt.dataprovider.model.v3.PaidApp;
import cm.aptoide.pt.dataprovider.ws.v7.billing.GetPurchasesRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.ArrayList;
import java.util.List;

public class PurchaseMapper {

  private final ExternalBillingSerializer serializer;

  public PurchaseMapper(ExternalBillingSerializer serializer) {
    this.serializer = serializer;
  }

  public Purchase map(PaidApp response) {
    return new PaidAppPurchase(response.getPath()
        .getStringPath(), response.getPayment()
        .isPaid() ? SimplePurchase.Status.COMPLETED : SimplePurchase.Status.FAILED,
        response.getPayment()
            .getMetadata()
            .getProductId());
  }

  public List<Purchase> map(List<GetPurchasesRequest.ResponseBody.Purchase> responseList) {

    final List<Purchase> purchases = new ArrayList<>(responseList.size());

    for (GetPurchasesRequest.ResponseBody.Purchase response : responseList) {
      purchases.add(map(response));
    }
    return purchases;
  }

  public Purchase map(GetPurchasesRequest.ResponseBody.Purchase response) {
    try {
      return new InAppPurchase(response.getProduct()
          .getId(), response.getSignature(), serializer.serializePurchase(response.getData()
          .getDeveloperData()), response.getProduct()
          .getSku(), response.getData()
          .getDeveloperData()
          .getPurchaseToken(), response.getData()
          .getDeveloperData()
          .getPurchaseState() == 0 ? SimplePurchase.Status.COMPLETED : SimplePurchase.Status.NEW);
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException(e);
    }
  }
}
