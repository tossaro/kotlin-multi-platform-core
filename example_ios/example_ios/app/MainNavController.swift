import UIKit
import core
import core_shared
import example_lib

class MainNavController: UIViewController {
    @IBOutlet weak var pageControl: UIPageControl!
    @IBOutlet weak var skipBtn: UIButton!
    @IBOutlet weak var nextBtn: UIButton!
    
    private var vm = OnBoardingViewModel()
    
    @IBAction func nextAction(_ sender: Any) {
        vm.next()
    }
    
    let initialPage = 0
    var pages = [UIViewController]()
    var onBoardingAdapter: GenericPageAdapter?
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        let skipedOnBoarding = Preferences.value(forKey: AppConstant.shared.ONBOARDING, defaultValue: false)
        if skipedOnBoarding {
            goToBottomNav()
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        vm.loadingIndicator.collect(collector: Collector<Bool> { isLoading in
            if let l = isLoading { self.view.showFulLoading(l) }
        }){ e in }
        vm.onNext.collect(collector: Collector<Bool> { isNext in
            if let n = isNext, n == true {
                if self.pageControl.currentPage < 2 {
                    self.pageControl.currentPage += 1
                    self.onBoardingAdapter?.goToNextPage()
                    self.setStyle()
                } else {
                    Preferences.value(forKey: AppConstant.shared.ONBOARDING, value: true)
                    self.goToBottomNav()
                }
                self.vm.onNext.setValue(false)
            }
        }){ e in }
        
        setStyle()
        onBoardingAdapter = self.children[0] as? GenericPageAdapter
        
        pageControl.addTarget(self, action: #selector(pageControlTapped(_:)), for: .valueChanged)
        pageControl.currentPage = initialPage
        onBoardingAdapter?.initialPage = initialPage
        onBoardingAdapter?.pageControl = pageControl
        
        for _ in 1...3 {
            pages.append(OnBoardingItemViewController(
                imageUrl: "https://dcxlearn.com/wp-content/uploads/2020/06/BC2-65-1170x660.jpg",
                title: "Lorem ipsum dolor sit amet",
                description: "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua"
            ))
        }
        onBoardingAdapter?.pages = pages
    }
    
    @objc func pageControlTapped(_ sender: UIPageControl) {
        onBoardingAdapter?.setViewControllers([pages[sender.currentPage]], direction: .forward, animated: true, completion: nil)
    }
    
    func goToBottomNav() {
        performSegue(withIdentifier: "BottomNavSegue", sender: nil)
    }
    
    func setStyle() {
        skipBtn.titleLabel?.textColor = .title
        skipBtn.titleLabel?.font = UIFont(name: "poppins_semi_bold", size: CGFloat(13))
        nextBtn.titleLabel?.textColor = .primary
        nextBtn.titleLabel?.font = UIFont(name: "poppins_semi_bold", size: CGFloat(13))
    }
}
