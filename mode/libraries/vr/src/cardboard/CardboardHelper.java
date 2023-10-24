package cardboard;
import android.util.DisplayMetrics;
public class CardboardHelper {
    private native DisplayMetrics getDisplayMetrics();
    public static void main(String[] args) {
        new CardboardHelper().getDisplayMetrics();
    }
}
