import com.lukeyes.ui.ArtieFrame;
import org.scijava.nativelib.NativeLoader;
import sun.plugin2.util.NativeLibLoader;

import javax.swing.*;
import java.io.IOException;

public class Application {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(
                new Runnable() {
                    @Override
                    public void run() {

                        ArtieFrame.createAndShowGUI();
                    }
                }
        );
    }
}
