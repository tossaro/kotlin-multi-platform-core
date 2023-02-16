import Foundation
import UIKit

public class BottomNavController: UITabBarController {
    public static let identifier = "BottomNavController"
    public override func viewDidLoad() {
        super.viewDidLoad()
        viewControllers?[0].tabBarItem = UITabBarItem(title: "Home", image: UIImage(systemName: "house"), tag: 0)
        viewControllers?[1].tabBarItem = UITabBarItem(title: "Order", image: UIImage(systemName: "cart"), tag: 1)
        viewControllers?[2].tabBarItem = UITabBarItem(title: "Profile", image: UIImage(systemName: "person.circle"), tag: 2)
        
        if #available(iOS 13, *) {
            let appearance = tabBar.standardAppearance.copy()
            appearance.backgroundImage = UIImage()
            appearance.shadowImage = UIImage()
            tabBar.standardAppearance = appearance
        } else {
            tabBar.shadowImage = UIImage()
            tabBar.backgroundImage = UIImage()
        }

        tabBar.layer.shadowColor = UIColor.lightGray.cgColor
        tabBar.layer.shadowOpacity = 0.3
        tabBar.layer.shadowRadius = 5
        tabBar.tintColor = .primary
    }
}
