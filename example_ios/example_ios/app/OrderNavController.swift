import core
import core_shared

class OrderNavController: UIViewController {
    @IBOutlet weak var scrollView: UIScrollView!
    @IBOutlet weak var provincesCompactAdapter: GenericItemAdapter!
    @IBOutlet weak var provincesAdapter: GenericItemAdapter!
    
    private var refreshControl: UIRefreshControl!
    let dummyImage = "https://images.unsplash.com/photo-1518509562904-e7ef99cdcc86?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1074&q=80"
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        refreshControl = UIRefreshControl()
        refreshControl.addTarget(self, action: #selector(onRefresh), for: .valueChanged)
        scrollView.refreshControl = refreshControl
    }
    
    @objc func onRefresh() {
        refreshControl.endRefreshing()
        provincesCompactAdapter.clear()
        provincesAdapter.clear()
        getData()
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        setupProvincesCompact()
        setupProvinces()
        getData()
    }
    
    func setupProvincesCompact() {
        provincesCompactAdapter.itemSize = CGSize(width: provincesCompactAdapter.bounds.width * 0.6, height: 124)
        provincesCompactAdapter.enableShadow = false
        provincesCompactAdapter.onSelected = { item in
            print("selected: \(item.id!)")
        }
        provincesCompactAdapter.skeleton = GenericItem(
            _id: 0,
            _fullImage: " ",
            _titleOverlay: " ",
            _subtitleOverlay: " "
        )
    }
    
    func setupProvinces() {
        provincesAdapter.itemSize = CGSize(width: provincesAdapter.bounds.width, height: 240)
        provincesAdapter.isScrollEnabled = false
        provincesAdapter.onSelected = { item in
            print("selected: \(item.id!)")
        }
        provincesAdapter.skeleton = GenericItem(
            _id: 0,
            _topImage: " ",
            _title: " ",
            _subtitle: " ",
            _rightDiscount: " ",
            _rightPrice: " "
        )
    }
    
    func getData() {
        provincesCompactAdapter.showSkeletons(2)
        provincesAdapter.showSkeletons(2)
        
        DispatchQueue.main.asyncAfter(deadline: .now() + 5.0) {
            self.provincesCompactAdapter.clear()
            self.provincesCompactAdapter.addItems(5) {
                for i in 1...5 {
                    self.provincesCompactAdapter.items.append(GenericItem(
                        _id: KotlinInt(integerLiteral: i),
                        _fullImage: self.dummyImage,
                        _titleOverlay: "This is Title \(i)",
                        _subtitleOverlay: "And This is Subtitle \(i)"
                    ))
                }
            }
            
            self.provincesAdapter.clear()
            self.provincesAdapter.addItems(5) {
                for i in 1...5 {
                    self.provincesAdapter.items.append(GenericItem(
                        _id: KotlinInt(integerLiteral: i),
                        _topImage: self.dummyImage,
                        _title: "This is Title \(i)",
                        _subtitle: "And This is Subtitle \(i)",
                        _rightDiscount: nil,
                        _rightPrice: nil
                    ))
                }
            }
        }
    }
}
