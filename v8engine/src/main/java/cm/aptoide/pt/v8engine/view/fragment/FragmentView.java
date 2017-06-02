package cm.aptoide.pt.v8engine.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import cm.aptoide.pt.logger.Logger;
import cm.aptoide.pt.v8engine.NavigationProvider;
import cm.aptoide.pt.v8engine.presenter.Presenter;
import cm.aptoide.pt.v8engine.presenter.View;
import cm.aptoide.pt.v8engine.view.MainActivity;
import cm.aptoide.pt.v8engine.view.leak.LeakFragment;
import cm.aptoide.pt.v8engine.view.navigator.ActivityNavigator;
import cm.aptoide.pt.v8engine.view.navigator.FragmentNavigator;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.RxLifecycle;
import com.trello.rxlifecycle.android.FragmentEvent;
import rx.Observable;

public abstract class FragmentView extends LeakFragment implements View {

  private static final String TAG = FragmentView.class.getName();

  private Presenter presenter;
  private NavigationProvider navigationProvider;

  public FragmentNavigator getFragmentNavigator() {
    return navigationProvider.getFragmentNavigator();
  }

  public ActivityNavigator getActivityNavigator() {
    return navigationProvider.getActivityNavigator();
  }

  public FragmentNavigator getFragmentChildNavigator(@IdRes int containerId) {
    return new FragmentNavigator(getChildFragmentManager(), containerId, android.R.anim.fade_in,
        android.R.anim.fade_out);
  }

  @Override public void onAttach(Activity activity) {
    super.onAttach(activity);
    try {
      navigationProvider = (NavigationProvider) activity;
    } catch (ClassCastException ex) {
      Logger.e(TAG, String.format("Parent activity must implement %s interface",
          NavigationProvider.class.getName()));
    }
  }

  @Override public void onSaveInstanceState(Bundle outState) {
    if (presenter != null) {
      presenter.saveState(outState);
    } else {
      Logger.w(this.getClass()
          .getName(), "No presenter was attached.");
    }

    super.onSaveInstanceState(outState);
  }

  @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
  }

  @Override public void onPrepareOptionsMenu(Menu menu) {
    super.onPrepareOptionsMenu(menu);
  }

  /**
   * Do not override this method in fragments to handle back stack navigation, before deciding if
   * toolbar menu items should be handled in activity or the fragment.
   *
   * The back navigation menu item selection handling is currently done in the {@link
   * MainActivity}.
   */
  @Override public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      getFragmentNavigator().popBackStack();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @NonNull @Override
  public final <T> LifecycleTransformer<T> bindUntilEvent(@NonNull LifecycleEvent lifecycleEvent) {
    return RxLifecycle.bindUntilEvent(getLifecycle(), lifecycleEvent);
  }

  @Override public Observable<LifecycleEvent> getLifecycle() {
    return lifecycle().flatMap(event -> convertToEvent(event));
  }

  @Override public void attachPresenter(Presenter presenter, Bundle savedInstanceState) {
    if (savedInstanceState != null) {
      presenter.restoreState(savedInstanceState);
    }
    this.presenter = presenter;
    this.presenter.present();
  }

  @NonNull private Observable<LifecycleEvent> convertToEvent(FragmentEvent event) {
    switch (event) {
      case ATTACH:
      case CREATE:
        return Observable.empty();
      case CREATE_VIEW:
        return Observable.just(LifecycleEvent.CREATE);
      case START:
        return Observable.just(LifecycleEvent.START);
      case RESUME:
        return Observable.just(LifecycleEvent.RESUME);
      case PAUSE:
        return Observable.just(LifecycleEvent.PAUSE);
      case STOP:
        return Observable.just(LifecycleEvent.STOP);
      case DESTROY_VIEW:
        return Observable.just(LifecycleEvent.DESTROY);
      case DETACH:
      case DESTROY:
        return Observable.empty();
      default:
        throw new IllegalStateException("Unrecognized event: " + event.name());
    }
  }
}
