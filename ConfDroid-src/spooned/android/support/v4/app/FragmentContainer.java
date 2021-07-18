package android.support.v4.app;


/**
 * Callbacks to a {@link Fragment}'s container.
 */
public abstract class FragmentContainer {
    /**
     * Return the view with the given resource ID. May return {@code null} if the
     * view is not a child of this container.
     */
    @android.support.annotation.Nullable
    public abstract android.view.View onFindViewById(@android.support.annotation.IdRes
    int id);

    /**
     * Return {@code true} if the container holds any view.
     */
    public abstract boolean onHasView();
}

