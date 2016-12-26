package cm.aptoide.pt.dataprovider.ws.v7.store;

import cm.aptoide.pt.dataprovider.ws.BaseBodyDecorator;
import cm.aptoide.pt.dataprovider.ws.v7.BaseBody;
import cm.aptoide.pt.dataprovider.ws.v7.Endless;
import cm.aptoide.pt.dataprovider.ws.v7.V7;
import cm.aptoide.pt.model.v7.store.ListStores;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import rx.Observable;

/**
 * Created by trinkes on 12/12/2016.
 */

public class GetMyStoreListRequest extends V7<ListStores, GetMyStoreListRequest.Body> {

  private final String url;

  public GetMyStoreListRequest(String url, Body body, String baseHost) {
    super(body, baseHost);
    this.url = url;
  }

  public static GetMyStoreListRequest of(String url, String accessToken, String aptoideClientUuid) {
    BaseBodyDecorator decorator = new BaseBodyDecorator(aptoideClientUuid);
    return new GetMyStoreListRequest(url,
        (Body) decorator.decorate(new Body(WidgetsArgs.createDefault()), accessToken), BASE_HOST);
  }

  @Override
  protected Observable<ListStores> loadDataFromNetwork(Interfaces interfaces, boolean bypassCache) {
    if (url.contains("getSubscribed")) {
      body.setRefresh(bypassCache);
    }
    return interfaces.getMyStoreList(url, body, bypassCache);
  }

  @EqualsAndHashCode(callSuper = true) public static class Body extends BaseBody
      implements Endless {

    @Getter private Integer limit = 25;
    @Getter @Setter private int offset;
    @Getter private WidgetsArgs widgetsArgs;
    @Getter @Setter private boolean refresh;

    public Body(WidgetsArgs widgetsArgs) {
      super();
      this.widgetsArgs = widgetsArgs;
    }
  }
}
