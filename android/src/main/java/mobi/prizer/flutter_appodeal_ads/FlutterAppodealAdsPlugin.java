package mobi.prizer.flutter_appodeal_ads;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.appodeal.ads.Appodeal;
import com.appodeal.ads.InterstitialCallbacks;
import com.appodeal.ads.RewardedVideoCallbacks;
import com.appodeal.ads.api.App;

import java.util.HashMap;
import java.util.Map;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/** FlutterAppodealAdsPlugin */
public class FlutterAppodealAdsPlugin implements FlutterPlugin, MethodCallHandler, ActivityAware {
  private MethodChannel channel;
  private FlutterPluginBinding binding = null;
  private Activity activity = null;

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding binding) {
    channel = new MethodChannel(binding.getBinaryMessenger(), "flutter_appodeal_ads");
    channel.setMethodCallHandler(this);
    this.binding = binding;
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
    this.binding = null;
  }

  @Override
  public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
    this.activity = binding.getActivity();
  }

  @Override
  public void onDetachedFromActivityForConfigChanges() {

  }

  @Override
  public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {

  }

  @Override
  public void onDetachedFromActivity() {
    this.activity = null;
  }

  public void init(Activity activity, String appKey, boolean testMode){
    Appodeal.disableLocationPermissionCheck();
    Appodeal.disableWriteExternalStoragePermissionCheck();
    Appodeal.disableWebViewCacheClear();
    Appodeal.initialize(activity, appKey, Appodeal.INTERSTITIAL | Appodeal.REWARDED_VIDEO);
    Appodeal.setTesting(testMode);
    Appodeal.setAutoCache(Appodeal.INTERSTITIAL, false);
    Appodeal.setAutoCache(Appodeal.REWARDED_VIDEO, false);
  }

  public void showInterstitial(Activity activity){
    Appodeal.show(activity, Appodeal.INTERSTITIAL);
  }

  public void cacheInterstitial(Activity activity){
    Appodeal.setInterstitialCallbacks(new InterstitialCallbacks() {
      @Override
      public void onInterstitialLoaded(boolean isPrecache) {
        channel.invokeMethod("onLoaded", null);
      }
      @Override
      public void onInterstitialFailedToLoad() {
        channel.invokeMethod("onFailedToLoad", null);
      }
      @Override
      public void onInterstitialShown() {
        channel.invokeMethod("onShown", null);
      }
      @Override
      public void onInterstitialShowFailed() {
        channel.invokeMethod("onShowFailed", null);
      }
      @Override
      public void onInterstitialClicked() {
        channel.invokeMethod("onClicked", null);
      }
      @Override
      public void onInterstitialClosed() {
        channel.invokeMethod("onClosed", null);
      }
      @Override
      public void onInterstitialExpired()  {
        channel.invokeMethod("onExpired", null);
      }
    });
    Appodeal.cache(activity, Appodeal.INTERSTITIAL);
  }

  public void cacheRewardedVideo(Activity activity){
    Appodeal.setRewardedVideoCallbacks(new RewardedVideoCallbacks() {
      @Override
      public void onRewardedVideoLoaded(boolean isPrecache) {
        channel.invokeMethod("onLoaded", null);
      }
      @Override
      public void onRewardedVideoFailedToLoad() {
        channel.invokeMethod("onFailedToLoad", null);
      }
      @Override
      public void onRewardedVideoShown() {
        channel.invokeMethod("onShown", null);
      }
      @Override
      public void onRewardedVideoShowFailed() {
        channel.invokeMethod("onShowFailed", null);
      }
      @Override
      public void onRewardedVideoClicked() {
        channel.invokeMethod("onClicked", null);
      }
      @Override
      public void onRewardedVideoFinished(double amount, String name) {
        channel.invokeMethod("onRewarded", null);
      }
      @Override
      public void onRewardedVideoClosed(boolean finished) {
        channel.invokeMethod("onClosed", null);
      }
      @Override
      public void onRewardedVideoExpired() {
        channel.invokeMethod("onExpired", null);
      }
    });

    Appodeal.cache(activity, Appodeal.REWARDED_VIDEO);
  }

  public void showRewardedVideo(Activity activity){
    Appodeal.show(activity, Appodeal.REWARDED_VIDEO);
  }

  public void isInterstitialLoaded(){
    Map<String, Boolean> isLoaded = new HashMap<String, Boolean>();
    isLoaded.put("isLoaded", Appodeal.isLoaded(Appodeal.INTERSTITIAL));
    channel.invokeMethod("isInterstitialLoaded", isLoaded);
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {

    String appKey = null;
    boolean testMode = false;

    if(call.argument("appKey") != null){
      appKey = call.argument("appKey");
    }
    else{
      result.error("no_app_key", "No app key provided", null);
    }

    if(call.argument("test") != null){
      testMode = call.argument("test");
    }

    if(call.method.equals("init")){
      init(activity, appKey, testMode);
    }
    if(call.method.equals("cacheInterstitial")){
      cacheInterstitial(activity);
    }
    if(call.method.equals("showInterstitial")){
      showInterstitial(activity);
    }
    if(call.method.equals("cacheRewarded")){
      cacheRewardedVideo(activity);
    }
    if(call.method.equals("showRewarded")){
      showRewardedVideo(activity);
    }
  }
}
