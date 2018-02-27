/**
 * 
 */
package savvy.example.tapan.inandout.com.example.tapan.app;

import android.content.Context;
import android.graphics.Typeface;

/**
 * @author Tapan
 *
 */
public class FontManager {
	 
    public static final String ROOT = "fonts/", FONTAWESOME = ROOT + "fontawesome-webfont.ttf";
     
    public static Typeface getTypeface(Context context, String font) {
        return Typeface.createFromAsset(context.getAssets(), font);
    }    
 
}