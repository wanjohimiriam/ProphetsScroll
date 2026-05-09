// lib/features/devotional/reader/reader_binding.dart

import 'package:get/get.dart';
import 'package:prophets_scroll/features/controllers/reader_controller.dart';
import 'package:prophets_scroll/features/devotional/DevotionalRepository.dart';

class ReaderBinding extends Bindings {
  @override
  void dependencies() {
    Get.lazyPut<ReaderController>(
      () => ReaderController(Get.find<DevotionalRepository>()),
    );
  }
}