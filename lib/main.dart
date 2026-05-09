import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:prophets_scroll/core/shell/main_shell.dart';
import 'package:prophets_scroll/core/bindings/app_bindings.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return GetMaterialApp(
      title: 'Prophets Scroll',
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.deepPurple),
      ),
      home: const MainShell(),
      initialBinding: AppBindings(),
    );
  }
}

