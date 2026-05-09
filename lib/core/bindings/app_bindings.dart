import 'package:get/get.dart';
import 'package:prophets_scroll/data/repository/DevotionalRepositoryImpl.dart';
import 'package:prophets_scroll/features/devotional/DevotionalRepository.dart';
import 'package:prophets_scroll/features/controllers/home_controller.dart';

/// Global application bindings - initializes all controllers and dependencies on app start
class AppBindings extends Bindings {
  @override
  void dependencies() {
    // Register repositories
    Get.lazyPut<DevotionalRepository>(
      () => DevotionalRepositoryImpl(),
      fenix: true, // Recreate after dispose
    );

    // Register controllers
    Get.lazyPut<HomeController>(
      () => HomeController(Get.find<DevotionalRepository>()),
      fenix: true,
    );
  }
}
