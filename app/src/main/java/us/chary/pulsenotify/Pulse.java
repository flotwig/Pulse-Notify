package us.chary.pulsenotify;

import android.app.Activity;
import android.util.Log;

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
    public PulseHandlerInterface phi;
    private PulseColor[] colorStack;
    final private String LOG_TAG = "PulseEvent";
    public Pulse() {
        phi = new ImplementPulseHandler();
        colorStack = new PulseColor[3]; // 0 highest
        for(int i=0; i<colorStack.length; i++)
            colorStack[i] = int2pc(0x000000);
        final Pulse pulse = this;
        phi.registerPulseNotifiedListener(new PulseNotifiedListener() {
            @Override
            public void onConnectMasterDevice(){
                Log.i(LOG_TAG, "onConnectMasterDevice");
                pulse.pushColor(0xff0000);
                pulse.pushColor(0xff00ff);
                pulse.pushColor(0x00ff00);

            }
            @Override
            public void onDisconnectMasterDevice(){
                Log.i(LOG_TAG, "onDisconnectMasterDevice");
            }

            @Override
            public void onLEDPatternChanged(PulseThemePattern pattern){
                Log.i(LOG_TAG, "onLEDPatternChanged");
            }

            @Override
            public void onRetBrightness(int brightness){
                Log.i(LOG_TAG, "onRetBrightness");
            }

            @Override
            public void onRetSetDeviceInfo(boolean ret){
                Log.i(LOG_TAG, "onRetSetDeviceInfo");
            }

            @Override
            public void onRetRequestDeviceInfo(DeviceModel[] deviceModel){
                Log.i(LOG_TAG, "onRetRequestDeviceInfo");
            }


            @Override
            public void onRetSetLEDPattern(boolean ret){
                Log.i(LOG_TAG, "onRetSetLEDPattern");
            }


            @Override
            public void onRetGetLEDPattern(PulseThemePattern pattern){
                Log.i(LOG_TAG, "onRetGetLEDPattern");
            }

            @Override
            public void onSoundEvent(int soundLevel){
                Log.i(LOG_TAG, "onSoundEvent: " + soundLevel);
            }

            @Override
            public void onRetCaptureColor(PulseColor capturedColor){
                Log.i(LOG_TAG, "onRetCaptureColor");
            }

            @Override
            public void onRetCaptureColor(byte red, byte green, byte blue){
                Log.i(LOG_TAG, "onRetCaptureColor");
            }
        });
    }
    public Boolean connect(Activity activity) {
        return phi.ConnectMasterDevice(activity);
    }
    public Boolean pushColor(int argb) {
        colorStack[2] = colorStack[1];
        colorStack[1] = colorStack[0];
        // extract R,G,B bytes from ARGB integer
        colorStack[0] = int2pc(argb);
        Log.i(LOG_TAG,"Color pushed: " + argb);
        return render();
    }
    public Boolean pushColor(int argb, int index) {
        colorStack[index] = int2pc(argb);
        return render();
    }
    public Boolean removeColor(int argb) {
        for(int i=0; i<colorStack.length; i++){
            if((colorStack[i].red == ((byte) (argb >> 16))) &&
               (colorStack[i].green == ((byte) (argb >> 8))) &&
               (colorStack[i].blue == ((byte) (argb)))) {
                return pushColor(0x000000, i);
                // TODO make remaining colors move up
            }
        }
    }
    private Boolean render() {
        // 3x2 lines of color separated by 3x1 lines of black
        // LTR, TopTBottom
        int width = 11, height = 9;
        PulseColor[] pixels = new PulseColor[99];
        for (int i=0; i<99; i++)
            pixels[i] = int2pc(0xffffff);
        for (int c=0; c<colorStack.length; c++)
            for (int i=0; i<height/colorStack.length; i++)
                for (int x=0; x<width; x++)
                    pixels[c*3*11 + 11*i + x] = colorStack[c];
        return phi.SetColorImage(pixels);
    }
    public PulseColor int2pc(int rgb){
        return new PulseColor(
            (byte) (rgb >> 16),
            (byte) (rgb >> 8),
            (byte) rgb
        );
    }
}
