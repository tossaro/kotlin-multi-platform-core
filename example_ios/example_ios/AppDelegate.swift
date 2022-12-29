import UIKit
import example_lib

@main
class AppDelegate: UIResponder, UIApplicationDelegate {
    let koinApp = ExampleKoinKt.doInitKoin(
        context: NSObject(),
        host: "${HOST}",
        deviceId: UIDevice.current.identifierForVendor?.uuidString ?? "ios-${random()}",
        version: UIApplication.appVersion
    )
    
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {        
        return true
    }

    // MARK: UISceneSession Lifecycle
    func application(_ application: UIApplication, configurationForConnecting connectingSceneSession: UISceneSession, options: UIScene.ConnectionOptions) -> UISceneConfiguration {
        return UISceneConfiguration(name: "Default Configuration", sessionRole: connectingSceneSession.role)
    }
}

public class Collector<T>: Kotlinx_coroutines_coreFlowCollector {
    let callback:(T?) -> Void

    public init(callback: @escaping (T?) -> Void) {
        self.callback = callback
    }
    
    public func emit(value: Any?, completionHandler: @escaping (Error?) -> Void) {
        callback(value as? T)
        completionHandler(nil)
    }
}
