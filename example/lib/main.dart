import 'package:flutter/material.dart';
import 'package:flutter_appodeal_ads/flutter_appodeal_ads.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';

  @override
  void initState() {
    super.initState();
    AppodealAds.init(appKey: "<Your App Key>", testMode: true);
  }

  interstitialListener(AppodealAdListener event) {
    if (event == AppodealAdListener.onLoaded) {
      AppodealAds.showInterstitial();
    }
  }

  rewardedListener(AppodealAdListener event) {
    if (event == AppodealAdListener.onLoaded) {
      AppodealAds.showRewardedVideo();
    }
    if (event == AppodealAdListener.onRewarded) {
      print("Video completed and rewarded");
    }
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Column(
          children: [
            Center(
              child: ElevatedButton(
                onPressed: () {
                  AppodealAds.cacheInterstitial((AppodealAdListener event) =>
                      interstitialListener(event));
                },
                child: Text("Show Interstitial"),
              ),
            ),
            SizedBox(
              height: 20.0,
            ),
            Center(
              child: ElevatedButton(
                onPressed: () {
                  AppodealAds.cacheRewardedVideo(
                      (AppodealAdListener event) => rewardedListener(event));
                },
                child: Text("Show Rewarded video"),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
