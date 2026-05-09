import 'package:get/get.dart';
import 'package:prophets_scroll/data/repository/DevotionalRepositoryImpl.dart';
import 'package:prophets_scroll/features/devotional/DevotionalRepository.dart';
import 'package:prophets_scroll/features/controllers/home_controller.dart';


class HomeBinding extends Bindings {
  @override
  void dependencies() {
    Get.lazyPut<DevotionalRepository>(
      () => DevotionalRepositoryImpl(),
    );
    Get.lazyPut<HomeController>(
      () => HomeController(Get.find<DevotionalRepository>()),
    );
  }
}