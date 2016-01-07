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
            colorStack[i] = int2pc(0x000000);
    }
    public Boolean connect(Activity activity) {
        return phi.ConnectMasterDevice(activity);
    }
    public Boolean pushColor(int argb) {
        colorStack[2] = colorStack[1];
        colorStack[1] = colorStack[0];
        // extract R,G,B bytes from ARGB integer
        colorStack[0] = int2pc(argb);
        return render();
    }
    private Boolean render() {
        // 3x2 lines of color separated by 3x1 lines of black
        // LTR, TopTBottom
        int width = 11, height = 9;
        PulseColor[] pixels = new PulseColor[99];
        for (int i=0; i<colorStack.length; i++) {
            for (int k=0; k<2; k++) {
                for (int j = 0; j < width; j++) {
                    pixels[(i + k + 1) * 11 + j] = colorStack[i];
                    pixels[(i + 3) * 11 + j] = int2pc(0x000000);
                }
            }
        }
        return phi.SetColorImage(pixels);
    }
    private PulseColor int2pc(int rgb){
        return new PulseColor(
            (byte) (rgb >> 16),
            (byte) (rgb >> 8),
            (byte) rgb
        );
    }
}
