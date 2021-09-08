import 'dart:async';

import 'package:flutter/services.dart';

typedef void OnInterstitialLoaded(bool isPreCache);
typedef void OnInterstitialFailedToLoad();
typedef void OnInterstitialShown();
typedef void OnInterstitialShowFailed();
typedef void OnInterstitialClicked();
typedef void OnInterstitialClosed();
typedef void OnInterstitialExpired();

enum AppodealAdListener {
  onLoaded,
  onFailedToLoad,
  onShown,
  onShowFailed,
  onClicked,
  onClosed,
  onExpired,
  onRewarded
}

class AppodealAds {
  static final MethodChannel _channel = MethodChannel("flutter_appodeal_ads");

  static final Map<String, AppodealAdListener> appodealAdListeners = {
    "onLoaded": AppodealAdListener.onLoaded,
    "onFailedToLoad": AppodealAdListener.onFailedToLoad,
    "onShown": AppodealAdListener.onShown,
    "onShowFailed": AppodealAdListener.onShowFailed,
    "onClicked": AppodealAdListener.onClicked,
    "onClosed": AppodealAdListener.onClosed,
    "onExpired": AppodealAdListener.onExpired,
    "onRewarded": AppodealAdListener.onRewarded
  };

  static Future<void> init(
      {required String appKey, bool testMode = false}) async {
    return _channel.invokeMethod(
        "init", <String, dynamic>{"appKey": appKey, "test": testMode});
  }

  static Future<void> cacheInterstitial(listener) async {
    _channel.setMethodCallHandler(
        (MethodCall call) async => await _platformCallHandler(call, listener));
    return _channel.invokeMethod("cacheInterstitial");
  }

  static Future<void> showInterstitial() async {
    await _channel.invokeMethod("showInterstitial");
  }

  static Future<void> cacheRewardedVideo(listener) async {
    _channel.setMethodCallHandler(
        (MethodCall call) async => await _platformCallHandler(call, listener));
    return _channel.invokeMethod("cacheRewarded");
  }

  static Future<void> showRewardedVideo() async {
    await _channel.invokeMethod("showRewarded");
  }

  static Future<void> _platformCallHandler(MethodCall call, listener) async {
    listener(appodealAdListeners[call.method]);
  }
}
