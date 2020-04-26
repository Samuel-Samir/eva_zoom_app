import 'dart:async';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class SplashScreen extends StatefulWidget{
  @override
  State<StatefulWidget> createState() {
    return SplashState();
  }
}

class SplashState extends State<SplashScreen>{
  static const platform = const MethodChannel('com.eva.zoom/zoom');

  @override
  Widget build(BuildContext context) {
    return new Scaffold(
        body: new Container(
          padding: EdgeInsets.all(30.0),
          child: new Column(
            crossAxisAlignment: CrossAxisAlignment.center,
            children: <Widget>[
              Expanded(
                child: Container(
                  alignment: Alignment.center,
                  child: Image.asset(
                    'images/eva_icon.png',
                    width: 200,
                    height: 200,
                  ),
                ),
              ),
            ],
          ),
        ));
  }


  @override
  void initState() {
    super.initState();
    _navigate();

  }
  _navigate() async {

    Future.delayed(Duration(milliseconds: 500)).then((_) async {
      print("samuel");
      platform.invokeMethod('zoom');
    });
  }

}