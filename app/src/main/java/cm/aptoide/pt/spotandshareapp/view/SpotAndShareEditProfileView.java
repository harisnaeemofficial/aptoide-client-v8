package cm.aptoide.pt.spotandshareapp.view;

import cm.aptoide.pt.presenter.View;
import cm.aptoide.pt.spotandshareapp.SpotAndShareUser;
import java.util.List;
import rx.Observable;

/**
 * Created by filipe on 21-06-2017.
 */

public interface SpotAndShareEditProfileView extends View {

  void finish();

  Observable<SpotAndShareUser> saveProfileChanges();

  Observable<SpotAndShareAvatar> onSelectedAvatar();

  void goBack();

  void selectAvatar(SpotAndShareAvatar avatar);

  void setAvatarsList(List<SpotAndShareAvatar> list);
}