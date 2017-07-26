package cm.aptoide.pt.v8engine.billing.view.boacompra;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import cm.aptoide.accountmanager.AptoideAccountManager;
import cm.aptoide.pt.v8engine.V8Engine;
import cm.aptoide.pt.v8engine.billing.Billing;
import cm.aptoide.pt.v8engine.billing.BillingAnalytics;
import cm.aptoide.pt.v8engine.billing.BillingSyncScheduler;
import cm.aptoide.pt.v8engine.billing.view.BillingNavigator;
import cm.aptoide.pt.v8engine.billing.view.PaymentThrowableCodeMapper;
import cm.aptoide.pt.v8engine.billing.view.ProductProvider;
import cm.aptoide.pt.v8engine.billing.view.PurchaseBundleMapper;
import cm.aptoide.pt.v8engine.billing.view.WebView;
import cm.aptoide.pt.v8engine.billing.view.WebViewFragment;

public class BoaCompraFragment extends WebViewFragment implements WebView {

  private static final String EXTRA_PAYMENT_METHOD_ID =
      "cm.aptoide.pt.v8engine.billing.view.extra.PAYMENT_METHOD_ID";

  private Billing billing;
  private BillingAnalytics billingAnalytics;
  private BillingSyncScheduler billingSyncScheduler;
  private ProductProvider productProvider;
  private int paymentMethodId;
  private AptoideAccountManager accountManager;

  public static Fragment create(Bundle bundle, int paymentMethodId) {
    final BoaCompraFragment fragment = new BoaCompraFragment();
    bundle.putInt(EXTRA_PAYMENT_METHOD_ID, paymentMethodId);
    fragment.setArguments(bundle);
    return fragment;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    paymentMethodId = getArguments().getInt(BoaCompraFragment.EXTRA_PAYMENT_METHOD_ID);
    billing = ((V8Engine) getContext().getApplicationContext()).getBilling();
    billingAnalytics = ((V8Engine) getContext().getApplicationContext()).getBillingAnalytics();
    billingSyncScheduler =
        ((V8Engine) getContext().getApplicationContext()).getBillingSyncScheduler();
    productProvider = ProductProvider.fromBundle(billing, getArguments());
    accountManager = ((V8Engine) getContext().getApplicationContext()).getAccountManager();
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    attachPresenter(new BoaCompraPresenter(this, billing, billingAnalytics, billingSyncScheduler,
            productProvider,
            new BillingNavigator(new PurchaseBundleMapper(new PaymentThrowableCodeMapper()),
                getActivityNavigator(), getFragmentNavigator(), accountManager), paymentMethodId),
        savedInstanceState);
  }
}