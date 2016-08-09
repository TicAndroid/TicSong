package eu.jpark.ticsong.utils;

import com.google.android.gms.common.Scopes;
import com.google.api.services.youtube.YouTubeScopes;

/**
 * Created by heyhe on 2016-07-30.
 */
public class Auth {
    // Register an API key here: https://console.developers.google.com
    public static final String KEY = "AIzaSyCBiSFM8HZgFDzMI-nvP9KHLYz-63mMEuA";
    public static final String[] SCOPES = {Scopes.PROFILE, YouTubeScopes.YOUTUBE};
}