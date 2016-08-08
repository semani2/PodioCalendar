package development.sai.podiocalendar.sdk;

import android.content.Context;

import com.podio.sdk.Podio;

/**
 * Created by sai on 8/1/16.
 */
public class SuperPodio extends Podio {
    public static void setup(Context context, String clientId, String clientSecret) {
        Podio.setup(context, clientId, clientSecret);
    }
}
