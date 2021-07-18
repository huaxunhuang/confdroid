/**
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package android.databinding.testapp.vo;


public class ListenerBindingObject {
    public static int lastClick = 0;

    public boolean inflateCalled;

    private final android.content.Context mContext;

    public boolean wasRunnableRun;

    public final android.databinding.ObservableBoolean clickable = new android.databinding.ObservableBoolean();

    public final android.databinding.ObservableBoolean useOne = new android.databinding.ObservableBoolean();

    public ListenerBindingObject(android.content.Context context) {
        clickable.set(true);
        this.mContext = context;
    }

    public void onMovedToScrapHeap(android.view.View view) {
    }

    public void onScroll(android.widget.AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
    }

    public void onScrollStateChanged(android.widget.AbsListView view, int scrollState) {
    }

    public boolean onMenuItemClick(android.view.MenuItem item) {
        return false;
    }

    public void onItemClick(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
    }

    public boolean onItemLongClick(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
        return true;
    }

    public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
    }

    public void onNothingSelected(android.widget.AdapterView<?> parent) {
    }

    public void onDismiss() {
    }

    public java.lang.CharSequence fixText(java.lang.CharSequence invalidText) {
        return invalidText;
    }

    public boolean isValid(java.lang.CharSequence text) {
        return true;
    }

    public void onSelectedDayChange(android.widget.CalendarView view, int year, int month, int dayOfMonth) {
    }

    public void onChronometerTick(android.widget.Chronometer chronometer) {
    }

    public void onCheckedChanged(android.widget.CompoundButton buttonView, boolean isChecked) {
    }

    public boolean onChildClick(android.widget.ExpandableListView parent, android.view.View v, int groupPosition, int childPosition, long id) {
        return false;
    }

    public boolean onGroupClick(android.widget.ExpandableListView parent, android.view.View v, int groupPosition, long id) {
        return false;
    }

    public void onGroupCollapse(int groupPosition) {
    }

    public void onGroupExpand(int groupPosition) {
    }

    public java.lang.String format(int value) {
        return null;
    }

    public void onValueChange(android.widget.NumberPicker picker, int oldVal, int newVal) {
    }

    public void onScrollStateChange(android.widget.NumberPicker view, int scrollState) {
    }

    public void onCheckedChanged(android.widget.RadioGroup group, int checkedId) {
    }

    public void onRatingChanged(android.widget.RatingBar ratingBar, float rating, boolean fromUser) {
    }

    public boolean onClose() {
        return false;
    }

    public boolean onQueryTextChange(java.lang.String newText) {
        return false;
    }

    public boolean onQueryTextSubmit(java.lang.String query) {
        return false;
    }

    public boolean onSuggestionClick(int position) {
        return false;
    }

    public boolean onSuggestionSelect(int position) {
        return false;
    }

    public void onProgressChanged(android.widget.SeekBar seekBar, int progress, boolean fromUser) {
    }

    public void onStartTrackingTouch(android.widget.SeekBar seekBar) {
    }

    public void onStopTrackingTouch(android.widget.SeekBar seekBar) {
    }

    public void onTabChanged(java.lang.String tabId) {
    }

    public boolean onEditorAction(android.widget.TextView v, int actionId, android.view.KeyEvent event) {
        return false;
    }

    public void afterTextChanged(android.text.Editable s) {
    }

    public void beforeTextChanged(java.lang.CharSequence s, int start, int count, int after) {
    }

    public void onTextChanged(java.lang.CharSequence s, int start, int before, int count) {
    }

    public void onTimeChanged(android.widget.TimePicker view, int hourOfDay, int minute) {
    }

    public void onClick(android.view.View view) {
    }

    public void onCompletion(android.media.MediaPlayer mp) {
    }

    public boolean onError(android.media.MediaPlayer mp, int what, int extra) {
        return true;
    }

    public boolean onInfo(android.media.MediaPlayer mp, int what, int extra) {
        return true;
    }

    public void onPrepared(android.media.MediaPlayer mp) {
    }

    public android.view.WindowInsets onApplyWindowInsets(android.view.View v, android.view.WindowInsets insets) {
        return null;
    }

    public void onCreateContextMenu(android.view.ContextMenu menu, android.view.View v, android.view.ContextMenu.ContextMenuInfo menuInfo) {
    }

    public boolean onDrag(android.view.View v, android.view.DragEvent event) {
        return true;
    }

    public void onFocusChange(android.view.View v, boolean hasFocus) {
    }

    public boolean onGenericMotion(android.view.View v, android.view.MotionEvent event) {
        return true;
    }

    public boolean onHover(android.view.View v, android.view.MotionEvent event) {
        return true;
    }

    public boolean onKey(android.view.View v, int keyCode, android.view.KeyEvent event) {
        return true;
    }

    public boolean onLongClick(android.view.View v) {
        return true;
    }

    public void onSystemUiVisibilityChange(int visibility) {
    }

    public boolean onTouch(android.view.View v, android.view.MotionEvent event) {
        return true;
    }

    public void getOutline(android.view.View view, android.graphics.Outline outline) {
    }

    public void onViewAttachedToWindow(android.view.View v) {
    }

    public void onViewDetachedFromWindow(android.view.View v) {
    }

    public void onChildViewAdded(android.view.View parent, android.view.View child) {
    }

    public void onChildViewRemoved(android.view.View parent, android.view.View child) {
    }

    public void onAnimationEnd(android.view.animation.Animation animation) {
    }

    public void onAnimationRepeat(android.view.animation.Animation animation) {
    }

    public void onAnimationStart(android.view.animation.Animation animation) {
    }

    public void onInflate(android.view.ViewStub stub, android.view.View inflated) {
        inflateCalled = true;
    }

    public android.view.View makeView() {
        return new android.view.View(mContext);
    }

    public void onClick1(android.view.View view) {
        android.databinding.testapp.vo.ListenerBindingObject.lastClick = 1;
    }

    public static void onClick2(android.view.View view) {
        android.databinding.testapp.vo.ListenerBindingObject.lastClick = 2;
    }

    public void onClick3(android.view.View view) {
        android.databinding.testapp.vo.ListenerBindingObject.lastClick = 3;
    }

    public static void onClick4(android.view.View view) {
        android.databinding.testapp.vo.ListenerBindingObject.lastClick = 4;
    }

    public void runnableRun() {
        this.wasRunnableRun = true;
    }

    public void onFoo() {
    }

    public void onBar() {
    }

    public boolean onBar(android.view.View view) {
        return true;
    }

    public static class Inner extends android.databinding.BaseObservable {
        public boolean clicked;

        public void onClick(android.view.View view) {
            clicked = true;
        }
    }
}

