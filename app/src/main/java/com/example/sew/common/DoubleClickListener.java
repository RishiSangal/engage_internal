package com.example.sew.common;

import android.view.View;


/**
 * @author pedromassango on 12/20/17.
 * @contributing to development samirtf
 * <p>
 * Interface to be notified from a new click event.
 */
public interface DoubleClickListener {

    /**
     * Called when the user make a single click.
     */
    void onSingleClick(final View view);

    /**
     * Called when the user make a double click.
     */
    void onDoubleClick(final View view);
}