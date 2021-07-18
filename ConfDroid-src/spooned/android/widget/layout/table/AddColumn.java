/**
 * Copyright (C) 2007 The Android Open Source Project
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
package android.widget.layout.table;


/**
 * This test adds an extra row with an extra column in the table.
 */
public class AddColumn extends android.app.Activity {
    @java.lang.Override
    protected void onCreate(android.os.Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.add_column_in_table);
        final android.widget.Button addRowButton = ((android.widget.Button) (findViewById(R.id.add_row_button)));
        addRowButton.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(android.view.View v) {
                final android.widget.TableLayout table = ((android.widget.TableLayout) (findViewById(R.id.table)));
                final android.widget.TableRow newRow = new android.widget.TableRow(android.widget.layout.table.AddColumn.this);
                for (int i = 0; i < 4; i++) {
                    final android.widget.TextView view = new android.widget.TextView(android.widget.layout.table.AddColumn.this);
                    view.setText("Column " + (i + 1));
                    view.setPadding(3, 3, 3, 3);
                    newRow.addView(view, new android.widget.TableRow.LayoutParams());
                }
                table.addView(newRow, new android.widget.TableLayout.LayoutParams());
                newRow.requestLayout();
            }
        });
    }
}

