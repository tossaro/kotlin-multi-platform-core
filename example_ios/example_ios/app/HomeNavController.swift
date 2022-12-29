import UIKit
import core
import core_shared
import example_lib

class HomeNavController: UIViewController {
    @IBOutlet weak var scrollView: UIScrollView!
    @IBOutlet weak var searchBar: UISearchBar!
    @IBOutlet weak var headerView: UIView!
    @IBOutlet weak var promoButton: UIButton!
    @IBOutlet weak var notifButton: UIButton!
    @IBOutlet weak var coinsAdapter: GenericItemAdapter!
    
    private var refreshControl: UIRefreshControl!
    private var vm = StockListViewModel()
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        refreshControl = UIRefreshControl()
        refreshControl.addTarget(self, action: #selector(onRefresh), for: .valueChanged)
        scrollView.refreshControl = refreshControl
        scrollView.delegate = self
        scrollView.isPagingEnabled = true
    }
    
    @objc func onRefresh() {
        refreshControl.endRefreshing()
        coinsAdapter.clear()
        vm.page = 1
        vm.getStocks()
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        navigationController?.navigationBar.isHidden = true
        setupHeader()
        setupCoinsAdapter()
        setupObservable()
        vm.getStocks()
    }
}

// MARK: - ScrollViewDelegate
extension HomeNavController: UIScrollViewDelegate {
    func scrollViewDidScroll(_ scrollView: UIScrollView) {
        if scrollView.contentOffset.y >= (scrollView.contentSize.height - scrollView.frame.size.height) {
            scrollView.isScrollEnabled = false
            coinsAdapter.isScrollEnabled = true
        }
    }
}

// MARK: - Action
extension HomeNavController {
    func setupHeader() {
        let gradient = CAGradientLayer()
        gradient.frame = headerView.bounds
        gradient.startPoint = CGPoint(x: 0.0, y: 1.0)
        gradient.endPoint = CGPoint(x: 1.0, y: 0.0)
        gradient.colors = [
            UIColor(hex: "#94B92A").cgColor,
            UIColor(hex: "#7DC954").cgColor,
            UIColor(hex: "#5DA84C").cgColor,
            UIColor(hex: "#5BA74F").cgColor,
            UIColor(hex: "#54A356").cgColor,
            UIColor(hex: "#33907C").cgColor,
            UIColor(hex: "#048E71").cgColor
        ]
        gradient.locations = [-0.092, 0.0547, 0.1032, 0.1033, 0.1596, 0.4073, 0.5745]
        headerView.layer.insertSublayer(gradient, at: 0)
        searchBar.searchTextField.backgroundColor = .white
        if #available(iOS 15, *) {
            notifButton.configuration?.contentInsets = NSDirectionalEdgeInsets(top: CGFloat(0), leading: CGFloat(8), bottom: CGFloat(0), trailing: CGFloat(8))
            promoButton.configuration?.contentInsets = NSDirectionalEdgeInsets(top: CGFloat(0), leading: CGFloat(5), bottom: CGFloat(0), trailing: CGFloat(5))
        }
    }
    
    func setupCoinsAdapter() {
        coinsAdapter.itemSize = CGSize(width: coinsAdapter.bounds.width, height: 70)
        coinsAdapter.shadowOpacity = 0.1
        coinsAdapter.fetchData = {
            self.loadMore()
        }
        coinsAdapter.isScrollEnabled = false
        coinsAdapter.onScrolled = { scrollview in
            if scrollview.contentOffset.y <= 0 && self.coinsAdapter.isScrollEnabled {
                self.scrollView.isScrollEnabled = true
                self.coinsAdapter.isScrollEnabled = false
            }
        }
        coinsAdapter.onSelected = { item in
            print("selected: \(item.title!)")
        }
        coinsAdapter.skeleton = GenericItem(
            _id: 0,
            _leftImage: " ",
            _title: " ",
            _subtitle: " ",
            _middleDiscount: nil,
            _middlePrice: " ",
            _middlePriceUnit: " "
        )
    }
    
    func setupObservable() {
        vm.loadingIndicator.collect(collector: Collector<Bool> { isLoading in
            self.onLoading(isLoading)
        }){ e in }
        vm.stocks.collect(collector: Collector<[Stock]> { stocks in
            if let its = stocks {
                self.coinsAdapter.addItems(its.count) {
                    for it in its {
                        let item = GenericItem(
                            _id: KotlinInt(pointer: it.id),
                            _leftImage: it.imageUrl,
                            _title: it.name,
                            _subtitle: it.fullname,
                            _middleDiscount: nil,
                            _middlePrice: it.price,
                            _middlePriceUnit: " / \(it.status ?? ""))"
                        )
                        self.coinsAdapter.items.append(item)
                        self.coinsAdapter.itemsCache.append(item)
                    }
                }
            }
        }){ e in }
    }

    func loadMore() {
        vm.page += 1
        vm.getStocks()
    }

    func onLoading(_ isLoading: Bool?) {
        if let showLoading = isLoading {
            if coinsAdapter.itemsCache.count == 0 && showLoading {
                coinsAdapter.showSkeletons()
            }
        }
    }
}
