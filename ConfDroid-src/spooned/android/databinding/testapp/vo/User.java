package android.databinding.testapp.vo;


public class User extends android.databinding.BaseObservable {
    @android.databinding.Bindable
    private android.databinding.testapp.vo.User friend;

    @android.databinding.Bindable
    private java.lang.String name;

    @android.databinding.Bindable
    private java.lang.String fullName;

    public android.databinding.testapp.vo.User getFriend() {
        return friend;
    }

    public void setFriend(android.databinding.testapp.vo.User friend) {
        this.friend = friend;
        notifyPropertyChanged(BR.friend);
    }

    public java.lang.String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    public java.lang.String getFullName() {
        return fullName;
    }

    public void setFullName(java.lang.String fullName) {
        this.fullName = fullName;
        notifyPropertyChanged(BR.fullName);
    }
}

