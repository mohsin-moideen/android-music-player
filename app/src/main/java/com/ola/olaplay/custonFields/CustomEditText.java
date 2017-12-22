package com.ola.olaplay.custonFields;

import android.content.Context;
import android.util.AttributeSet;
import android.support.v7.widget.AppCompatEditText;
import android.view.KeyEvent;

/**
 * Created by MohsinM on 17-12-2017.
 *
 * Custom EditView to erase content and clear focus on back button press
 */

public class CustomEditText extends AppCompatEditText
{
    public CustomEditText( Context context )
    {
        super( context );
    }

    public CustomEditText( Context context, AttributeSet attribute_set )
    {
        super( context, attribute_set );
    }

    public CustomEditText( Context context, AttributeSet attribute_set, int def_style_attribute )
    {
        super( context, attribute_set, def_style_attribute );
    }

    @Override
    public boolean onKeyPreIme( int key_code, KeyEvent event )
    {
        if ( event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP )
            this.clearFocus();

        return super.onKeyPreIme( key_code, event );
    }
}