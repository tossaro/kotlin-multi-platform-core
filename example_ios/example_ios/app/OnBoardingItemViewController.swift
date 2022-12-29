import UIKit

class OnBoardingItemViewController: UIViewController {
    @IBOutlet weak var imageView: UIImageView!
    @IBOutlet weak var titleView: UILabel!
    @IBOutlet weak var descriptionView: UILabel!
    
    var imageUrl: String!
    var titleStr: String!
    var descriptionStr: String!
    
    static let identifier = "OnBoardingItemViewController"
    static func nib() -> UINib {
        return UINib(nibName: "OnBoardingItemViewController", bundle: nil)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.imageView.downloaded(from: imageUrl)
        self.titleView.text = titleStr
        self.descriptionView.text = descriptionStr
    }
    
    init(imageUrl: String, title: String, description: String) {
        super.init(nibName: nil, bundle: nil)
        self.imageUrl = imageUrl
        self.titleStr = title
        self.descriptionStr = description
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
}
