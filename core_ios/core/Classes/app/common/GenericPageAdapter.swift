import UIKit

public class GenericPageAdapter: UIPageViewController {
    public var initialPage = 0
    public var pages = [UIViewController]()
    public var pageControl = UIPageControl()
    public var isEnableLoop = false
    
    public override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        dataSource = self
        delegate = self
        setViewControllers([pages[initialPage]], direction: .forward, animated: true, completion: nil)
    }
}

// MARK: - DataSource
extension GenericPageAdapter: UIPageViewControllerDataSource {
    public func pageViewController(_ pageViewController: UIPageViewController, viewControllerBefore viewController: UIViewController) -> UIViewController? {

        guard let currentIndex = pages.firstIndex(of: viewController) else { return nil }
        
        if currentIndex == 0 {
            return isEnableLoop ? pages.last : nil
        } else {
            return pages[currentIndex - 1]
        }
    }
        
    public func pageViewController(_ pageViewController: UIPageViewController, viewControllerAfter viewController: UIViewController) -> UIViewController? {
        
        guard let currentIndex = pages.firstIndex(of: viewController) else { return nil }

        if currentIndex < pages.count - 1 {
            return pages[currentIndex + 1]
        } else {
            return isEnableLoop ? pages.first : nil
        }
    }
}

// MARK: - Delegates
extension GenericPageAdapter: UIPageViewControllerDelegate {
    public func pageViewController(_ pageViewController: UIPageViewController, didFinishAnimating finished: Bool, previousViewControllers: [UIViewController], transitionCompleted completed: Bool) {
        
        guard let viewControllers = pageViewController.viewControllers else { return }
        guard let currentIndex = pages.firstIndex(of: viewControllers[0]) else { return }
        
        pageControl.currentPage = currentIndex
    }
}
