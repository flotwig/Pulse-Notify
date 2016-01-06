package us.chary.pulsenotify;

import android.app.Activity;

import com.harman.pulsesdk.DeviceModel;
import com.harman.pulsesdk.PulseColor;
import com.harman.pulsesdk.PulseThemePattern;
import com.harman.pulsesdk.PulseHandlerInterface;
import com.harman.pulsesdk.PulseNotifiedListener;
import com.harman.pulsesdk.ImplementPulseHandler;

/**
 * Created by Zach on 1/6/2016.
 */
public class Pulse {
    private PulseHandlerInterface phi;
    private PulseColor[] colorStack;
    public Pulse() {
        phi = new ImplementPulseHandler();
        colorStack = new PulseColor[3]; // 0 highest
        for(int i=0; i<colorStack.length; i++)
            colorStack[i] = new PulseColor((byte)0,(byte)0,(byte)0);
    }
    public Boolean connect(Activity activity) {
        return phi.ConnectMasterDevice(activity);
    }
    public Boolean pushColor(int argb) {
        colorStack[2] = colorStack[1];
        colorStack[1] = colorStack[0];
        // extract R,G,B bytes from ARGB integer
        colorStack[0] = new PulseColor(
                (byte) (argb >> 16),
                (byte) (argb >> 8),
                (byte) argb
        );
        return render();
    }
    private Boolean render() {
        // 3x3 lines of color separated by 2x1 lines of black
        // LTR, TopTBottom
        int width = 11, height = 9;
        PulseColor[] pixels = new PulseColor[99];
        for (int i=0; i<colorStack.length; i++) {
            for (int k=0; k<3; k++)
                for (int j=0; j<width; j++)
                    pixels[(i+k+1)*11 + j] = colorStack[i];
            if (i<colorStack.length-1)
                for (int j=0; j<width; j++)
                    pixels[(i+4)*11 + j] = new PulseColor((byte)0,(byte)0,(byte)0);
        }
        return phi.SetColorImage(pixels);
    }
}
